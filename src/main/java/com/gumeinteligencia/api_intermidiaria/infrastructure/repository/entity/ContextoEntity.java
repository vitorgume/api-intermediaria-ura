package com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity;

import com.gumeinteligencia.api_intermidiaria.domain.StatusContexto;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "contextos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContextoEntity {

    @MongoId
    private String id;
    private String telefone;
    private List<String> mensagens;
    private StatusContexto status;
}
