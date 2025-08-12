package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Regiao {
    MARINGA(1, "Maringá"),
    REGIAO_MARINGA(2, "Região de Maringá"),
    OUTRA(3, "Outras regiões"),
    NAO_INFORMADA(4, "Não informada");

    private final int codigo;
    private final String descricao;
}
