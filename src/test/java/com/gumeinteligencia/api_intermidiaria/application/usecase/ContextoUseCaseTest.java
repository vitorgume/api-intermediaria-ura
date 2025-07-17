package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.ContextoGateway;
import com.gumeinteligencia.api_intermidiaria.application.gateways.MensageriaGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextoUseCaseTest {

    @Mock
    private ContextoGateway gateway;

    @Mock
    private MensageriaGateway mensageriaGateway;

    @InjectMocks
    private ContextoUseCase contextoUseCase;

    private final String telefone = "44999999999";
    private Mensagem mensagem;

    @BeforeEach
    void setUp() {
        mensagem = Mensagem.builder()
                .telefone(telefone)
                .mensagem("Oi, tudo bem?")
                .build();
    }

    @Test
    void deveConsultarContextoPorTelefone() {
        Contexto contexto = Contexto.builder().telefone(telefone).status(StatusContexto.ATIVO).build();
        when(gateway.consultarPorTelefoneAtivo(telefone)).thenReturn(Optional.of(contexto));

        Optional<Contexto> resultado = contextoUseCase.consultarPorTelefoneAtivo(telefone);

        assertTrue(resultado.isPresent());
        assertEquals(telefone, resultado.get().getTelefone());
        assertEquals(StatusContexto.ATIVO, resultado.get().getStatus());
    }

    @Test
    void deveProcessarContextoExistente() {
        Contexto contexto = Contexto.builder()
                .id(UUID.randomUUID())
                .telefone(telefone)
                .status(StatusContexto.ATIVO)
                .mensagens(new ArrayList<>(List.of("Mensagem antiga")))
                .build();

        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mensageriaGateway.enviarParaFila(any())).thenReturn(null);

        contextoUseCase.processarContextoExistente(contexto, mensagem);

        verify(gateway, Mockito.times(2)).salvar(any(Contexto.class));
        verify(mensageriaGateway).enviarParaFila(any(Contexto.class));
    }

    @Test
    void deveIniciarNovoContexto() {
        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mensageriaGateway.enviarParaFila(any())).thenReturn(null);

        contextoUseCase.iniciarNovoContexto(mensagem);

        ArgumentCaptor<Contexto> captor = ArgumentCaptor.forClass(Contexto.class);
        verify(gateway).salvar(captor.capture());
        verify(mensageriaGateway).enviarParaFila(captor.getValue());

        Contexto contextoCriado = captor.getValue();
        assertEquals(telefone, contextoCriado.getTelefone());
        assertEquals(StatusContexto.ATIVO, contextoCriado.getStatus());
        assertEquals(List.of(mensagem.getMensagem()), contextoCriado.getMensagens());
    }

}