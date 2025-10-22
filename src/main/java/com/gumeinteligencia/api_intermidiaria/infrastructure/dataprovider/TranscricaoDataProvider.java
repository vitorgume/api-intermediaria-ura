package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.application.gateways.TranscricaoGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Transcricao;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.MidiaClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class TranscricaoDataProvider implements TranscricaoGateway {

    private static final String MENSAGEM_ERRO_SALVAR_AUDIO = "Erro ao salvar audio.";

    private final S3Client s3;
    private final String bucket;


    private static final Map<String, String> EXT_BY_CONTENT_TYPE = Map.ofEntries(
            Map.entry("audio/mpeg", ".mp3"),
            Map.entry("audio/mp3",  ".mp3"),
            Map.entry("audio/ogg",  ".ogg"),
            Map.entry("audio/opus", ".opus"),
            Map.entry("audio/webm", ".webm"),
            Map.entry("audio/wav",  ".wav"),
            Map.entry("audio/x-wav",".wav"),
            Map.entry("audio/flac", ".flac"),
            Map.entry("audio/amr",  ".amr"),
            Map.entry("audio/mp4",  ".mp4"),
            Map.entry("audio/aac",  ".aac"),
            Map.entry("audio/m4a",  ".m4a")
    );

    public TranscricaoDataProvider(
            @Value("${app.storage.s3.bucket}") String bucket,
            @Value("${app.storage.s3.region}") String region,
            @Value("${app.storage.s3.access-key:}") String accessKey,
            @Value("${app.storage.s3.secret-key:}") String secretKey,
            @Value("${app.storage.s3.session-token:}") String sessionToken,
            @Value("${app.storage.s3.endpoint:}") String endpoint,
            @Value("${app.storage.s3.path-style:false}") boolean pathStyle
    ) {
        // Config do S3
        var s3Cfg = S3Configuration.builder()
                .pathStyleAccessEnabled(pathStyle)
                .build();

        // Credenciais
        AwsCredentialsProvider credsProvider;
        boolean hasAccess = accessKey != null && !accessKey.isBlank();
        boolean hasSecret = secretKey != null && !secretKey.isBlank();

        if (hasAccess || hasSecret) {
            if (!hasAccess || !hasSecret) {
                throw new IllegalStateException("Defina access-key e secret-key ou nenhuma das duas.");
            }
            if (sessionToken != null && !sessionToken.isBlank()) {
                credsProvider = StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(accessKey, secretKey, sessionToken));
            } else {
                credsProvider = StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey));
            }
        } else {
            credsProvider = DefaultCredentialsProvider.create();
        }

        var s3Builder = S3Client.builder()
                .region(Region.of(region))
                .serviceConfiguration(s3Cfg)
                .credentialsProvider(credsProvider);

        var presignerBuilder = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(credsProvider);

        Optional.ofNullable(endpoint)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .ifPresent(ep -> {
                    URI uri = URI.create(ep);
                    s3Builder.endpointOverride(uri);
                    presignerBuilder.endpointOverride(uri);
                });

        this.s3 = s3Builder.build();

        this.bucket = bucket;
    }

    @Override
    public void enviarAudioTranscricao(byte[] bytes, String telefone, String fileName) {
        String ext = (fileName != null && fileName.contains("."))
                ? fileName.substring(fileName.lastIndexOf('.'))
                : guessExtFromContentType("audio/mpeg");

        String key = String.format("transcribe/incoming/%s/%s%s",
                telefone.replaceAll("\\D", ""), UUID.randomUUID(), ext != null ? ext : "");

        try {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType("audio/mpeg")
                    .metadata(Map.of(
                            "telefone", telefone,
                            "arquivo-origem", fileName != null ? fileName : "desconhecido"
                    ))
                    .serverSideEncryption(ServerSideEncryption.AES256)
                    .build();

            s3.putObject(putReq, RequestBody.fromBytes(bytes));

            log.info("Áudio enviado ao S3: s3://{}/{}", bucket, key);
        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_SALVAR_AUDIO + " (S3)", ex);
            throw new DataProviderException(MENSAGEM_ERRO_SALVAR_AUDIO, ex);
        }
    }


    @Override
    public byte[] baixarAudio(String urlAudio) {
        try {
            if (isLocalFile(urlAudio)) {
                return Files.readAllBytes(toPath(urlAudio));
            }
            return WebClient.create()
                    .get().uri(URI.create(urlAudio))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (Exception e) {
            throw new DataProviderException("Falha ao baixar arquivo de origem.", e);
        }
    }

    @Override
    public Transcricao baixarTranscricao(String s3Key, String bucket) {
        try {
            var getReq = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build();

            try (ResponseInputStream<GetObjectResponse> in = s3.getObject(getReq)) {
                String json = new String(in.readAllBytes(), StandardCharsets.UTF_8);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);

                // Extrai campos do JSON normalizado
                String transcript = root.path("transcript").asText("");
                String telefone   = root.path("telefone").asText("");
                String jobName    = root.path("jobName").asText("");
                String language   = root.path("language").asText("");
                long durationMs   = root.path("durationMs").asLong(0L);

                log.info("Transcrição carregada do S3: telefone={} jobName={} tamanho={} caracteres",
                        telefone, jobName, transcript.length());

                return Transcricao.builder()
                        .bucket(bucket)
                        .key(s3Key)
                        .body(transcript)
                        .build();

            }
        } catch (Exception ex) {
            log.error("Erro ao carregar transcrição do S3: {}/{}", bucket, s3Key, ex);
            throw new DataProviderException("Erro ao carregar transcrição.", ex);
        }
    }

    private static String guessExtFromContentType(String contentType) {
        if (contentType == null) return null;
        String ct = contentType.toLowerCase(Locale.ROOT).trim();

        int sc = ct.indexOf(';');
        if (sc > -1) ct = ct.substring(0, sc).trim();

        return EXT_BY_CONTENT_TYPE.getOrDefault(ct, null);
    }

    private static Path toPath(String s) {
        return s.startsWith("file:") ? Paths.get(URI.create(s)) : Paths.get(s);
    }

    private static boolean isLocalFile(String s) {
        if (s == null || s.isBlank()) return false;
        return s.startsWith("file:") || !(s.startsWith("http://") || s.startsWith("https://"));
    }
}
