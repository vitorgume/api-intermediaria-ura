package com.gumeinteligencia.api_intermidiaria.infrastructure.dataprovider;

import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.MensagemContexto;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private MensagemContexto mensagemContexto;
    private List<String> mensagensComoString;

    @BeforeEach
    void setUp() {
        mensagemContexto = MensagemContexto.builder().mensagem("Oi").build();
        mensagensComoString = List.of(mensagemContexto.getMensagem());

        contextoEntity = ContextoEntity.builder()
                .id(UUID.randomUUID())
                .telefone("45999999999")
                .mensagens(List.of(mensagemContexto))
                .build();

        contexto = Contexto.builder()
                .id(contextoEntity.getId())
                .telefone(contextoEntity.getTelefone())
                .mensagens(contextoEntity.getMensagens())
                .build();
    }

    @Test
    void deveConsultarPorTelefoneComSucesso() {
        Map<String, AttributeValue> itemMap = new HashMap<>();
        itemMap.put("id", AttributeValue.fromS(contextoEntity.getId().toString()));
        itemMap.put("telefone", AttributeValue.fromS(contextoEntity.getTelefone()));
        itemMap.put("mensagens", AttributeValue.fromSs(mensagensComoString));

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(itemMap))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> resultado = dataProvider.consultarPorTelefone("45999999999");

        assertTrue(resultado.isPresent());
        assertEquals("45999999999", resultado.get().getTelefone());
        assertEquals(mensagensComoString, resultado.get().getMensagens().stream().map(MensagemContexto::getMensagem).toList());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarTelefone() {
        QueryResponse responseVazio = QueryResponse.builder()
                .items(List.of())
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(responseVazio);

        Optional<Contexto> resultado = dataProvider.consultarPorTelefone("000000000");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveLancarExcecaoAoConsultarTelefoneComErro() {
        when(dynamoDbClient.query(any(QueryRequest.class))).thenThrow(new RuntimeException("Erro simulado"));

        DataProviderException ex = assertThrows(DataProviderException.class, () ->
                dataProvider.consultarPorTelefone("erro"));

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

    @Test
    void deveConstruirQueryRequestCorretamente() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("45999999999"));
        item.put("mensagens", AttributeValue.fromSs(List.of("Oi")));

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        ArgumentCaptor<QueryRequest> captor = ArgumentCaptor.forClass(QueryRequest.class);
        when(dynamoDbClient.query(captor.capture())).thenReturn(mockResponse);

        dataProvider.consultarPorTelefone("45999999999");

        QueryRequest sent = captor.getValue();
        assertEquals("contexto_entity", sent.tableName());
        assertEquals("TelefoneIndex", sent.indexName());
        assertEquals("telefone = :telefone", sent.keyConditionExpression());
        assertEquals(Integer.valueOf(1), sent.limit());
        assertEquals("45999999999", sent.expressionAttributeValues().get(":telefone").s());
    }

    @Test
    void deveConverterMensagensQuandoAtributoMensagensAusente() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("111"));
        // sem chave "mensagens"

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> out = dataProvider.consultarPorTelefone("111");
        assertTrue(out.isPresent());
        assertNotNull(out.get().getMensagens());
        assertTrue(out.get().getMensagens().isEmpty(), "Quando nao ha 'mensagens', deve voltar lista vazia");
    }

    @Test
    void deveConverterMensagensQuandoVemComoListaL() {
        List<AttributeValue> l = List.of(
                AttributeValue.builder().s("A").build(),
                AttributeValue.builder().s("B").build(),
                AttributeValue.builder().s("C").build()
        );
        AttributeValue mensagensL = AttributeValue.builder().l(l).build();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("222"));
        item.put("mensagens", mensagensL);

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> out = dataProvider.consultarPorTelefone("222");
        assertTrue(out.isPresent());
        assertEquals(List.of("A", "B", "C"), out.get().getMensagens().stream().map(MensagemContexto::getMensagem).toList());
    }

    @Test
    void deveConverterMensagensQuandoSsVazioMasLTemValores() {
        List<AttributeValue> l = List.of(
                AttributeValue.builder().s("X").build(),
                AttributeValue.builder().s("Y").build()
        );
        AttributeValue mensagensMistas = AttributeValue.builder()
                .ss(new ArrayList<>())
                .l(l)
                .build();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("333"));
        item.put("mensagens", mensagensMistas);

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> out = dataProvider.consultarPorTelefone("333");
        assertTrue(out.isPresent());
        assertEquals(List.of("X", "Y"), out.get().getMensagens().stream().map(MensagemContexto::getMensagem).toList(),
                "Quando ss esta vazio e l tem valores, deve usar l");
    }

    @Test
    void deveConverterMensagensQuandoSsELVazios() {
        AttributeValue mensagensVazias = AttributeValue.builder()
                .ss(new ArrayList<>())
                .l(new ArrayList<>())
                .build();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("telefone", AttributeValue.fromS("444"));
        item.put("mensagens", mensagensVazias);

        QueryResponse mockResponse = QueryResponse.builder()
                .items(List.of(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(mockResponse);

        Optional<Contexto> out = dataProvider.consultarPorTelefone("444");
        assertTrue(out.isPresent());
        assertNotNull(out.get().getMensagens());
        assertTrue(out.get().getMensagens().isEmpty(), "Com ss e l vazios, resultado deve ser vazio");
    }
}
