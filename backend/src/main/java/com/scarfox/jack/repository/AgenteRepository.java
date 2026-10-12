package com.scarfox.jack.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scarfox.jack.domain.entity.Agente;

public interface AgenteRepository extends JpaRepository<Agente, UUID>{
    
}
