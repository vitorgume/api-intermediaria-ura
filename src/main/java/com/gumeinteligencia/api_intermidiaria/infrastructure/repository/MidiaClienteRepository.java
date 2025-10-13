package com.gumeinteligencia.api_intermidiaria.infrastructure.repository;

import com.gumeinteligencia.api_intermidiaria.infrastructure.repository.entity.MidiaClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MidiaClienteRepository extends JpaRepository<MidiaClienteEntity, UUID> {
}
