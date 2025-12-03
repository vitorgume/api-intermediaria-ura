package com.gumeinteligencia.api_intermidiaria.infrastructure.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumeinteligencia.api_intermidiaria.domain.MensagemContexto;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MensagemContextoListConverter implements AttributeConverter<List<MensagemContexto>> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public AttributeValue transformFrom(List<MensagemContexto> input) {
        if (input == null || input.isEmpty()) {
            return AttributeValue.builder().nul(true).build();
        }
        return AttributeValue.builder().s(toJson(input)).build();
    }

    @Override
    public List<MensagemContexto> transformTo(AttributeValue input) {
        return fromAttributeValue(input);
    }

    @Override
    public EnhancedType<List<MensagemContexto>> type() {
        return EnhancedType.listOf(MensagemContexto.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        // vamos armazenar como String (S) no DynamoDB
        return AttributeValueType.S;
    }

    public static List<MensagemContexto> fromAttributeValue(AttributeValue input) {
        if (input == null || (input.nul() != null && input.nul())) {
            return Collections.emptyList();
        }

        if (input.s() != null && !input.s().isBlank()) {
            return fromJson(input.s());
        }

        if (input.ss() != null && !input.ss().isEmpty()) {
            return input.ss().stream()
                    .map(value -> MensagemContexto.builder().mensagem(value).build())
                    .toList();
        }

        if (input.l() != null && !input.l().isEmpty()) {
            return input.l().stream()
                    .map(AttributeValue::s)
                    .map(value -> MensagemContexto.builder().mensagem(value).build())
                    .toList();
        }

        return Collections.emptyList();
    }

    private static String toJson(List<MensagemContexto> input) {
        try {
            return MAPPER.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro convertendo lista de MensagemContexto para JSON", e);
        }
    }

    private static List<MensagemContexto> fromJson(String json) {
        try {
            return MAPPER.readValue(json, new TypeReference<List<MensagemContexto>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Erro convertendo JSON para lista de MensagemContexto", e);
        }
    }
}
