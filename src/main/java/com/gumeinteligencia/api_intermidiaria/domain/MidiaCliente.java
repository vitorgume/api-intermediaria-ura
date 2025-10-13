package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MidiaCliente {
    private UUID id;
    private String telefoneCliente;
    private List<String> urlMidias;

    public void adicionarUrl(String url) {
        urlMidias.add(url);
    }
}
