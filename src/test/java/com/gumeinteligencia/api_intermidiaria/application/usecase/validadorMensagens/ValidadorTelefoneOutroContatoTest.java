package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.application.usecase.OutroContatoUseCase;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadorTelefoneOutroContatoTest {

    @Mock
    private OutroContatoUseCase outroContatoUseCase;

    private ValidadorTelefoneOutroContato validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorTelefoneOutroContato(outroContatoUseCase);
    }

    @Test
    void deveIgnorarQuandoTelefoneExisteEmOutroContato() {
        OutroContato contato = OutroContato.builder().telefone("44999999999").build();
        when(outroContatoUseCase.listar()).thenReturn(List.of(contato));

        Mensagem mensagem = Mensagem.builder().telefone("44999999999").build();

        assertTrue(validador.deveIgnorar(mensagem));
    }

    @Test
    void naoDeveIgnorarQuandoTelefoneNaoExiste() {
        when(outroContatoUseCase.listar()).thenReturn(List.of());

        Mensagem mensagem = Mensagem.builder().telefone("44999999999").build();

        assertFalse(validador.deveIgnorar(mensagem));
    }

}