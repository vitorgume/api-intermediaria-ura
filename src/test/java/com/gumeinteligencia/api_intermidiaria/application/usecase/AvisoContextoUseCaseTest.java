package com.gumeinteligencia.api_intermidiaria.application.usecase;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AvisoContextoUseCaseTest {

    private final AvisoContextoUseCase useCase = new AvisoContextoUseCase();

    @Test
    void deveCriarAvisoComDadosBasicos() {
        UUID idContexto = UUID.randomUUID();

        var aviso = useCase.criarAviso(idContexto);

        assertNotNull(aviso.getId());
        assertNotNull(aviso.getDataCriacao());
        assertEquals(idContexto, aviso.getIdContexto());
    }
}
