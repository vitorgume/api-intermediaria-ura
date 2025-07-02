package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ContextoMapperTest {

    private Contexto contextoDomain;
    private ContextoEntity contextoEntity;

    @BeforeEach
    void setUp() {
        contextoDomain = Contexto.builder()
                .id(UUID.randomUUID())
                .telefone("000000000000")
                .status(StatusContexto.ATIVO)
                .mensagens(List.of("Olá"))
                .build();

        contextoEntity = ContextoEntity.builder()
                .id(UUID.randomUUID())
                .telefone("000000000000")
                .status(StatusContexto.ATIVO)
                .mensagens(List.of("Olá"))
                .build();
    }

    @Test
    void deveRetornarDomain() {
        Contexto resultado = ContextoMapper.paraDomain(contextoEntity);

        Assertions.assertEquals(contextoEntity.getId(), resultado.getId());
        Assertions.assertEquals(contextoEntity.getTelefone(), resultado.getTelefone());
        Assertions.assertEquals(contextoEntity.getStatus(), resultado.getStatus());
        Assertions.assertEquals(contextoEntity.getMensagens(), resultado.getMensagens());
    }

    @Test
    void deveRetornarEntity() {
        ContextoEntity resultado = ContextoMapper.paraEntity(contextoDomain);

        Assertions.assertEquals(contextoDomain.getId(), resultado.getId());
        Assertions.assertEquals(contextoDomain.getTelefone(), resultado.getTelefone());
        Assertions.assertEquals(contextoDomain.getStatus(), resultado.getStatus());
        Assertions.assertEquals(contextoDomain.getMensagens(), resultado.getMensagens());
    }
}