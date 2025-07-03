package com.gumeinteligencia.api_intermidiaria.entrypoint.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.TextoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MensagemMapperTest {

    private MensagemDto mensagemDto;

    @BeforeEach
    void setUp() {
        mensagemDto = MensagemDto.builder()
                .phone("000000000000")
                .text(TextoDto.builder()
                        .message("Teste mensagem")
                        .build()
                ).build();
    }

    @Test
    void paraDomain() {
        Mensagem resultado = MensagemMapper.paraDomain(mensagemDto);

        Assertions.assertEquals(resultado.getMensagem(), mensagemDto.getText().getMessage());
        Assertions.assertEquals(resultado.getTelefone(), mensagemDto.getPhone());
    }
}