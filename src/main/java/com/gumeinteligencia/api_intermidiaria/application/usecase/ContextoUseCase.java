package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.ContextoGateway;
import com.gumeinteligencia.api_intermidiaria.application.gateways.MensageriaGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.MensagemContexto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContextoUseCase {

    private final ContextoGateway gateway;
    private final MensageriaGateway mensageriaGateway;
    private final AvisoContextoUseCase avisoContextoUseCase;

    public Optional<Contexto> consultarPorTelefone(String telefone) {
        return gateway.consultarPorTelefone(telefone);
    }

    public void processarContextoExistente(Contexto contexto, Mensagem mensagem) {
        log.info("Processando contexto existente. Contexto: {}, Mensagem: {}", contexto, mensagem);

        contexto.getMensagens().add(
                MensagemContexto.builder()
                        .mensagem(mensagem.getMensagem())
                        .imagemUrl(mensagem.getUrlImagem())
                        .audioUrl(mensagem.getUrlAudio())
                        .build()
        );

        gateway.salvar(contexto);

        log.info("Contexto processado com sucesso.");
    }

    public void iniciarNovoContexto(Mensagem mensagem) {
        log.info("Iniciando novo contexto. Mensagem: {}", mensagem);

        Contexto novoContexto = Contexto.builder()
                .id(UUID.randomUUID())
                .mensagens(new ArrayList<>(List.of(
                        MensagemContexto.builder()
                                .mensagem(mensagem.getMensagem())
                                .imagemUrl(mensagem.getUrlImagem())
                                .audioUrl(mensagem.getUrlAudio())
                                .build()
                )))
                .telefone(mensagem.getTelefone())
                .build();

        novoContexto = gateway.salvar(novoContexto);

        log.info("Enviando contexto para a fila. Contexto: {}", novoContexto);

        var response = mensageriaGateway.enviarParaFila(avisoContextoUseCase.criarAviso(novoContexto.getId()));

        log.info("Contexto enviado com sucesso. Telefone: {}, Response: {}", novoContexto.getTelefone(), response);

        log.info("Contexto iniciado com sucesso.");
    }
}
