package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class ContextoMapperTest {

    private Contexto contextoDomain;
    private ContextoEntity contextoEntity;

    @BeforeEach
    void setUp() {
        contextoDomain = Contexto.builder()
                .id(UUID.randomUUID())
                .telefone("000000000000")
                .mensagens(List.of("Ola"))
                .build();

        contextoEntity = ContextoEntity.builder()
                .id(UUID.randomUUID())
                .telefone("000000000000")
                .mensagens(List.of("Ola"))
                .build();
    }

    @Test
    void deveRetornarDomain() {
        Contexto resultado = ContextoMapper.paraDomain(contextoEntity);

        Assertions.assertEquals(contextoEntity.getId(), resultado.getId());
        Assertions.assertEquals(contextoEntity.getTelefone(), resultado.getTelefone());
        Assertions.assertEquals(contextoEntity.getMensagens(), resultado.getMensagens());
    }

    @Test
    void deveRetornarEntity() {
        ContextoEntity resultado = ContextoMapper.paraEntity(contextoDomain);

        Assertions.assertEquals(contextoDomain.getId(), resultado.getId());
        Assertions.assertEquals(contextoDomain.getTelefone(), resultado.getTelefone());
        Assertions.assertEquals(contextoDomain.getMensagens(), resultado.getMensagens());
    }

    @Test
    void devePermitirInstanciacao() {
        Assertions.assertNotNull(new ContextoMapper());
    }
}
