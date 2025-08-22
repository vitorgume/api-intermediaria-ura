package com.gumeinteligencia.api_intermidiaria.domain.outroContato;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OutroContato {
    private UUID id;
    private String nome;
    private String telefone;
    private String descricao;
    private Setor setor;
}
