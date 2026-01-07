package com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens;

import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidadorTelefoneValidoTest {
    private final ValidadorTelefoneValido validador = new ValidadorTelefoneValido();

    @ParameterizedTest
    @DisplayName("deveIgnorar = true para telefones validos com codigo do pais")
    @ValueSource(strings = {
            "+55 (11) 91234-5678",
            "+55 11 91234-5678",
            "+1 (781) 000-0000",
            "+44 20 7946-0958",
            "+81 3 1234-5678",
            "+49 (30) 1234-5678",
            "+358 40 123 4567",
            "+5511912345678"
    })
    void deveIgnorar_quandoTelefoneValido(String telefone) {
        Mensagem msg = mock(Mensagem.class);
        when(msg.getTelefone()).thenReturn(telefone);

        assertFalse(validador.deveIgnorar(msg), "Deveria aceitar: " + telefone);
    }

    @ParameterizedTest
    @DisplayName("deveIgnorar = false para formatos invalidos")
    @ValueSource(strings = {
            "99876-5432",                 // sem codigo do pais
            "1 (000) 000-0000",           // sem +
            "(011) 99876-5432",           // sem codigo do pais
            "+1 (000) 000-0000#",         // caractere invalido
            "+1 123-456",                 // digitos a menos
            "+123 1234567890123456",      // digitos a mais
            "+abc",                       // nao numerico
            "+55 (11) 9a234-5678",        // letras misturadas
            "++55 11 91234-5678"          // dois sinais de +
    })
    void naoDeveIgnorar_quandoTelefoneInvalido(String telefone) {
        Mensagem msg = mock(Mensagem.class);
        when(msg.getTelefone()).thenReturn(telefone);

        assertTrue(validador.deveIgnorar(msg), "Nao deveria aceitar: " + telefone);
    }

    @Test
    @DisplayName("deveIgnorar = false para string vazia")
    void naoDeveIgnorar_quandoVazio() {
        Mensagem msg = mock(Mensagem.class);
        when(msg.getTelefone()).thenReturn("");

        assertTrue(validador.deveIgnorar(msg));
    }

    @Test
    @DisplayName("Lanca NullPointerException quando telefone e null")
    void lancaExcecao_quandoNull() {
        Mensagem msg = mock(Mensagem.class);
        when(msg.getTelefone()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> validador.deveIgnorar(msg));
    }
}
