package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidadorTelefoneValido implements MensagemValidator {

    @Override
    public boolean deveIgnorar(Mensagem mensagem) {
        return !PADRAO_FLEX.matcher(mensagem.getTelefone()).matches();
    }

    private static final Pattern PADRAO_FLEX = Pattern.compile(
            "^(?:\\+?55\\s?)?\\(?\\d{2}\\)?\\s?(?:9\\d{4}|\\d{4})-?\\d{4}$"
    );
}
