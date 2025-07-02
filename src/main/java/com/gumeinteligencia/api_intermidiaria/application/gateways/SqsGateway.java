package com.gumeinteligencia.api_intermidiaria.application.gateways;

import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

public interface SqsGateway {
    SendMessageResponse enviarParaFila(Contexto novoContexto);
}
