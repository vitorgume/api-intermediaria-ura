package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.domain.outroContato.Setor;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.OutroContatoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutroContatoRepositoryTest {

    @Mock
    private DynamoDbEnhancedClient enhancedClient;

    @Mock
    private DynamoDbTable<OutroContatoEntity> outroContatoTable;

    @InjectMocks
    private OutroContatoRepository outroContatoRepository;

    private OutroContatoEntity outroContato;

    @BeforeEach
    void setUp() {
        outroContato = OutroContatoEntity.builder()
                .id(1L)
                .nome("Maria")
                .telefone("47999999999")
                .descricao("Contato para urgências")
                .setor(Setor.FINANCEIRO)
                .build();

        when(enhancedClient.table(eq("outros_contatos"), ArgumentMatchers.<TableSchema<OutroContatoEntity>>any()))
                .thenReturn(outroContatoTable);
    }

    @Test
    void deveSalvarOutroContatoComSucesso() {
        outroContatoRepository.salvar(outroContato);

        verify(outroContatoTable).putItem(outroContato);
    }

    @Test
    void deveListarTodosOsContatos() {
        Page<OutroContatoEntity> page = Page.create(List.of(outroContato), null);
        PageIterable<OutroContatoEntity> iterable = PageIterable.create(() -> List.of(page).iterator());

        when(outroContatoTable.scan()).thenReturn(iterable); // CORREÇÃO AQUI

        List<OutroContatoEntity> resultado = outroContatoRepository.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Maria", resultado.get(0).getNome());
    }

    @Test
    void deveRetornarListaVaziaSeNaoHouverContatos() {
        Page<OutroContatoEntity> page = Page.create(List.of(), null);
        PageIterable<OutroContatoEntity> iterable = PageIterable.create(() -> List.of(page).iterator());

        when(outroContatoTable.scan()).thenReturn(iterable); // CORRIGIDO

        List<OutroContatoEntity> resultado = outroContatoRepository.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}