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

    @Test
    void devePreencherUrlsQuandoMidiasPresentes() {
        MensagemDto dtoComMidias = MensagemDto.builder()
                .phone("111")
                .text(TextoDto.builder().message("com midia").build())
                .audio(com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.AudioDto.builder().audioUrl("audio").build())
                .image(com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.ImageDto.builder().imageUrl("img").build())
                .video(com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.VideoDto.builder().videoUrl("video").build())
                .build();

        Mensagem resultado = MensagemMapper.paraDomain(dtoComMidias);

        assertEquals("audio", resultado.getUrlAudio());
        assertEquals("img", resultado.getUrlImagem());
        assertEquals("video", resultado.getUrlVideo());
        assertEquals("com midia", resultado.getMensagem());
    }

    @Test
    void deveRetornarStringsVaziasQuandoTextEAudiosForemNulos() {
        MensagemDto dtoSemMidias = MensagemDto.builder()
                .phone("222")
                .build(); // campos nulos devem virar string vazia

        Mensagem resultado = MensagemMapper.paraDomain(dtoSemMidias);

        assertEquals("", resultado.getMensagem());
        assertEquals("", resultado.getUrlAudio());
        assertEquals("", resultado.getUrlImagem());
        assertEquals("", resultado.getUrlVideo());
    }
}
