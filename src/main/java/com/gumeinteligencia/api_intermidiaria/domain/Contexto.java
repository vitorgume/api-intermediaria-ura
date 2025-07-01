package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Contexto {
    private String id;
    private String telefone;
    private List<String> mensagens;
    private StatusContexto status;
}
