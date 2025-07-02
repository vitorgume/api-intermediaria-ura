package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Slf4j
public class SqsUseCase {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.url}")
    private final String queueUrl;

    @Value("${aws.sqs.delay}")
    private final Integer delay;

    public SqsUseCase(
            SqsClient sqsClient,
            ObjectMapper objectMapper,
            @Value("${aws.sqs.url}") String queueUrl,
            @Value("${aws.sqs.delay}") Integer delay
    ) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.queueUrl = queueUrl;
        this.delay = delay;
    }

    public void enviarParaFila(Contexto contexto) {
        log.info("Enviando contexto para a fila. Contexto: {}", contexto);

        try {
            String json = objectMapper.writeValueAsString(contexto);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .delaySeconds(delay)
                    .build();

            var response = sqsClient.sendMessage(request);

            log.info("Contexto enviado com delay de {}s: {}, response: {}", delay, contexto.getTelefone(), response);

        } catch (Exception e) {
            log.error("Erro ao enviar contexto para a fila SQS", e);
            throw new RuntimeException(e);
        }
    }
}
