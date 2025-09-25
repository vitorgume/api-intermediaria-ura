package com.gumeinteligencia.api_intermidiaria.application.usecase;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RoteadorDeTrafegoUseCaseTest {
    @Test
    void construtorDeveTravarAbaixoDeZeroEmZero() {
        RoteadorDeTrafegoUseCase useCase = new RoteadorDeTrafegoUseCase(-10);
        // Com 0%, deve ser sempre false
        assertThat(useCase.deveUsarChatbot("qualquer")).isFalse();
        assertThat(useCase.deveUsarChatbot(null)).isFalse();
    }

    @Test
    void construtorDeveTravarAcimaDeCemEmCem() {
        RoteadorDeTrafegoUseCase useCase = new RoteadorDeTrafegoUseCase(200);
        // Com 100%, deve ser sempre true
        assertThat(useCase.deveUsarChatbot("qualquer")).isTrue();
        assertThat(useCase.deveUsarChatbot(null)).isTrue();
    }

    @Test
    void percentualZeroDeveSempreRetornarFalse() {
        RoteadorDeTrafegoUseCase useCase = new RoteadorDeTrafegoUseCase(0);
        assertThat(useCase.deveUsarChatbot("a")).isFalse();
        assertThat(useCase.deveUsarChatbot("b")).isFalse();
        assertThat(useCase.deveUsarChatbot("")).isFalse();
        assertThat(useCase.deveUsarChatbot(null)).isFalse();
    }

    @Test
    void percentualCemDeveSempreRetornarTrue() {
        RoteadorDeTrafegoUseCase useCase = new RoteadorDeTrafegoUseCase(100);
        assertThat(useCase.deveUsarChatbot("a")).isTrue();
        assertThat(useCase.deveUsarChatbot("b")).isTrue();
        assertThat(useCase.deveUsarChatbot("")).isTrue();
        assertThat(useCase.deveUsarChatbot(null)).isTrue();
    }

    @Test
    void nullEhTratadoComoStringVazia() {
        RoteadorDeTrafegoUseCase useCase = new RoteadorDeTrafegoUseCase(50);
        boolean resultadoNull = useCase.deveUsarChatbot(null);
        boolean resultadoVazio = useCase.deveUsarChatbot("");
        assertThat(resultadoNull).isEqualTo(resultadoVazio);
    }

    @Test
    void fronteiraDependeDoBucketCalculadoPeloTelefone() {
        String telefone = "44999999999";

        int bucket = Math.floorMod(telefone.hashCode(), 100);

        // percentual == bucket  → retorna false (pois a condição é bucket < percentual)
        RoteadorDeTrafegoUseCase percentualIgualAoBucket = new RoteadorDeTrafegoUseCase(bucket);
        assertThat(percentualIgualAoBucket.deveUsarChatbot(telefone)).isFalse();

        // percentual == bucket + 1 → retorna true
        RoteadorDeTrafegoUseCase percentualBucketMaisUm = new RoteadorDeTrafegoUseCase(bucket + 1);
        assertThat(percentualBucketMaisUm.deveUsarChatbot(telefone)).isTrue();
    }

    @Test
    void validaRegraEmDiversosTelefones() {
        int percentual = 37;
        RoteadorDeTrafegoUseCase useCase = new RoteadorDeTrafegoUseCase(percentual);

        String[] telefones = new String[] {
                "44999999999", "11987654321", "abc", "XYZ", "555",
                "", "_____","long number 1234567890", "with-symbols-!@#", "çáéíóú"
        };

        for (String tel : telefones) {
            String t = tel; // evita shadowing
            int bucket = Math.floorMod(t.hashCode(), 100);
            boolean esperado = bucket < percentual;
            assertThat(useCase.deveUsarChatbot(t))
                    .as("tel=%s bucket=%s esperado=%s", t, bucket, esperado)
                    .isEqualTo(esperado);
        }
    }
}