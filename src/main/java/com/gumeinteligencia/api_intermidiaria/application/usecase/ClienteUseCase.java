package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.ClienteGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteUseCase {

    private final ClienteGateway clienteGateway;

    public Optional<Cliente> consultarPorTelefone(String telefone) {
        return clienteGateway.consultarPorTelefone(telefone);
    }
}
