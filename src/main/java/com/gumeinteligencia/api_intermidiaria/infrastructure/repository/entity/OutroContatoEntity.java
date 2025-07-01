package com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity;

import com.gumeinteligencia.api_intermidiaria.domain.outroContato.Setor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OutroContatoEntity {

    private String id;
    private String nome;
    private String telefone;
    private String descricao;
    private Setor setor;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public Setor getSetor() {
        return setor;
    }
}
