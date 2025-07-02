package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Contexto {
    private UUID id;
    private String telefone;
    private List<String> mensagens;
    private StatusContexto status;
}
