package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.MensageriaGateway;
import com.gumeinteligencia.api_intermidiaria.domain.TranscricaoNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MensageriaUseCase {

    private MensageriaGateway mensageriaGateway;

    public List<TranscricaoNotificacao> listarTranscricoes() {
        return mensageriaGateway.listarTranscricoes();
    }
}
