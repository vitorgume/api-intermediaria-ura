package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadorMensagemUseCaseTest {

    @Mock
    private MensagemValidator validador1;

    @Mock
    private MensagemValidator validador2;

    private ValidadorMensagemUseCase useCase;

    private Mensagem mensagem;

    @BeforeEach
    void setUp() {
        useCase = new ValidadorMensagemUseCase(List.of(validador1, validador2));

        mensagem = Mensagem.builder()
                .telefone("44999999999")
                .mensagem("Olá")
                .build();
    }

    @Test
    void deveIgnorarQuandoUmValidadorRetornaTrue() {
        when(validador1.deveIgnorar(mensagem)).thenReturn(false);
        when(validador2.deveIgnorar(mensagem)).thenReturn(true);

        boolean resultado = useCase.deveIngorar(mensagem);

        assertTrue(resultado);
    }

    @Test
    void naoDeveIgnorarQuandoTodosRetornamFalse() {
        when(validador1.deveIgnorar(mensagem)).thenReturn(false);
        when(validador2.deveIgnorar(mensagem)).thenReturn(false);

        boolean resultado = useCase.deveIngorar(mensagem);

        assertFalse(resultado);
    }

}