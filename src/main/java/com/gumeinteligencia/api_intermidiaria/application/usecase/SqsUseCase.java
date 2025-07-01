package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

@Service
@RequiredArgsConstructor
public class SqsUseCase {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private String queueUrl = "http://localhost:4566/000000000000/minha-fila-teste";

    public void enviarParaFila(Contexto novoContexto) {

    }
}
