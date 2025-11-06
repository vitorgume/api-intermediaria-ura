package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CLienteRepository extends JpaRepository<ClienteEntity, UUID> {
    Optional<ClienteEntity> findByTelefoneAndInativoFalse(String telefone);
}
