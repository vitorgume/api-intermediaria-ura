package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens.ValidadorMensagemUseCase;
import com.gumeinteligencia.api_intermidiaria.domain.Canal;
import com.gumeinteligencia.api_intermidiaria.domain.Cliente;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessarMensagemUseCaseTest {

    @Mock
    private ValidadorMensagemUseCase validadorMensagem;

    @Mock
    private ContextoUseCase contextoUseCase;

    @Mock
    private ClienteUseCase clienteUseCase;

    @InjectMocks
    private ProcessarMensagemUseCase processarMensagemUseCase;

    private Mensagem mensagem;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        mensagem = Mensagem.builder()
                .telefone("44999999999")
                .mensagem("Olá")
                .build();

        cliente = Cliente.builder()
                .id(UUID.randomUUID())
                .canal(Canal.URA)
                .build();
    }

    @Test
    void deveIgnorarMensagemSeValidadorRetornarTrue() {
        when(validadorMensagem.deveIngorar(mensagem)).thenReturn(true);

        processarMensagemUseCase.processarNovaMensagem(mensagem);

        verify(contextoUseCase, never()).consultarPorTelefoneAtivo(any());
        verify(contextoUseCase, never()).processarContextoExistente(any(), any());
        verify(contextoUseCase, never()).iniciarNovoContexto(any());
    }

    @Test
    void deveProcessarMensagemComContextoExistente() {
        Contexto contexto = Contexto.builder().telefone("44999999999").build();

        when(validadorMensagem.deveIngorar(mensagem)).thenReturn(false);
        when(contextoUseCase.consultarPorTelefoneAtivo(mensagem.getTelefone())).thenReturn(Optional.of(contexto));

        processarMensagemUseCase.processarNovaMensagem(mensagem);

        verify(contextoUseCase).processarContextoExistente(contexto, mensagem);
        verify(contextoUseCase, never()).iniciarNovoContexto(any());
    }

    @Test
    void deveProcessarMensagemSemContextoExistente() {
        when(validadorMensagem.deveIngorar(mensagem)).thenReturn(false);
        when(contextoUseCase.consultarPorTelefoneAtivo(mensagem.getTelefone())).thenReturn(Optional.empty());
        when(clienteUseCase.consultarPorTelefone(Mockito.anyString())).thenReturn(Optional.of(cliente));

        processarMensagemUseCase.processarNovaMensagem(mensagem);

        verify(contextoUseCase).iniciarNovoContexto(mensagem);
        verify(contextoUseCase, never()).processarContextoExistente(any(), any());
    }
}