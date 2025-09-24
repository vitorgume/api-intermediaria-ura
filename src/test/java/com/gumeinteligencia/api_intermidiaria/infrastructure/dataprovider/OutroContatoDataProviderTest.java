package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.domain.outroContato.OutroContato;
import com.gumeinteligencia.api_intermidiaria.domain.outroContato.Setor;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.OutroContatoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.OutroContatoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OutroContatoDataProviderTest {

    @Mock
    private OutroContatoRepository repository;

    @InjectMocks
    private OutroContatoDataProvider dataProvider;

    private List<OutroContatoEntity> outroContatos;

    @BeforeEach
    void setUp() {
        outroContatos = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            outroContatos.add(OutroContatoEntity.builder()
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
    void deveListarComSucesso() {
        when(repository.listar()).thenReturn(outroContatos);

        List<OutroContato> resultado = dataProvider.listar();

        for (int i = 0; i < resultado.size(); i++) {
            assertEquals(resultado.get(i).getTelefone(), outroContatos.get(i).getTelefone());
        }
    }

    @Test
    void deveLancarExceptionAoListar() {
        when(repository.listar()).thenThrow(new RuntimeException("Erro simulado"));

        DataProviderException ex = assertThrows(DataProviderException.class, () -> dataProvider.listar());

        assertEquals("Erro ao listar outros contatos.", ex.getMessage());
    }
}