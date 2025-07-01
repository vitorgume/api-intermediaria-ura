package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.springframework.stereotype.Service;

@Service
public class ValidadorMensagemVazia implements MensagemValidator {
    @Override
    public boolean deveIgnorar(Mensagem mensagem) {
        return mensagem.getMensagem() == null || mensagem.getMensagem().isBlank();
    }
}
