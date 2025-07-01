package com.gumeinteligencia.api_intermidiaria.application.gateways;

import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;

import java.util.List;

public interface OutroContatoGateway {
    List<OutroContato> listar();
}
