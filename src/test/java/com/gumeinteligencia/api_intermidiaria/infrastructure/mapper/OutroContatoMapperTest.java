package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;
import com.gumeinteligencia.api_intermidiaria.domain.outroContato.Setor;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.OutroContatoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OutroContatoMapperTest {

    private OutroContato outroContatoDomain;
    private OutroContatoEntity outroContatoEntity;

    @BeforeEach
    void setUp() {
        outroContatoDomain = OutroContato.builder()
                .id(UUID.randomUUID())
                .nome("Nome teste")
                .setor(Setor.LOGISTICA)
                .descricao("Descricao teste")
                .telefone("000000000000")
                .build();

        outroContatoEntity = OutroContatoEntity.builder()
                .id(UUID.randomUUID())
                .nome("Nome teste")
                .setor(Setor.LOGISTICA)
                .descricao("Descricao teste")
                .telefone("000000000000")
                .build();
    }

    @Test
    void paraDomain() {
        OutroContato resultado = OutroContatoMapper.paraDomain(outroContatoEntity);

        Assertions.assertEquals(outroContatoEntity.getId(), resultado.getId());
        Assertions.assertEquals(outroContatoEntity.getNome(), resultado.getNome());
        Assertions.assertEquals(outroContatoEntity.getTelefone(), resultado.getTelefone());
        Assertions.assertEquals(outroContatoEntity.getDescricao(), resultado.getDescricao());
        Assertions.assertEquals(outroContatoEntity.getSetor(), resultado.getSetor());
    }

    @Test
    void paraEntity() {
        OutroContatoEntity resultado = OutroContatoMapper.paraEntity(outroContatoDomain);

        Assertions.assertEquals(outroContatoDomain.getId(), resultado.getId());
        Assertions.assertEquals(outroContatoDomain.getNome(), resultado.getNome());
        Assertions.assertEquals(outroContatoDomain.getTelefone(), resultado.getTelefone());
        Assertions.assertEquals(outroContatoDomain.getDescricao(), resultado.getDescricao());
        Assertions.assertEquals(outroContatoDomain.getSetor(), resultado.getSetor());
    }
}