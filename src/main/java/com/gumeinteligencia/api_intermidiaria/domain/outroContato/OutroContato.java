package com.gumeinteligencia.api_intermidiaria.domain.outroContato;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OutroContato {
    private String id;
    private String nome;
    private String telefone;
    private String descricao;
    private Setor setor;
}
