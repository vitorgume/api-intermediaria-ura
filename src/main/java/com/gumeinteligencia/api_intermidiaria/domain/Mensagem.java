package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Mensagem {
    private String telefone;
    private String mensagem;
    private String urlImagem;
    private String urlAudio;
    private String urlVideo;
}
