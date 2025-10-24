package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Contexto {
    private UUID id;
    private String telefone;
    private List<String> mensagens;
    private StatusContexto status;
}
