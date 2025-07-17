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
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextoDataProviderTest {

    @Mock
    private ContextoRepository repository;

    @Mock
    private DynamoDbClient dynamoDbClient;

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
    void deveConsultarPorTelefoneAtivoComSucesso() {
        Map<String, AttributeValue> itemMap = new HashMap<>();
        itemMap.put("id", AttributeValue.fromS(contextoEntity.getId().toString()));
        itemMap.put("telefone", AttributeValue.fromS(contextoEntity.getTelefone()));
        itemMap.put("status", AttributeValue.fromS(contextoEntity.getStatus().name()));
        itemMap.put("mensagens", AttributeValue.fromSs(contextoEntity.getMensagens()));

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(itemMap))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> resultado = dataProvider.consultarPorTelefoneAtivo("45999999999");

        assertTrue(resultado.isPresent());
        assertEquals("45999999999", resultado.get().getTelefone());
        assertEquals(List.of("Oi"), resultado.get().getMensagens());
        assertEquals(StatusContexto.ATIVO, resultado.get().getStatus());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarTelefone() {
        QueryResponse responseVazio = QueryResponse.builder()
                .items(List.of())
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(responseVazio);

        Optional<Contexto> resultado = dataProvider.consultarPorTelefoneAtivo("000000000");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveLancarExcecaoAoConsultarTelefoneComErro() {
        when(dynamoDbClient.query(any(QueryRequest.class))).thenThrow(new RuntimeException("Erro simulado"));

        DataProviderException ex = assertThrows(DataProviderException.class, () ->
                dataProvider.consultarPorTelefoneAtivo("erro"));

        assertEquals("Erro ao consultar contexto pelo seu telefone e ativo.", ex.getMessage());
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