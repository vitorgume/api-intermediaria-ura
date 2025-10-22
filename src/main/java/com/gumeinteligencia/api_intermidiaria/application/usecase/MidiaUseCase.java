package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.MidiaGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.MidiaCliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MidiaUseCase {

    private final MidiaGateway gateway;

    public Mensagem extrairMidias(Mensagem mensagem) {

        MidiaCliente midiaCliente = MidiaCliente.builder().telefoneCliente(mensagem.getTelefone()).urlMidias(new ArrayList<>()).build();

        if(!mensagem.getUrlAudio().isBlank()) {
            this.transcreverAudio(mensagem.getUrlAudio(), mensagem.getTelefone());
        }

        if (!mensagem.getUrlImagem().isBlank()) {
            midiaCliente.adicionarUrl(mensagem.getUrlImagem());
            gateway.salvar(midiaCliente);
            mensagem.setMensagem("Midia do usuário");
            return mensagem;
        }

        if(!mensagem.getUrlVideo().isBlank()) {
            midiaCliente.adicionarUrl(mensagem.getUrlVideo());
            gateway.salvar(midiaCliente);
            mensagem.setMensagem("Midia do usuário");
            return mensagem;
        }

        return mensagem;
    }

    private void transcreverAudio(String urlAudio, String telefone) {
        byte[] bytes = gateway.baixarAudio(urlAudio);
        gateway.enviarAudioTranscricao(bytes, telefone, "audio-chat");
    }
    
}
