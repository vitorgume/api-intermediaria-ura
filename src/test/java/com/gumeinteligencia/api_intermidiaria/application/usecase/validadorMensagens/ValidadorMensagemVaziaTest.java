package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorMensagemVaziaTest {

    private ValidadorMensagemVazia validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorMensagemVazia();
    }

    @Test
    void deveIgnorarMensagemNula() {
        Mensagem mensagem = Mensagem.builder().mensagem(null).build();
        assertTrue(validador.deveIgnorar(mensagem));
    }

    @Test
    void deveIgnorarMensagemVazia() {
        Mensagem mensagem = Mensagem.builder().mensagem("   ").build();
        assertTrue(validador.deveIgnorar(mensagem));
    }

    @Test
    void naoDeveIgnorarMensagemValida() {
        Mensagem mensagem = Mensagem.builder().mensagem("Oi!").build();
        assertFalse(validador.deveIgnorar(mensagem));
    }

}