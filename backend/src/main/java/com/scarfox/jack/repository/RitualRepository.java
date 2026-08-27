package com.scarfox.jack.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scarfox.jack.domain.entity.Ritual;
import com.scarfox.jack.domain.enums.Elemento;

public interface RitualRepository extends JpaRepository<Ritual, UUID> {

    Optional<Ritual> findByNomeIgnoreCase(String nome);
    List<Ritual> findByNomeContainingIgnoreCaseOrderByNomeAsc(String termo);
    List<Ritual> findByElemento(Elemento elemento);
}