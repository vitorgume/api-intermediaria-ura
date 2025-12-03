package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MidiaUseCase {

    public Mensagem extrairMidias(Mensagem mensagem) {

        String urlVideo = mensagem.getUrlVideo();

        boolean possuiVideo = urlVideo != null && !urlVideo.isBlank();

        if (possuiVideo) {
            mensagem.setMensagem("Midia do usuario");
            return mensagem;
        }

        return mensagem;
    }
}
