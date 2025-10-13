package com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Entity(name = "MidiaCliente")
@Table(name = "medias_clientes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MidiaClienteEntity {
    private UUID id;
    private String telefoneCliente;
    private List<String> urlMidias;
}
