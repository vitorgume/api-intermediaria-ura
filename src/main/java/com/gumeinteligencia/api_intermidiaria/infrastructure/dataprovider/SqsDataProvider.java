package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.application.gateways.SqsGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Component
@Slf4j
public class SqsDataProvider implements SqsGateway {

    private final String MENSAGEM_ERRO_ENVIAR_PARA_FILA = "Erro ao enviar contexto para a fila SQS";
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.url}")
    private final String queueUrl;

    @Value("${aws.sqs.delay}")
    private final Integer delay;

    public SqsDataProvider(
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

    @Override
    public SendMessageResponse enviarParaFila(Contexto contexto) {
        try {
            String json = objectMapper.writeValueAsString(contexto);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .delaySeconds(delay)
                    .build();

            return sqsClient.sendMessage(request);

        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_ENVIAR_PARA_FILA, ex);
            throw new DataProviderException(MENSAGEM_ERRO_ENVIAR_PARA_FILA, ex);
        }
    }
}
