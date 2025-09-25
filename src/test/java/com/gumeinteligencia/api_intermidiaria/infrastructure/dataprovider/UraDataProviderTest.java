package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.TextoDto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UraDataProviderTest {

    private static final String API_KEY = "secret-key";

    @Mock private WebClient webClient;
    @Mock private WebClient.RequestBodyUriSpec postSpec;
    @Mock private WebClient.RequestHeadersSpec<?> headersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private UraDataProvider dataProvider;

    @BeforeEach
    void init() {
        dataProvider = new UraDataProvider(webClient, API_KEY);
    }

    @Test
    void devePostarComApiKeyEBodyCorretos_quandoSucesso() {
        MensagemDto entrada = MensagemDto.builder()
                .phone("44999999999")
                .text(new TextoDto("Olá"))
                .build();

        when(webClient.post()).thenReturn(postSpec);
        when(postSpec.uri("/mensagens")).thenReturn(postSpec);
        when(postSpec.header("x-api-key", API_KEY)).thenReturn(postSpec);

        doReturn(headersSpec).when(postSpec).bodyValue(entrada);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(MensagemDto.class)).thenReturn(Mono.just(entrada));

        dataProvider.enviarMensagem(entrada);

        InOrder in = inOrder(webClient, postSpec, headersSpec, responseSpec);
        in.verify(webClient).post();
        in.verify(postSpec).uri("/mensagens");
        in.verify(postSpec).header("x-api-key", API_KEY);
        in.verify(postSpec).bodyValue(entrada);
        in.verify(headersSpec).retrieve();
        in.verify(responseSpec).bodyToMono(MensagemDto.class);

        verifyNoMoreInteractions(webClient, postSpec, headersSpec, responseSpec);
    }

    @Test
    void deveLancarDataProviderException_quandoFalharAntesDoBodyToMono() {
        MensagemDto entrada = MensagemDto.builder()
                .phone("44999999999")
                .text(new TextoDto("Falha"))
                .build();

        when(webClient.post()).thenReturn(postSpec);
        when(postSpec.uri("/mensagens")).thenReturn(postSpec);
        when(postSpec.header("x-api-key", API_KEY)).thenReturn(postSpec);
        doReturn(headersSpec).when(postSpec).bodyValue(entrada);
        when(headersSpec.retrieve()).thenThrow(new IllegalStateException("erro simulado"));

        assertThatThrownBy(() -> dataProvider.enviarMensagem(entrada))
                .isInstanceOf(DataProviderException.class)
                .hasMessage("Erro ao enviar mensagem para a URA.");

        verify(webClient).post();
        verify(postSpec).uri("/mensagens");
        verify(postSpec).header("x-api-key", API_KEY);
        verify(postSpec).bodyValue(entrada);
        verify(headersSpec).retrieve();
        verifyNoMoreInteractions(webClient, postSpec, headersSpec);
    }

    @Test
    void deveTentar3VezesEConcluirNa4a_quandoFalharEAposRetentarSucesso() {
        MensagemDto entrada = MensagemDto.builder()
                .phone("44999999999")
                .text(new TextoDto("Teste retry"))
                .build();

        when(webClient.post()).thenReturn(postSpec);
        when(postSpec.uri("/mensagens")).thenReturn(postSpec);
        when(postSpec.header("x-api-key", API_KEY)).thenReturn(postSpec);
        doReturn(headersSpec).when(postSpec).bodyValue(entrada);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        AtomicInteger subs = new AtomicInteger(0);

        when(responseSpec.bodyToMono(MensagemDto.class))
                .thenReturn(Mono.defer(() -> {
                    int n = subs.incrementAndGet();
                    if (n < 4) {
                        return Mono.error(new IllegalStateException("erro " + n));
                    }
                    return Mono.just(entrada);
                }));

        try (MockedStatic<Retry> mockedRetry = Mockito.mockStatic(Retry.class, Answers.CALLS_REAL_METHODS)) {
            mockedRetry.when(() -> Retry.backoff(eq(3), any()))
                    .thenAnswer(inv -> Retry.fixedDelay(3, Duration.ZERO));

            dataProvider.enviarMensagem(entrada);
        }

        assertThat(subs.get()).isEqualTo(4);

        InOrder in = inOrder(webClient, postSpec, headersSpec, responseSpec);
        in.verify(webClient).post();
        in.verify(postSpec).uri("/mensagens");
        in.verify(postSpec).header("x-api-key", API_KEY);
        in.verify(postSpec).bodyValue(entrada);
        in.verify(headersSpec).retrieve();
        in.verify(responseSpec).bodyToMono(MensagemDto.class);
        verifyNoMoreInteractions(webClient, postSpec, headersSpec, responseSpec);
    }
}