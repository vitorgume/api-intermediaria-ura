package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextoRepositoryTest {

    @Mock
    private DynamoDbEnhancedClient enhancedClient;

    @Mock
    private DynamoDbTable<ContextoEntity> contextoTable;

    @InjectMocks
    private ContextoRepository contextoRepository;

    private final UUID id = UUID.fromString("d1aea9b5-4007-459f-8629-b3ea7a22ca6b");
    private final String telefone = "45999999999";

    private ContextoEntity contexto;

    private Key key;

    @BeforeEach
    void setUp() {
        contexto = ContextoEntity.builder()
                .id(id)
                .telefone(telefone)
                .status(StatusContexto.ATIVO)
                .mensagens(List.of("Olá"))
                .build();

        key = Key.builder().partitionValue(contexto.getId().toString()).build();

        when(enhancedClient.table(eq("contextos"), ArgumentMatchers.<TableSchema<ContextoEntity>>any()))
                .thenReturn(contextoTable);
    }

    @Test
    void deveSalvarContextoComSucesso() {

        when(contextoTable.getItem(eq(key))).thenReturn(contexto);

        ContextoEntity salvo = contextoRepository.salvar(contexto);

        verify(contextoTable).putItem(contexto);
        verify(contextoTable).getItem(eq(key));
        assertEquals(contexto.getId(), salvo.getId());
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(contextoTable.getItem(eq(key))).thenReturn(contexto);

        Optional<ContextoEntity> encontrado = contextoRepository.buscarPorId(id.toString());

        verify(contextoTable).getItem(eq(key));
        assertTrue(encontrado.isPresent());
        assertEquals(contexto.getTelefone(), encontrado.get().getTelefone());
    }

    @Test
    void deveRetornarVazioAoBuscarPorIdInexistente() {
        when(contextoTable.getItem(any(Key.class))).thenReturn(null);

        Optional<ContextoEntity> encontrado = contextoRepository.buscarPorId("inexistente");

        assertTrue(encontrado.isEmpty());
    }

    @Test
    void deveBuscarPorTelefoneComSucesso() {
        Page<ContextoEntity> page = Page.create(List.of(contexto), null);
        PageIterable<ContextoEntity> iterable = PageIterable.create(() -> List.of(page).iterator());

        when(contextoTable.scan(ArgumentMatchers.<ScanEnhancedRequest>any())).thenReturn(iterable);

        Optional<ContextoEntity> resultado = contextoRepository.buscarPorTelefone(telefone);

        assertTrue(resultado.isPresent());
        assertEquals(telefone, resultado.get().getTelefone());
    }

    @Test
    void deveRetornarVazioAoBuscarTelefoneInexistente() {
        Page<ContextoEntity> page = Page.create(List.of(), null);
        PageIterable<ContextoEntity> emptyIterable = PageIterable.create(() -> List.of(page).iterator());

        when(contextoTable.scan(ArgumentMatchers.<ScanEnhancedRequest>any())).thenReturn(emptyIterable);

        Optional<ContextoEntity> resultado = contextoRepository.buscarPorTelefone("000000000");

        assertTrue(resultado.isEmpty());
    }


}