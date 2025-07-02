package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContextoRepository {


    private final DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<ContextoEntity> getTable() {
        return enhancedClient.table("contextos", TableSchema.fromBean(ContextoEntity.class));
    }

    public ContextoEntity salvar(ContextoEntity contexto) {
        getTable().putItem(contexto);
        return getTable().getItem(Key.builder().partitionValue(contexto.getId().toString()).build());
    }

    public Optional<ContextoEntity> buscarPorId(String id) {
        return Optional.ofNullable(getTable().getItem(Key.builder().partitionValue(id).build()));
    }

    public Optional<ContextoEntity> buscarPorTelefone(String telefone) {
        DynamoDbTable<ContextoEntity> table = getTable();

        Expression expression = Expression.builder()
                .expression("telefone = :telefone")
                .putExpressionValue(":telefone", AttributeValue.fromS(telefone))
                .build();

        return table.scan(ScanEnhancedRequest.builder()
                        .filterExpression(expression)
                        .build())
                .items()
                .stream()
                .findFirst();
    }
}
