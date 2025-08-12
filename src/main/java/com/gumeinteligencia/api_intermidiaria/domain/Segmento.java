package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Segmento {
    MEDICINA_SAUDE(1, "Medicina e Saúde"),
    BOUTIQUE_LOJAS(2, "Boutique e Lojas"),
    ENGENHARIA_ARQUITETURA(3, "Engenharia e Arquitetura"),
    ALIMENTOS(4, "Alimentos"),
    CELULARES(5, "Celulares"),
    OUTROS(6, "Outros"),
    NAO_INFORMADO(7, "Não informado");

    private final int codigo;
    private final String descricao;
}
