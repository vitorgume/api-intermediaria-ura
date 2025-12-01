package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MensagemContexto {
    private String mensagem;
    private String imagemUrl;
    private String audioUrl;
}
