package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens.ValidadorMensagemUseCase;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessarMensagemUseCase {

    private final ValidadorMensagemUseCase validadorMensagem;
    private final ContextoUseCase contextoUseCase;

    public void processarNovaMensagem(Mensagem mensagem) {
        log.info("Processando nova mensagem. Mensagem: {}", mensagem);

        if(validadorMensagem.deveIngorar(mensagem)) {
            log.info("Mensagem ignorada. Motivo: Validação");
            return;
        }

        contextoUseCase
                .consultarPorTelefone(mensagem.getTelefone())
                .ifPresentOrElse(
                        contexto -> contextoUseCase.processarContextoExistente(contexto, mensagem),
                        () -> contextoUseCase.iniciarNovoContexto(mensagem)
                );

        log.info("Mensagem processada com sucesso.");
    }
}
