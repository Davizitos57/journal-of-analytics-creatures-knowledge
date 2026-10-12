package com.scarfox.jack.repository;

import com.scarfox.jack.domain.entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EquipeRepository extends JpaRepository<Equipe, UUID> {
    boolean existsByNomeIgnoreCase(String nome);
    List<Equipe> findByNomeContainingIgnoreCaseOrderByNomeAsc(String termo);
    boolean existsByNomeIgnoreCaseAndUuidNot(String nome, UUID uuid);
}
