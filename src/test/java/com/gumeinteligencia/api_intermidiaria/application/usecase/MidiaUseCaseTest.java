package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MidiaUseCaseTest {

    private final MidiaUseCase midiaUseCase = new MidiaUseCase();

    @Test
    void deveRetornarMensagemQuandoNaoHaMidias() {
        Mensagem mensagem = Mensagem.builder()
                .telefone("111")
                .mensagem("texto")
                .urlAudio("")
                .urlImagem("")
                .urlVideo("")
                .build();

        Mensagem resultado = midiaUseCase.extrairMidias(mensagem);

        assertEquals("texto", resultado.getMensagem());
    }

    @Test
    void deveSubstituirMensagemQuandoTiverAlgumaMidia() {
        Mensagem mensagem = Mensagem.builder()
                .telefone("111")
                .mensagem("texto")
                .urlAudio("")
                .urlImagem("")
                .urlVideo("video-url")
                .build();

        Mensagem resultado = midiaUseCase.extrairMidias(mensagem);

        // o método deve sobrescrever o texto quando houver midia, pouco importa o acento definido na constante
        assertNotEquals("texto", resultado.getMensagem());
        assertTrue(resultado.getMensagem().startsWith("Midia do"));
    }
}
