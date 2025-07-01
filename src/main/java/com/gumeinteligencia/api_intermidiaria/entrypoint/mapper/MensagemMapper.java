package com.gumeinteligencia.api_intermidiaria.entrypoint.mapper;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;

public class MensagemMapper {

    public static Mensagem paraDomain(MensagemDto dto) {
        return Mensagem.builder()
                .telefone(dto.getPhone())
                .mensagem(dto.getText().getMessage())
                .build();
    }
}
