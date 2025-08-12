package com.gumeinteligencia.api_intermidiaria.application.usecase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RoteadorDeTrafegoUseCase {

    @Value("${experimentos.chatbot.percentual}")
    private final int percentualChatbot;

    public RoteadorDeTrafegoUseCase(
            @Value("${experimentos.chatbot.percentual}") int percentualChatbot
    ) {
        this.percentualChatbot = Math.min(Math.max(percentualChatbot, 0), 100);;
    }

    public boolean deveUsarChatbot(String telefone) {
        if (telefone == null) telefone = "";

        int bucket = Math.floorMod(telefone.hashCode(), 100);
        return bucket < percentualChatbot;
    }
}
