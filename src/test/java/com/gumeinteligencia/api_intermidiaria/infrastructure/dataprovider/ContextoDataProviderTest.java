package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions.DataProviderException;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.ContextoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    // -------------------- seus testes originais --------------------

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

    // -------------------- NOVOS TESTES (aumentam branch coverage) --------------------

    @Test
    void deveConstruirQueryRequestCorretamente() {
        // arrange: resposta com 1 item válido
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("45999999999"));
        item.put("status", AttributeValue.fromS(StatusContexto.ATIVO.name()));
        item.put("mensagens", AttributeValue.fromSs(List.of("Oi")));

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        ArgumentCaptor<QueryRequest> captor = ArgumentCaptor.forClass(QueryRequest.class);
        when(dynamoDbClient.query(captor.capture())).thenReturn(mockResponse);

        // act
        dataProvider.consultarPorTelefoneAtivo("45999999999");

        // assert: valida campos de QueryRequest
        QueryRequest sent = captor.getValue();
        assertEquals("contexto_entity", sent.tableName());
        assertEquals("TelefoneStatusIndex", sent.indexName());
        assertEquals("telefone = :telefone AND #status = :status", sent.keyConditionExpression());
        assertEquals(Integer.valueOf(1), sent.limit());
        assertEquals("status", sent.expressionAttributeNames().get("#status"));
        assertEquals("45999999999", sent.expressionAttributeValues().get(":telefone").s());
        assertEquals("ATIVO", sent.expressionAttributeValues().get(":status").s());
    }

    @Test
    void deveConverterMensagensQuandoAtributoMensagensAusente() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("111"));
        item.put("status", AttributeValue.fromS(StatusContexto.ATIVO.name()));
        // sem chave "mensagens"

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> out = dataProvider.consultarPorTelefoneAtivo("111");
        assertTrue(out.isPresent());
        assertNotNull(out.get().getMensagens());
        assertTrue(out.get().getMensagens().isEmpty(), "Quando não há 'mensagens', deve voltar lista vazia");
    }

    @Test
    void deveConverterMensagensQuandoVemComoListaL() {
        // mensagens via atributo L (lista de AttributeValue S)
        List<AttributeValue> l = List.of(
                AttributeValue.builder().s("A").build(),
                AttributeValue.builder().s("B").build(),
                AttributeValue.builder().s("C").build()
        );
        AttributeValue mensagensL = AttributeValue.builder().l(l).build();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("222"));
        item.put("status", AttributeValue.fromS(StatusContexto.ATIVO.name()));
        item.put("mensagens", mensagensL);

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> out = dataProvider.consultarPorTelefoneAtivo("222");
        assertTrue(out.isPresent());
        assertEquals(List.of("A", "B", "C"), out.get().getMensagens());
    }

    @Test
    void deveConverterMensagensQuandoSsVazioMasLTemValores() {
        // força caminho do "else if": ss presente porém vazio + l não vazio
        List<AttributeValue> l = List.of(
                AttributeValue.builder().s("X").build(),
                AttributeValue.builder().s("Y").build()
        );
        AttributeValue mensagensMistas = AttributeValue.builder()
                .ss(new ArrayList<>()) // ss vazio
                .l(l)                  // l com valores
                .build();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("333"));
        item.put("status", AttributeValue.fromS(StatusContexto.ATIVO.name()));
        item.put("mensagens", mensagensMistas);

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> out = dataProvider.consultarPorTelefoneAtivo("333");
        assertTrue(out.isPresent());
        assertEquals(List.of("X", "Y"), out.get().getMensagens(),
                "Quando ss está vazio e l tem valores, deve usar l");
    }

    @Test
    void deveConverterMensagensQuandoSsELVazios() {
        // ambos vazios → mensagens deve ficar lista vazia
        AttributeValue mensagensVazias = AttributeValue.builder()
                .ss(new ArrayList<>())
                .l(new ArrayList<>())
                .build();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("444"));
        item.put("status", AttributeValue.fromS(StatusContexto.ATIVO.name()));
        item.put("mensagens", mensagensVazias);

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> out = dataProvider.consultarPorTelefoneAtivo("444");
        assertTrue(out.isPresent());
        assertNotNull(out.get().getMensagens());
        assertTrue(out.get().getMensagens().isEmpty(), "Com ss e l vazios, resultado deve ser vazio");
    }

    @Test
    void deveLancarIllegalArgumentQuandoStatusInvalido() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("555"));
        item.put("status", AttributeValue.fromS("QUALQUER_COISA")); // inválido p/ valueOf
        item.put("mensagens", AttributeValue.fromSs(List.of("Oi")));

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        assertThrows(IllegalArgumentException.class, () -> dataProvider.consultarPorTelefoneAtivo("555"),
                "Status inválido deve propagar IllegalArgumentException (fora do try/catch)");
    }
}