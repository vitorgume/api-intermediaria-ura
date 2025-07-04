package com.gumeinteligencia.api_intermidiaria.entrypoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.application.gateways.SqsGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.TextoDto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.mapper.MensagemMapper;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.ContextoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.OutroContatoRepository;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    private SqsGateway sqsGateway;

    @MockitoBean
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private ContextoEntity contextoEntity;

    @BeforeEach
    void setUp() {
        contextoEntity = ContextoEntity.builder()
                .id(UUID.randomUUID())
                .telefone("45999999999")
                .status(StatusContexto.ATIVO)
                .mensagens(List.of("Oi"))
                .build();
    }

    @Test
    void deveProcessarMensagemQuandoNaoForParaIgnorar() throws Exception {
        MensagemDto dto = MensagemDto.builder()
                .phone("44999999999")
                .text(TextoDto.builder()
                        .message("Oi")
                        .build()
                ).build();

        Mensagem domain = MensagemMapper.paraDomain(dto);

        when(outroContatoRepository.listar()).thenReturn(new ArrayList<>());
        when(contextoRepository.buscarPorTelefone(domain.getTelefone())).thenReturn(Optional.empty());
        when(contextoRepository.salvar(Mockito.any())).thenReturn(contextoEntity);
        when(sqsGateway.enviarParaFila(Mockito.any())).thenReturn(null);

        mockMvc.perform(post("/mensagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

//        verify(contextoUseCase).iniciarNovoContexto(argThat(m -> m.getTelefone().equals(dto.getPhone())));
//        verify(contextoUseCase, never()).processarContextoExistente(any(), any());
    }

//    @Test
//    void deveIgnorarMensagemQuandoValidadorRetornarTrue() throws Exception {
//        MensagemDto dto = MensagemDto.builder()
//                .phone("44999999999")
//                .text(TextoDto.builder()
//                        .message("Oi")
//                        .build()
//                ).build();
//
//        when(validadorMensagem.deveIngorar(any())).thenReturn(true);
//
//        mockMvc.perform(post("/mensagens")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk());
//
//        verifyNoInteractions(contextoUseCase);
//    }
//
//    @Test
//    void deveProcessarContextoExistenteQuandoEncontrarContexto() throws Exception {
//        MensagemDto dto = MensagemDto.builder()
//                .phone("44999999999")
//                .text(TextoDto.builder()
//                        .message("Oi")
//                        .build()
//                ).build();
//
//        Mensagem domain = MensagemMapper.paraDomain(dto);
//        Contexto contextoExistente = Contexto.builder().telefone(dto.getPhone()).mensagens(new ArrayList<>()).build();
//
//        when(validadorMensagem.deveIngorar(any())).thenReturn(false);
//        when(contextoUseCase.consultarPorTelefone(dto.getPhone())).thenReturn(Optional.of(contextoExistente));
//
//        mockMvc.perform(post("/mensagens")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk());
//
//        verify(contextoUseCase).processarContextoExistente(eq(contextoExistente), any());
//        verify(contextoUseCase, never()).iniciarNovoContexto(any());
//    }

}