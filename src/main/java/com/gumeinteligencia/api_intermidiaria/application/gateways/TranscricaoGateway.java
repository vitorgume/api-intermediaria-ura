package com.gumeinteligencia.api_intermidiaria.application.gateways;

import com.gumeinteligencia.api_intermidiaria.domain.Transcricao;

public interface TranscricaoGateway {
    byte[] baixarAudio(String urlAudio);

    void enviarAudioTranscricao(byte[] bytes, String telefone, String s);

    Transcricao baixarTranscricao(String s3Key, String bucket);
}
