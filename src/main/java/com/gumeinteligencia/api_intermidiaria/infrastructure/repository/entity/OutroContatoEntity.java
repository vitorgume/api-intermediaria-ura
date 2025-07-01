package com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity;

import com.gumeinteligencia.api_intermidiaria.domain.outroContato.Setor;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "outros_contatos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OutroContatoEntity {

    @MongoId
    private String id;
    private String nome;
    private String telefone;
    private String descricao;
    private Setor setor;
}
