package com.gumeinteligencia.api_intermidiaria.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${ura.url}")
    private final String urlBase;

    public WebClientConfig(
            @Value("${ura.url}") String urlBase
    ) {
        this.urlBase = urlBase;
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(urlBase)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(
                                Duration.ofSeconds(30))))
                .build();
    }
}
