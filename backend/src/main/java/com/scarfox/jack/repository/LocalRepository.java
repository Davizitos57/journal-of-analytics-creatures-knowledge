package com.scarfox.jack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scarfox.jack.domain.entity.Local;

public interface LocalRepository extends JpaRepository<Local, UUID> {
    List<Local> findAllByOrderByPaisAsc();
    List<Local> findByPaisIgnoreCaseOrderByEstadoProvinciaAsc(String pais);
    List<Local> findByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseOrderByCidadeAsc(String pais, String estadoProvincia);
    List<Local> findByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseAndCidadeIgnoreCaseOrderByBairroAsc(String pais, String estadoProvincia, String cidade);
    List<Local> findByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseAndCidadeIgnoreCaseAndBairroIgnoreCaseOrderByRuaAsc(String pais, String estadoProvincia, String cidade, String bairro);
}