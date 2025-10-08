package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AudioUseCase {

    public Mensagem trasformar(Mensagem mensagem) {
        if(mensagem.getUrlAudio().isBlank()) {
            return mensagem;
        }


    }
}
