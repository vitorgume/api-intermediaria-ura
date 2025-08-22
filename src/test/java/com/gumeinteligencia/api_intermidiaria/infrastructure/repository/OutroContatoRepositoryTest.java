package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.domain.outroContato.Setor;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.OutroContatoEntity;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutroContatoRepositoryTest {

    @Mock
    private DynamoDbTemplate dynamoDbTemplate;

    @InjectMocks
    private OutroContatoRepository outroContatoRepository;

    private OutroContatoEntity outroContato;

    @BeforeEach
    void setUp() {
        outroContato = OutroContatoEntity.builder()
                .id(UUID.randomUUID())
                .nome("Maria")
                .telefone("47999999999")
                .descricao("Contato para urgências")
                .setor(Setor.FINANCEIRO)
                .build();
    }

    @Test
    void deveSalvarOutroContatoComSucesso() {
        when(dynamoDbTemplate.save(outroContato)).thenReturn(outroContato);

        OutroContatoEntity salvo = outroContatoRepository.salvar(outroContato);

        verify(dynamoDbTemplate).save(outroContato);
        assertEquals(outroContato, salvo);
    }

    @Test
    void deveListarTodosOsContatos() {
        // Simula uma página com 1 item
        Page<OutroContatoEntity> page = Page.create(List.of(outroContato), null);
        PageIterable<OutroContatoEntity> iterable = PageIterable.create(() -> List.of(page).iterator());

        when(dynamoDbTemplate.scanAll(OutroContatoEntity.class)).thenReturn(iterable);

        List<OutroContatoEntity> resultado = outroContatoRepository.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Maria", resultado.get(0).getNome());
    }

    @Test
    void deveRetornarListaVaziaSeNaoHouverContatos() {
        // Simula uma página vazia
        Page<OutroContatoEntity> page = Page.create(List.of(), null);
        PageIterable<OutroContatoEntity> iterable = PageIterable.create(() -> List.of(page).iterator());

        when(dynamoDbTemplate.scanAll(OutroContatoEntity.class)).thenReturn(iterable);

        List<OutroContatoEntity> resultado = outroContatoRepository.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}