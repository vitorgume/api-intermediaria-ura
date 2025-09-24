package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.OutroContatoGateway;
import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;
import com.gumeinteligencia.api_intermidiaria.domain.outroContato.Setor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutroContatoUseCaseTest {

    @Mock
    private OutroContatoGateway gateway;

    @InjectMocks
    private OutroContatoUseCase outroContatoUseCase;

    private List<OutroContato> outroContatoList;

    @BeforeEach
    void setUp() {
        outroContatoList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            outroContatoList.add(OutroContato.builder()
                    .id(UUID.randomUUID())
                    .nome("Nome teste")
                    .setor(Setor.LOGISTICA)
                    .descricao("Descricao teste")
                    .telefone("000000000000")
                    .build()
            );
        }
    }

    @Test
    void listar() {
        when(gateway.listar()).thenReturn(outroContatoList);

        List<OutroContato> resultado = outroContatoUseCase.listar();

        for (int i = 0; i < resultado.size(); i++) {
            assertEquals(resultado.get(i).getTelefone(), outroContatoList.get(i).getTelefone());
        }
    }
}