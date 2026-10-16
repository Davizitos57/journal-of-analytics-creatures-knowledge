package com.scarfox.jack.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scarfox.jack.domain.entity.Agente;
import com.scarfox.jack.domain.enums.Patente;

public interface AgenteRepository extends JpaRepository<Agente, UUID>{
    Optional<Agente> findByKeycloakId(String keycloakId);
    boolean existsByEmailIgnoreCase(String email);
    Optional<List<Agente>> findByPatente(Patente patente);
}
