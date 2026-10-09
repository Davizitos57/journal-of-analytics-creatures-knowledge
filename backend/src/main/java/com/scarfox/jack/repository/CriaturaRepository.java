package com.scarfox.jack.repository;

import com.scarfox.jack.domain.entity.Criatura;
import com.scarfox.jack.domain.enums.Elemento;
import com.scarfox.jack.domain.enums.Tamanho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CriaturaRepository extends JpaRepository<Criatura, UUID>, JpaSpecificationExecutor<Criatura> {
    List<Criatura> findByNomeIgnoreCase(String nome);
    List<Criatura> findByNomeContainingIgnoreCaseOrderByNomeAsc(String termo);
    List<Criatura> findByElementoPrincipal(Elemento elemento);
    List<Criatura> findByElementosSecundariosContaining(Elemento elemento);
    @Query("SELECT c FROM Criatura c JOIN c.elementosSecundarios e WHERE e IN :elementos GROUP BY c HAVING COUNT(DISTINCT e) = :quantidade")
    List<Criatura> findByTodosElementosSecundarios(@Param("elementos") Set<Elemento> elementos, @Param("quantidade") long quantidade);
    List<Criatura> findByTamanho(Tamanho tamanho);
}
