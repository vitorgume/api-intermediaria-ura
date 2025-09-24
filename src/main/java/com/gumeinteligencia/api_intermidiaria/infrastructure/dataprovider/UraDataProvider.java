package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.application.gateways.UraGateway;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class UraDataProvider implements UraGateway {

    private final WebClient webClient;

    @Value("${ura.api.key}")
    private final String URA_API_KEY;

    public UraDataProvider(
            WebClient webClient,
            @Value("${ura.api.key}") String URA_API_KEY
    ) {
        this.webClient = webClient;
        this.URA_API_KEY = URA_API_KEY;
    }

    private final String MENSAGEM_ERRO_ENVIAR_MENSAGEM_URA = "Erro ao enviar mensagem para a URA.";

    @Override
    public void enviarMensagem(MensagemDto mensagem) {

        try {
            webClient
                    .post()
                    .uri("/mensagens")
                    .header("x-api-key", URA_API_KEY)
                    .bodyValue(mensagem)
                    .retrieve()
                    .bodyToMono(MensagemDto.class)
                    .retryWhen(
                            Retry.backoff(3, Duration.ofSeconds(2))
                                    .filter(throwable -> {
                                        log.warn("Tentando novamente após erro: {}", throwable.getMessage());
                                        return true;
                                    })
                    )
                    .doOnError(e -> log.error("{} | Erro: {}", "Erro ao enviar mensagem para URA", e.getMessage()))
                    .block();
        } catch (Exception ex) {
            log.error(MENSAGEM_ERRO_ENVIAR_MENSAGEM_URA, ex);
            throw new DataProviderException(MENSAGEM_ERRO_ENVIAR_MENSAGEM_URA, ex.getCause());
        }

    }
}
