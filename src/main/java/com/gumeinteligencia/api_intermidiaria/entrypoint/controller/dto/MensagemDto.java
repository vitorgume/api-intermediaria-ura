package com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MensagemDto {
    private String phone;
    private TextoDto text;
    private ImageDto image;
    private AudioDto audio;
    private VideoDto video;
}
