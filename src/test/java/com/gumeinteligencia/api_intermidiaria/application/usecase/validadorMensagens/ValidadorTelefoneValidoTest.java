package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidadorTelefoneValidoTest {
    private final ValidadorTelefoneValido validador = new ValidadorTelefoneValido();

    @ParameterizedTest
    @DisplayName("deveIgnorar = true para telefones BR válidos no padrão flex")
    @ValueSource(strings = {
            "(11) 99876-5432",
            "(44) 3030-1234",
            "+55 (11) 91234-5678",
            "+55 11 91234-5678",
            "11912345678",
            "(11)91234-5678",
            "(44)3030-1234"
    })
    void deveIgnorar_quandoTelefoneValido(String telefone) {
        Mensagem msg = mock(Mensagem.class);
        when(msg.getTelefone()).thenReturn(telefone);

        assertFalse(validador.deveIgnorar(msg), "Deveria aceitar: " + telefone);
    }

    @ParameterizedTest
    @DisplayName("deveIgnorar = false para formatos inválidos")
    @ValueSource(strings = {
            "99876-5432",        // sem DDD
            "11-99876-5432",     // DDD com hífen no lugar errado
            "(011) 99876-5432",  // DDD com 3 dígitos
            "(11) 991234-567",   // dígitos a menos
            "(11) 991234-56789", // dígitos a mais
            "abc",               // não numérico
            "+5511-91234-5678",  // hífen após +55 (regex não permite aqui)
            "0055 (11) 91234-5678" // 00 55 (regex não cobre)
    })
    void naoDeveIgnorar_quandoTelefoneInvalido(String telefone) {
        Mensagem msg = mock(Mensagem.class);
        when(msg.getTelefone()).thenReturn(telefone);

        assertTrue(validador.deveIgnorar(msg), "Não deveria aceitar: " + telefone);
    }

    @Test
    @DisplayName("deveIgnorar = false para string vazia")
    void naoDeveIgnorar_quandoVazio() {
        Mensagem msg = mock(Mensagem.class);
        when(msg.getTelefone()).thenReturn("");

        assertTrue(validador.deveIgnorar(msg));
    }

    @Test
    @DisplayName("Lança NullPointerException quando telefone é null")
    void lancaExcecao_quandoNull() {
        Mensagem msg = mock(Mensagem.class);
        when(msg.getTelefone()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> validador.deveIgnorar(msg));
    }

}