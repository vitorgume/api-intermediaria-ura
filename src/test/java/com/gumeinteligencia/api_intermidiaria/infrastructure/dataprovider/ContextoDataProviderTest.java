package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.ContextoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextoDataProviderTest {

    @Mock
    private ContextoRepository repository;

    @InjectMocks
    private ContextoDataProvider dataProvider;

    private ContextoEntity contextoEntity;
    private Contexto contexto;

    @BeforeEach
    void setUp() {
        contextoEntity = ContextoEntity.builder()
                .id(UUID.randomUUID())
                .telefone("45999999999")
                .status(StatusContexto.ATIVO)
                .mensagens(List.of("Oi"))
                .build();

        contexto = Contexto.builder()
                .id(contextoEntity.getId())
                .telefone(contextoEntity.getTelefone())
                .status(contextoEntity.getStatus())
                .mensagens(contextoEntity.getMensagens())
                .build();
    }

    @Test
    void deveConsultarPorTelefoneComSucesso() {
        when(repository.buscarPorTelefone("45999999999")).thenReturn(Optional.of(contextoEntity));

        Optional<Contexto> resultado = dataProvider.consultarPorTelefone("45999999999");

        assertTrue(resultado.isPresent());
        assertEquals(contexto.getTelefone(), resultado.get().getTelefone());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarTelefone() {
        when(repository.buscarPorTelefone("000000000")).thenReturn(Optional.empty());

        Optional<Contexto> resultado = dataProvider.consultarPorTelefone("000000000");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveLancarExcecaoAoConsultarTelefoneComErro() {
        when(repository.buscarPorTelefone(anyString())).thenThrow(new RuntimeException("Erro simulado"));

        DataProviderException ex = assertThrows(DataProviderException.class, () ->
                dataProvider.consultarPorTelefone("erro"));

        assertEquals("Erro ao consultar contexto pelo seu telefone.", ex.getMessage());
    }

    @Test
    void deveSalvarComSucesso() {
        when(repository.salvar(any())).thenReturn(contextoEntity);

        Contexto salvo = dataProvider.salvar(contexto);

        assertNotNull(salvo);
        assertEquals(contexto.getTelefone(), salvo.getTelefone());
    }

    @Test
    void deveLancarExcecaoAoSalvarContexto() {
        when(repository.salvar(any())).thenThrow(new RuntimeException("Falha ao salvar"));

        DataProviderException ex = assertThrows(DataProviderException.class, () ->
                dataProvider.salvar(contexto));

        assertEquals("Erro ao salvar contexto.", ex.getMessage());
    }

}