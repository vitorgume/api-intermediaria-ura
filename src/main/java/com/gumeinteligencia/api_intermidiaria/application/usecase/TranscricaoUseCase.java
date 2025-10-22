package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.TranscricaoGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.Transcricao;
import com.gumeinteligencia.api_intermidiaria.domain.TranscricaoNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranscricaoUseCase {

    private final TranscricaoGateway gateway;
    private final MensageriaUseCase mensageriaUseCase;
    private final ProcessarMensagemUseCase processarMensagemUseCase;

    @Scheduled(fixedDelay = 5000)
    public void consumirTranscricao() {
        List<TranscricaoNotificacao> transcricaoNotificacaos = mensageriaUseCase.listarTranscricoes();
        transcricaoNotificacaos.forEach(this::processarTranscricao);
    }

    private void processarTranscricao(TranscricaoNotificacao transcricaoNotificacao) {
        Transcricao transcricao = gateway.baixarTranscricao(transcricaoNotificacao.getS3Key(), transcricaoNotificacao.getS3Bucket());

        Mensagem novaMensagem = Mensagem.builder()
                .telefone(transcricaoNotificacao.getTelefone())
                .mensagem(transcricao.getBody())
                .build();

        processarMensagemUseCase.processarNovaMensagem(novaMensagem);
    }
}
