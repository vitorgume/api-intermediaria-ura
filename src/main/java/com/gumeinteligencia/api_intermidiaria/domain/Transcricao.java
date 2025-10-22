package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transcricao {
    private String bucket;
    private String key;
    private String body;
}
