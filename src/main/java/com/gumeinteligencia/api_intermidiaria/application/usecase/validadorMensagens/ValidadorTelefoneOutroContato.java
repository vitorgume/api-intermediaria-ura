package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.application.usecase.OutroContatoUseCase;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidadorTelefoneOutroContato implements MensagemValidator {

    private final OutroContatoUseCase outroContatoUseCase;

    @Override
    public boolean deveIgnorar(Mensagem mensagem) {
        return outroContatoUseCase.listar().stream().map(OutroContato::getTelefone).toList().contains(mensagem.getTelefone());
    }
}
