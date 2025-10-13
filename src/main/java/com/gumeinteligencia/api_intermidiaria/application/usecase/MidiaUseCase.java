package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.MidiaGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.MidiaCliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MidiaUseCase {

    private final MidiaGateway gateway;

    public void extrairMidias(Mensagem mensagem) {

        MidiaCliente midiaCliente = MidiaCliente.builder().telefoneCliente(mensagem.getTelefone()).urlMidias(new ArrayList<>()).build();

        if(!mensagem.getUrlAudio().isBlank()) {
            midiaCliente.adicionarUrl(mensagem.getUrlAudio());
        }

        if (!mensagem.getUrlImagem().isBlank()) {
            midiaCliente.adicionarUrl(mensagem.getUrlImagem());
        }

        if(!mensagem.getUrlVideo().isBlank()) {
            midiaCliente.adicionarUrl(mensagem.getUrlVideo());
        }

        gateway.salvar(midiaCliente);
    }
}
