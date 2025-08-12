package com.gumeinteligencia.api_intermidiaria.application.gateways;

import com.gumeinteligencia.api_intermidiaria.domain.Cliente;

import java.util.Optional;

public interface ClienteGateway {
    Optional<Cliente> consultarPorTelefone(String telefone);
}
