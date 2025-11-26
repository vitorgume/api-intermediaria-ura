package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.domain.AvisoContexto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AvisoContextoUseCase {

    public AvisoContexto criarAviso(UUID idContexto) {
        return AvisoContexto.builder()
                .id(UUID.randomUUID())
                .dataCriacao(LocalDateTime.now())
                .idContexto(idContexto)
                .build();
    }

}
