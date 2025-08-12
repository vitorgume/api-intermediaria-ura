package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.UraGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.TextoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UraUseCase {

    private final UraGateway uraGateway;

    public void enviar(Mensagem mensagem) {
        log.info("Enviando para a URA. Mensagem: {}", mensagem);

        MensagemDto mensagemUra = MensagemDto.builder()
                .phone(mensagem.getTelefone())
                .text(new TextoDto(mensagem.getMensagem()))
                .build();

        uraGateway.enviarMensagem(mensagemUra);

        log.info("Enviado para a URA com sucesso.");
    }
}
