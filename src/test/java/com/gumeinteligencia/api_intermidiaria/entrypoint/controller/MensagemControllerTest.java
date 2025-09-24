package com.gumeinteligencia.api_intermidiaria.entrypoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.application.gateways.MensageriaGateway;
import com.gumeinteligencia.api_intermidiaria.application.usecase.ContextoUseCase;
import com.gumeinteligencia.api_intermidiaria.application.usecase.validadorMensagens.ValidadorMensagemUseCase;
import com.gumeinteligencia.api_intermidiaria.domain.Contexto;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.TextoDto;
import com.gumeinteligencia.api_intermidiaria.infrastructure.mapper.ContextoMapper;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.CLienteRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.ContextoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.OutroContatoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
        "ura.url=teste",
        "aws.sqs.url=teste",
        "spring.datasource.url=teste",
        "spring.datasource.username=teste",
        "spring.datasource.password=teste",
        "ura.api.key=teste",
        "ura.url=teste",
        "experimentos.chatbot.percentual=30",
        "aws.sqs.delay=0",
        "management.endpoints.web.exposure.include=health,info",
        "management.endpoint.health.probes.enabled=true"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MensagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContextoRepository contextoRepository;

    @MockitoBean
    private OutroContatoRepository outroContatoRepository;

    @MockitoBean
    private MensageriaGateway mensageriaGateway;

    @MockitoBean
    private DynamoDbTemplate dynamoDbTemplate;

    @MockitoBean
    private ContextoUseCase contextoUseCase;

    @MockitoBean
    private DynamoDbClient dynamoDbClient;

    @MockitoBean
    private ValidadorMensagemUseCase validadorMensagemUseCase;

    @MockitoBean
    private CLienteRepository cLienteRepository;

    private MensagemDto mensagemDto;

    private ContextoEntity contextoEntity;

    @BeforeEach
    void setUp() {
        mensagemDto = MensagemDto.builder()
                .phone("45999999999")
                .text(TextoDto.builder().message("Olá, gostaria de um orçamento.").build())
                .build();

        contextoEntity = ContextoEntity.builder()
                .id(UUID.randomUUID())
                .telefone("45999999999")
                .status(StatusContexto.ATIVO)
                .mensagens(List.of("Olá"))
                .build();
    }

    @Test
    void deveProcessarMensagemDeUmNovoContextoComSucesso() throws Exception {
        when(outroContatoRepository.listar()).thenReturn(List.of());
        when(contextoRepository.buscarPorTelefone(any())).thenReturn(Optional.empty());
        when(contextoRepository.salvar(any())).thenReturn(contextoEntity);
        when(mensageriaGateway.enviarParaFila(any())).thenReturn(null);

        String mensagemJson = objectMapper.writeValueAsString(mensagemDto);

        mockMvc.perform(post("/mensagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mensagemJson)
                ).andExpect(status().isOk());

        verify(contextoUseCase).iniciarNovoContexto(any(Mensagem.class));
    }

    @Test
    void deveProcessarMensagemDeUmContextoExistenteComSucesso() throws Exception {
        when(outroContatoRepository.listar()).thenReturn(List.of());
        when(contextoUseCase.consultarPorTelefoneAtivo(any())).thenReturn(Optional.of(ContextoMapper.paraDomain(contextoEntity)));
        when(contextoRepository.salvar(any())).thenReturn(contextoEntity);
        when(mensageriaGateway.enviarParaFila(any())).thenReturn(null);

        String mensagemJson = objectMapper.writeValueAsString(mensagemDto);

        mockMvc.perform(post("/mensagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mensagemJson)
        ).andExpect(status().isOk());

        verify(contextoUseCase).processarContextoExistente(any(Contexto.class) ,any(Mensagem.class));
    }
}