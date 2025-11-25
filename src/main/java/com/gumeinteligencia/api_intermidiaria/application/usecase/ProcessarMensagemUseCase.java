package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens.ValidadorMensagemUseCase;
import com.gumeinteligencia.api_intermidiaria.domain.Cliente;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessarMensagemUseCase {

    private final ValidadorMensagemUseCase validadorMensagem;
    private final ContextoUseCase contextoUseCase;
    private final UraUseCase uraUseCase;
    private final RoteadorDeTrafegoUseCase roteadorDeTrafegoUseCase;
    private final ClienteUseCase clienteUseCase;
    private final MidiaUseCase midiaUseCase;

    public void processarNovaMensagem(Mensagem mensagem) {
        log.info("Processando nova mensagem. Mensagem: {}", mensagem);

        if (validadorMensagem.deveIngorar(mensagem)) {
            log.info("Mensagem ignorada. Motivo: Validação");
            return;
        }

        Mensagem mensagemFinal = midiaUseCase.extrairMidias(mensagem);

        contextoUseCase.consultarPorTelefone(mensagem.getTelefone())
                .ifPresentOrElse(contexto -> {
                    contextoUseCase.processarContextoExistente(contexto, mensagemFinal);
                }, () -> {
                    Optional<Cliente> cliente = clienteUseCase.consultarPorTelefone(mensagemFinal.getTelefone());

                    if(cliente.isEmpty()) {
                        boolean usarChatbot = roteadorDeTrafegoUseCase.deveUsarChatbot(mensagemFinal.getTelefone());
                        if (usarChatbot) {
                            contextoUseCase.iniciarNovoContexto(mensagemFinal);
                        } else {
                            uraUseCase.enviar(mensagemFinal);
                        }
                    } else {
                        if (cliente.get().getCanal().getCodigo() == 0) {
                            contextoUseCase.iniciarNovoContexto(mensagemFinal);
                        } else {
                            uraUseCase.enviar(mensagemFinal);
                        }
                    }
                });

        log.info("Mensagem processada com sucesso.");
    }
}
