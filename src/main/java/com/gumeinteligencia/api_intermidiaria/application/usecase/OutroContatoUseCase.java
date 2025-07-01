package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.OutroContatoGateway;
import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutroContatoUseCase {

    private final OutroContatoGateway gateway;

    public List<OutroContato> listar() {
        return gateway.listar();
    }
}
