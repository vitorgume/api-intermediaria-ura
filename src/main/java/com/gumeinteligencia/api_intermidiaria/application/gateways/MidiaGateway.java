package com.gumeinteligencia.api_intermidiaria.application.gateways;

import com.gumeinteligencia.api_intermidiaria.domain.MidiaCliente;

public interface MidiaGateway {
    void salvar(MidiaCliente midiaCliente);

    byte[] baixarAudio(String urlAudio);

    void enviarAudioTranscricao(byte[] bytes, String telefone, String fileName);
}
