package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.ClienteGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Canal;
import com.gumeinteligencia.api_intermidiaria.domain.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteUseCaseTest {

    @Mock
    private ClienteGateway clienteGateway;

    @InjectMocks
    private ClienteUseCase clienteUseCase;

    @Test
    void deveConsultarClientePorTelefone() {
        var cliente = Cliente.builder()
                .id(UUID.randomUUID())
                .telefone("44999999999")
                .canal(Canal.CHATBOT)
                .build();

        when(clienteGateway.consultarPorTelefone(cliente.getTelefone())).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteUseCase.consultarPorTelefone(cliente.getTelefone());

        assertTrue(resultado.isPresent());
        assertEquals(cliente.getTelefone(), resultado.get().getTelefone());
        assertEquals(cliente.getCanal(), resultado.get().getCanal());
    }
}
