package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.ContextoGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContextoUseCase {

    private final ContextoGateway gateway;
    private final SqsUseCase sqsUseCase;

    public Optional<Contexto> consultarPorTelefone(String telefone) {
        return gateway.consultarPorTelefone(telefone);
    }

    public void processarContextoExistente(Contexto contexto, Mensagem mensagem) {
        contexto.setStatus(StatusContexto.OBSOLETO);
        gateway.salvar(contexto);

        Contexto novoContexto = Contexto.builder()
                .mensagens(new ArrayList<>(contexto.getMensagens()))
                .status(StatusContexto.ATIVO)
                .telefone(mensagem.getTelefone())
                .build();

        novoContexto.getMensagens().add(mensagem.getMensagem());

        novoContexto = gateway.salvar(novoContexto);

        sqsUseCase.enviarParaFila(novoContexto);
    }

    public void iniciarNovoContexto(Mensagem mensagem) {
        Contexto novoContexto = Contexto.builder()
                .mensagens(new ArrayList<>(List.of(mensagem.getMensagem())))
                .status(StatusContexto.ATIVO)
                .telefone(mensagem.getTelefone())
                .build();

        gateway.salvar(novoContexto);
    }
}
