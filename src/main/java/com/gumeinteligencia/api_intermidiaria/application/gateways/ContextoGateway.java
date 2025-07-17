package com.gumeinteligencia.api_intermidiaria.application.gateways;

import com.gumeinteligencia.api_intermidiaria.domain.Contexto;

import java.util.Optional;

public interface ContextoGateway {
    Optional<Contexto> consultarPorTelefoneAtivo(String telefone);

    Contexto salvar(Contexto contexto);
}
