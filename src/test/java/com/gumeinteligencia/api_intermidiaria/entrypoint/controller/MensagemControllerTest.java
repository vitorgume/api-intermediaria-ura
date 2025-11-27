package com.gumeinteligencia.api_intermidiaria.entrypoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.application.usecase.ProcessarMensagemUseCase;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.TextoDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MensagemController.class)
@AutoConfigureMockMvc
class MensagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProcessarMensagemUseCase processarMensagemUseCase;

    @Test
    void deveProcessarMensagemComSucesso() throws Exception {
        MensagemDto mensagemDto = MensagemDto.builder()
                .phone("45999999999")
                .text(TextoDto.builder().message("Ola, gostaria de um orcamento.").build())
                .build();

        String mensagemJson = objectMapper.writeValueAsString(mensagemDto);

        mockMvc.perform(post("/mensagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mensagemJson))
                .andExpect(status().isOk());

        ArgumentCaptor<Mensagem> captor = ArgumentCaptor.forClass(Mensagem.class);
        verify(processarMensagemUseCase).processarNovaMensagem(captor.capture());

        Mensagem enviado = captor.getValue();
        org.assertj.core.api.Assertions.assertThat(enviado.getTelefone()).isEqualTo("45999999999");
        org.assertj.core.api.Assertions.assertThat(enviado.getMensagem()).isEqualTo("Ola, gostaria de um orcamento.");
    }
}
