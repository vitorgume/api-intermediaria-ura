package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;

public class ContextoMapper {

    public static Contexto paraDomain(ContextoEntity entity) {
        return Contexto.builder()
                .id(entity.getId())
                .mensagens(entity.getMensagens())
                .telefone(entity.getTelefone())
                .status(entity.getStatus())
                .build();
    }

    public static ContextoEntity paraEntity(Contexto domain) {
        return ContextoEntity.builder()
                .id(domain.getId())
                .mensagens(domain.getMensagens())
                .telefone(domain.getTelefone())
                .status(domain.getStatus())
                .build();
    }
}
