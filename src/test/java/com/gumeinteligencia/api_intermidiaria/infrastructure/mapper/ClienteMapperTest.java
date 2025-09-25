package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.Canal;
import com.gumeinteligencia.api_intermidiaria.domain.Cliente;
import com.gumeinteligencia.api_intermidiaria.domain.Regiao;
import com.gumeinteligencia.api_intermidiaria.domain.Segmento;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ClienteEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteMapperTest {

    private ClienteEntity clienteEntity;

    @BeforeEach
    void setUp() {
        clienteEntity = ClienteEntity.builder()
                .id(UUID.randomUUID())
                .nome("Nome teste d")
                .telefone("5500000000000")
                .regiao(Regiao.REGIAO_MARINGA)
                .segmento(Segmento.BOUTIQUE_LOJAS)
                .inativo(false)
                .canal(Canal.URA)
                .build();
    }

    @Test
    void deveRetornarDomainComSeucesso() {
        Cliente resultado = ClienteMapper.paraDomain(clienteEntity);

        Assertions.assertEquals(resultado.getId(), clienteEntity.getId());
        Assertions.assertEquals(resultado.getNome(), clienteEntity.getNome());
        Assertions.assertEquals(resultado.getTelefone(), clienteEntity.getTelefone());
        Assertions.assertEquals(resultado.getRegiao(), clienteEntity.getRegiao());
        Assertions.assertEquals(resultado.getSegmento(), clienteEntity.getSegmento());
        Assertions.assertFalse(resultado.isInativo());
        Assertions.assertEquals(resultado.getCanal(), clienteEntity.getCanal());
    }
}