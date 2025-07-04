package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.OutroContatoEntity;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OutroContatoRepository {

    private final DynamoDbTemplate dynamoDbTemplate;


    public OutroContatoEntity salvar(OutroContatoEntity outroContato) {
        return dynamoDbTemplate.save(outroContato);
    }

    public List<OutroContatoEntity> listar() {
        dynamoDbTemplate.scanAll()
    }
}
