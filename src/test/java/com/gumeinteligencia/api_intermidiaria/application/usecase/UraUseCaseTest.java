package com.gumeinteligencia.api_intermidiaria.application.usecase;

import com.gumeinteligencia.api_intermidiaria.application.gateways.UraGateway;
import com.gumeinteligencia.api_intermidiaria.domain.Mensagem;
import com.gumeinteligencia.api_intermidiaria.entrypoint.controller.dto.MensagemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class UraUseCaseTest {

    @Mock
    private UraGateway uraGateway;

    @InjectMocks
    private UraUseCase uraUseCase;

    private Mensagem mensagem;

    @BeforeEach
    void setUp() {
        mensagem = Mensagem.builder()
                .telefone("44999999999")
                .mensagem("Olá mundo")
                .build();
    }

    @Test
    void deveMapearMensagemParaDtoEChamarGateway() {
        ArgumentCaptor<MensagemDto> captor = ArgumentCaptor.forClass(MensagemDto.class);

        uraUseCase.enviar(mensagem);

        verify(uraGateway).enviarMensagem(captor.capture());
        verifyNoMoreInteractions(uraGateway);

        MensagemDto dto = captor.getValue();
        assertThat(dto.getPhone()).isEqualTo("44999999999");
        assertThat(dto.getText()).isNotNull();
        assertThat(dto.getText().getMessage()).isEqualTo("Olá mundo");
    }

}