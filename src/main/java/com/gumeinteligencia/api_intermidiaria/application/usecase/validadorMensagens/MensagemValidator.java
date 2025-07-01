package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;

public interface MensagemValidator {
    boolean deveIgnorar(Mensagem mensagem);
}
