package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidadorTelefoneValido implements MensagemValidator {

    @Override
    public boolean deveIgnorar(Mensagem mensagem) {
        String telefone = mensagem.getTelefone();
        if (telefone.startsWith("+")) {
            if (!PADRAO_FORMATACAO.matcher(telefone).matches()) {
                return true;
            }
        } else if (!PADRAO_APENAS_DIGITOS.matcher(telefone).matches()) {
            return true;
        }

        String digitos = telefone.replaceAll("\\D", "");
        return digitos.length() < TAMANHO_MIN_DIGITOS || digitos.length() > TAMANHO_MAX_DIGITOS;
    }

    private static final int TAMANHO_MIN_DIGITOS = 8;
    private static final int TAMANHO_MAX_DIGITOS = 15;
    private static final Pattern PADRAO_FORMATACAO = Pattern.compile("^\\+[0-9()\\s-]+$");
    private static final Pattern PADRAO_APENAS_DIGITOS = Pattern.compile("^\\d+$");
}
