package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.domain.TranscricaoNotificacao;
import software.amazon.awssdk.services.sqs.model.Message;

public class TranscricaoNotificacaoMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static TranscricaoNotificacao paraDomainDeMessage(Message message) {
        try {
            TranscricaoNotificacao transcricaoNotificacao = objectMapper.readValue(message.body(), TranscricaoNotificacao.class);
            transcricaoNotificacao.setMensagemFila(message);
            return transcricaoNotificacao;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter mensagem da fila para Contexto", e);
        }
    }
}
