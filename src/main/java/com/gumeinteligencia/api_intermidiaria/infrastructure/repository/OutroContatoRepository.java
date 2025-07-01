package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ContextoEntity;
import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.OutroContatoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OutroContatoRepository {

    private final DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<OutroContatoEntity> getTable() {
        return enhancedClient.table("outros_contatos", TableSchema.fromBean(OutroContatoEntity.class));
    }

    public void salvar(OutroContatoEntity outroContato) {
        getTable().putItem(outroContato);
    }

    public List<OutroContatoEntity> listar() {
        DynamoDbTable<OutroContatoEntity> table = getTable();
        return table.scan().items().stream().collect(Collectors.toList());
    }
}
