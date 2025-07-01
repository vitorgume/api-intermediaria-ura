package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Mensagem {
    private String telefone;
    private String mensagem;
}
