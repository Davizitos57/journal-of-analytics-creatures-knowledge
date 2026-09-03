package com.scarfox.jack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scarfox.jack.domain.entity.Local;

public interface LocalRepository extends JpaRepository<Local, UUID>{
    List<String> findDistinctPaisOrderByPaisAsc();
    List<String> findDistinctEstadoProvinciaByPaisIgnoreCaseOrderByEstadoProvinciaAsc(String pais);
    List<String> findDistinctCidadeByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseOrderByCidadeAsc(String pais, String estadoProvincia);
    List<String> findDistinctBairroByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseAndCidadeIgnoreCaseOrderByBairroAsc(String pais, String estadoProvincia, String cidade);
    List<String> findDistinctRuaByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseAndCidadeIgnoreCaseAndBairroIgnoreCaseOrderByRuaAsc(String pais, String estadoProvincia, String cidade, String bairro);
}
