package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.ContextoGateway;
import com.gumeinteligencia.api_intermidiaria.application.gateways.MensageriaGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
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

    public Optional<Contexto> consultarPorTelefoneAtivo(String telefone) {
        Optional<Contexto> contexto = gateway.consultarPorTelefoneAtivo(telefone);
        return contexto;
    }

    public void processarContextoExistente(Contexto contexto, Mensagem mensagem) {
        log.info("Processando contexto existente. Contexto: {}, Mensagem: {}", contexto, mensagem);

        contexto.setStatus(StatusContexto.OBSOLETO);
        gateway.salvar(contexto);

        Contexto novoContexto = Contexto.builder()
                .id(UUID.randomUUID())
                .mensagens(new ArrayList<>(contexto.getMensagens()))
                .status(StatusContexto.ATIVO)
                .telefone(mensagem.getTelefone())
                .build();

        novoContexto.getMensagens().add(mensagem.getMensagem());

        novoContexto = gateway.salvar(novoContexto);

        log.info("Enviando contexto para a fila. Contexto: {}", contexto);

        var response = mensageriaGateway.enviarParaFila(novoContexto);

        log.info("Contexto enviado com sucesso. Telefone: {}, Response: {}", contexto.getTelefone(), response);

        log.info("Contexto processado com sucesso.");
    }

    public void iniciarNovoContexto(Mensagem mensagem) {
        log.info("Iniciando novo contexto. Mensagem: {}", mensagem);

        Contexto novoContexto = Contexto.builder()
                .id(UUID.randomUUID())
                .mensagens(new ArrayList<>(List.of(mensagem.getMensagem())))
                .status(StatusContexto.ATIVO)
                .telefone(mensagem.getTelefone())
                .build();

        novoContexto = gateway.salvar(novoContexto);

        log.info("Enviando contexto para a fila. Contexto: {}", novoContexto);

        var response = mensageriaGateway.enviarParaFila(novoContexto);

        log.info("Contexto enviado com sucesso. Telefone: {}, Response: {}", novoContexto.getTelefone(), response);

        log.info("Contexto iniciado com sucesso.");
    }
}
