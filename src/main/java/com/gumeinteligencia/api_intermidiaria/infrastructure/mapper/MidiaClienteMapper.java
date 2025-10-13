package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.MidiaCliente;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.MidiaClienteEntity;

public class MidiaClienteMapper {

    public static MidiaCliente paraDomain(MidiaClienteEntity entity) {
        return MidiaCliente.builder()
                .id(entity.getId())
                .telefoneCliente(entity.getTelefoneCliente())
                .urlMidias(entity.getUrlMidias())
                .build();
    }

    public static MidiaClienteEntity paraEntity(MidiaCliente domain) {
        return MidiaClienteEntity.builder()
                .id(domain.getId())
                .telefoneCliente(domain.getTelefoneCliente())
                .urlMidias(domain.getUrlMidias())
                .build();
    }
}
