package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.application.gateways.MensageriaGateway;
import com.gumeinteligencia.api_intermidiaria.domain.TranscricaoNotificacao;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.mapper.TranscricaoNotificacaoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.List;

@Component
@Slf4j
public class MensageriaDataProvider implements MensageriaGateway {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    private final String MENSAGEM_ERRO_ENVIAR_PARA_FILA = "Erro ao enviar contexto para a fila SQS";
    private final String MENSAGEM_ERRO_LISTAR_CONTEXTOS_SQS = "Erro ao listar contextos da fila SQS.";

    @Value("${aws.sqs.url.transcripe}")
    private final String queueUrlTranscripe;

    @Value("${aws.sqs.url}")
    private final String queueUrl;

    public MensageriaDataProvider(
            SqsClient sqsClient,
            ObjectMapper objectMapper,
            @Value("${aws.sqs.url}") String queueUrl,
            @Value("${aws.sqs.url.transcripe}") String queueUrlTranscripe
    ) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.queueUrl = queueUrl;
        this.queueUrlTranscripe = queueUrlTranscripe;
    }

    @Override
    public SendMessageResponse enviarParaFila(Contexto contexto) {
        try {
            String json = objectMapper.writeValueAsString(contexto);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .messageGroupId("message-group-" + contexto.getId().toString())
                    .build();

            return sqsClient.sendMessage(request);

        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_ENVIAR_PARA_FILA, ex);
            throw new DataProviderException(MENSAGEM_ERRO_ENVIAR_PARA_FILA, ex);
        }
    }

    @Override
    public List<TranscricaoNotificacao> listarTranscricoes() {
        try {
            ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrlTranscripe)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(5)
                    .build();

            List<Message> messages = sqsClient.receiveMessage(request).messages();

            return messages.stream()
                    .map(TranscricaoNotificacaoMapper::paraDomainDeMessage)
                    .toList();
        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_LISTAR_CONTEXTOS_SQS, ex);
            throw new DataProviderException(MENSAGEM_ERRO_LISTAR_CONTEXTOS_SQS, ex.getCause());
        }
    }
}
