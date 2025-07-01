package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidadorMensagemUseCase {

    private final List<MensagemValidator> validators;

    public boolean deveIngorar(Mensagem mensagem) {
        return validators.stream().anyMatch(validator -> validator.deveIgnorar(mensagem));
    }
}
