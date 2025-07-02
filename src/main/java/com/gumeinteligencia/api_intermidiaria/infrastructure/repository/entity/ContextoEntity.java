package com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity;

import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;
import java.util.UUID;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ContextoEntity {

    private UUID id;
    private String telefone;
    private List<String> mensagens;
    private StatusContexto status;

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }

    public String getTelefone() {
        return telefone;
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    public StatusContexto getStatus() {
        return status;
    }
}
