package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.OutroContatoEntity;

public class OutroContatoMapper {

    public static OutroContato paraDomain(OutroContatoEntity entity) {
        return OutroContato.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .telefone(entity.getTelefone())
                .setor(entity.getSetor())
                .build();
    }

    public static OutroContatoEntity paraEntity(OutroContato domain) {
        return OutroContatoEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .telefone(domain.getTelefone())
                .setor(domain.getSetor())
                .build();
    }
}
