package com.scarfox.jack.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scarfox.jack.domain.entity.Agente;
import com.scarfox.jack.domain.enums.Patente;

public interface AgenteRepository extends JpaRepository<Agente, UUID>{
    List<Agente> findByAtivoTrue();
    Optional<Agente> findByUuidAndAtivoTrue(UUID uuid);
    List<Agente> findByPatenteAndAtivoTrue(Patente patente);
    Optional<Agente> findByKeycloakIdAndAtivoTrue(String keycloakId);
    List<Agente> findByEquipeUuidAndAtivoTrue(UUID equipeUUID);
    long countByEquipeUuidAndAtivoTrue(UUID equipeUUID);
    Optional<Agente> findByKeycloakId(String keycloakId);
    boolean existsByEmailIgnoreCase(String email);
    Optional<List<Agente>> findByPatente(Patente patente);
    long countByEquipeUuid(UUID equipeUUID);
    List<Agente> findByEquipeUuid(UUID equipeUUID);
}
