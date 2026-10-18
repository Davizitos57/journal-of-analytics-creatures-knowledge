package com.scarfox.jack.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scarfox.jack.domain.entity.Ocorrencia;
import com.scarfox.jack.domain.enums.Status;
import java.util.List;
import java.util.Optional;


public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, UUID>{
    @Override
    @EntityGraph(attributePaths = {"criaturas", "rituais"})
    List<Ocorrencia> findAll();

    @Override
    @EntityGraph(attributePaths = {"criaturas", "rituais"})
    Optional<Ocorrencia> findById(UUID uuid);

    @EntityGraph(attributePaths = {"criaturas", "rituais"})
    List<Ocorrencia> findByStatus(Status status);

    @EntityGraph(attributePaths = {"criaturas", "rituais"})
    List<Ocorrencia> findByAgenteRegistradorUuid(UUID agenteUUID);
}
