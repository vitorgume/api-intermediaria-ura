package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContextoRepository {

    private final DynamoDbTemplate dynamoDbTemplate;

    public ContextoEntity salvar(ContextoEntity contexto) {
            return dynamoDbTemplate.save(contexto);
    }

    public Optional<ContextoEntity> buscarPorId(String id) {
        ContextoEntity contexto = dynamoDbTemplate.load(Key.builder()
                .partitionValue(id)
                .build()
                ,ContextoEntity.class
        );

        return contexto == null ? Optional.empty() : Optional.of(contexto);
    }

    public Optional<ContextoEntity> buscarPorTelefone(String telefone) {
        PageIterable<ContextoEntity> results = dynamoDbTemplate.scan(ScanEnhancedRequest.builder().build(), ContextoEntity.class);

        return results.items()
                .stream()
                .filter(c -> telefone.equals(c.getTelefone()))
                .findFirst();
    }
}
