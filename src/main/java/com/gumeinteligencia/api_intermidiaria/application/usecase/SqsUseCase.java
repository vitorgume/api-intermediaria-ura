package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqsUseCase {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private String queueUrl = "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/fila-contexto";
    private Integer delay = 25;

    public void enviarParaFila(Contexto contexto) {
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
