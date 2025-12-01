package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.domain.MensagemContexto;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MensagemContextoListConverterTest {

    private final MensagemContextoListConverter converter = new MensagemContextoListConverter();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void transformFromDeveRetornarNulQuandoListaNula() {
        AttributeValue attributeValue = converter.transformFrom(null);

        assertTrue(Boolean.TRUE.equals(attributeValue.nul()));
    }

    @Test
    void transformFromDeveRetornarNulQuandoListaVazia() {
        AttributeValue attributeValue = converter.transformFrom(Collections.emptyList());

        assertTrue(Boolean.TRUE.equals(attributeValue.nul()));
    }

    @Test
    void transformFromDeveSerializarListaParaJson() throws Exception {
        List<MensagemContexto> mensagens = List.of(
                MensagemContexto.builder().mensagem("ola").imagemUrl("img").audioUrl("aud").build(),
                MensagemContexto.builder().mensagem("tchau").build()
        );

        AttributeValue attributeValue = converter.transformFrom(mensagens);

        String expectedJson = objectMapper.writeValueAsString(mensagens);
        assertEquals(expectedJson, attributeValue.s());
        assertFalse(Boolean.TRUE.equals(attributeValue.nul()));
    }

    @Test
    void transformToDeveRetornarListaVaziaQuandoInputNulo() {
        List<MensagemContexto> resultado = converter.transformTo(null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void transformToDeveRetornarListaVaziaQuandoAttributeNul() {
        AttributeValue attributeValue = AttributeValue.builder().nul(true).build();

        List<MensagemContexto> resultado = converter.transformTo(attributeValue);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void transformToDeveDesserializarJsonParaLista() throws Exception {
        List<MensagemContexto> mensagens = List.of(
                MensagemContexto.builder().mensagem("primeira").build(),
                MensagemContexto.builder().mensagem("segunda").imagemUrl("img2").audioUrl("aud2").build()
        );
        String json = objectMapper.writeValueAsString(mensagens);
        AttributeValue attributeValue = AttributeValue.builder().s(json).build();

        List<MensagemContexto> resultado = converter.transformTo(attributeValue);

        List<MensagemContexto> expected = objectMapper.readValue(json, new TypeReference<>() {});
        assertEquals(expected.size(), resultado.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getMensagem(), resultado.get(i).getMensagem());
            assertEquals(expected.get(i).getImagemUrl(), resultado.get(i).getImagemUrl());
            assertEquals(expected.get(i).getAudioUrl(), resultado.get(i).getAudioUrl());
        }
    }

    @Test
    void transformToDeveLancarRuntimeExceptionQuandoJsonInvalido() {
        AttributeValue attributeValue = AttributeValue.builder().s("{json-invalido").build();

        assertThrows(RuntimeException.class, () -> converter.transformTo(attributeValue));
    }

    @Test
    void attributeValueTypeDeveRetornarString() {
        assertEquals(AttributeValueType.S, converter.attributeValueType());
    }

    @Test
    void typeDeveRetornarListaDeMensagemContexto() {
        assertEquals(EnhancedType.listOf(MensagemContexto.class), converter.type());
    }
}
