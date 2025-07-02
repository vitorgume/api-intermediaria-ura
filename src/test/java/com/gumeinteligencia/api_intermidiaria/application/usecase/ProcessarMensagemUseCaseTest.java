package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens.ValidadorMensagemUseCase;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessarMensagemUseCaseTest {

    @Mock
    private ValidadorMensagemUseCase validadorMensagem;

    @Mock
    private ContextoUseCase contextoUseCase;

    @InjectMocks
    private ProcessarMensagemUseCase processarMensagemUseCase;

    private Mensagem mensagem;

    @BeforeEach
    void setUp() {
        mensagem = Mensagem.builder()
                .telefone("44999999999")
                .mensagem("Olá")
                .build();
    }

    @Test
    void deveIgnorarMensagemSeValidadorRetornarTrue() {
        when(validadorMensagem.deveIngorar(mensagem)).thenReturn(true);

        processarMensagemUseCase.processarNovaMensagem(mensagem);

        verify(contextoUseCase, never()).consultarPorTelefone(any());
        verify(contextoUseCase, never()).processarContextoExistente(any(), any());
        verify(contextoUseCase, never()).iniciarNovoContexto(any());
    }

    @Test
    void deveProcessarMensagemComContextoExistente() {
        Contexto contexto = Contexto.builder().telefone("44999999999").build();

        when(validadorMensagem.deveIngorar(mensagem)).thenReturn(false);
        when(contextoUseCase.consultarPorTelefone(mensagem.getTelefone())).thenReturn(Optional.of(contexto));

        processarMensagemUseCase.processarNovaMensagem(mensagem);

        verify(contextoUseCase).processarContextoExistente(contexto, mensagem);
        verify(contextoUseCase, never()).iniciarNovoContexto(any());
    }

    @Test
    void deveProcessarMensagemSemContextoExistente() {
        when(validadorMensagem.deveIngorar(mensagem)).thenReturn(false);
        when(contextoUseCase.consultarPorTelefone(mensagem.getTelefone())).thenReturn(Optional.empty());

        processarMensagemUseCase.processarNovaMensagem(mensagem);

        verify(contextoUseCase).iniciarNovoContexto(mensagem);
        verify(contextoUseCase, never()).processarContextoExistente(any(), any());
    }
}