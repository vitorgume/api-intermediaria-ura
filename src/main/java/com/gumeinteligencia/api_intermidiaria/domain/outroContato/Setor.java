package com.gumeinteligencia.api_intermidiaria.domain.outroContato;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Setor {
    FINANCEIRO(0, "Financeiro"),
    LOGISTICA(1, "Logistica"),
    GERENTE(2, "Gerente"),
    OUTRO(3, "Outro");

    private final int codigo;
    private final String descricao;
}
