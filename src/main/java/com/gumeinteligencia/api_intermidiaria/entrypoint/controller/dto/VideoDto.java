package com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class VideoDto {
    private String videoUrl;
}
