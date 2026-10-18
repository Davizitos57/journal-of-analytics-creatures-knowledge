package com.scarfox.jack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scarfox.jack.domain.entity.HistoricoOcorrencia;

public interface HistoricoOcorrenciaRepository extends JpaRepository <HistoricoOcorrencia, UUID>{
    @EntityGraph(attributePaths = {"agente"})
    List<HistoricoOcorrencia> findByOcorrencia_UuidOrderByDataHoraDesc(UUID ocorrenciaUUID);
   
}
