package com.scarfox.jack.service;

import com.scarfox.jack.domain.DTOs.AtualizarCriaturaDTO;
import com.scarfox.jack.domain.entity.Criatura;
import com.scarfox.jack.domain.enums.Elemento;
import com.scarfox.jack.domain.enums.Tamanho;
import com.scarfox.jack.exception.RecursoNaoEncontradoException;
import com.scarfox.jack.repository.CriaturaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.springframework.data.domain.Sort;

@Service
public class CriaturaService {

    private final CriaturaRepository criaturaRepository;

    public CriaturaService(CriaturaRepository criaturaRepository){
        this.criaturaRepository = criaturaRepository;
    }

    @Transactional
    public Criatura inserir(Criatura criatura){
        if (criatura.getNome() == null || criatura.getNome().isBlank()){
            throw new IllegalArgumentException("O nome da criatura deve ser informado, mesmo se você não souber, escreva DESCONHECIDO");
        }
        criatura.setNome(criatura.getNome().trim());
        validarElementos(criatura);
        return criaturaRepository.save(criatura);
    }

    @Transactional(readOnly = true)
    public List<Criatura> listarTodos(String nome, Tamanho tamanho, Elemento elementoPrincipal, Elemento elementoSecundario) {
        Specification<Criatura> filtros = (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicados.add(cb.like(cb.lower(root.get("nome")), "%" + nome.trim().toLowerCase(Locale.ROOT) + "%"));
            }
            if (tamanho != null) {
                predicados.add(cb.equal(root.get("tamanho"), tamanho));
            }
            if (elementoPrincipal != null) {
                predicados.add(cb.equal(root.get("elementoPrincipal"), elementoPrincipal));
            }
            if (elementoSecundario != null) {
                predicados.add(cb.equal(root.join("elementosSecundarios"), elementoSecundario));
            }
            return cb.and(predicados.toArray(new Predicate[0]));
        };

        return criaturaRepository.findAll(filtros, Sort.by("nome").ascending());
    }

    @Transactional(readOnly = true)
    public List<Criatura> buscarPorNome(String nome){
        if (nome == null || nome.isBlank()){
            throw new IllegalArgumentException("O nome da criatura deve ser informado");
        }
        return criaturaRepository.findByNomeIgnoreCase(nome.trim());
    }

    @Transactional(readOnly = true)
    public List<Criatura> pesquisarPorNome(String termo){
        if (termo == null || termo.trim().length() < 2){
            return List.of();
        }
        return criaturaRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(termo.trim());
    }

    @Transactional(readOnly = true)
    public Criatura buscarPorId(UUID id){
        return criaturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma criatura encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Criatura> buscarPorTamanho(Tamanho tamanho){
        return criaturaRepository.findByTamanho(tamanho);
    }

    @Transactional(readOnly = true)
    public List<Criatura> buscarPorElementoPrincipal(Elemento elemento){
        return criaturaRepository.findByElementoPrincipal(elemento);
    }

    @Transactional(readOnly = true)
    public List<Criatura> buscarPorElementoSecundario(Elemento elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("O elemento secundário deve ser informado.");
        }
        return criaturaRepository.findByElementosSecundariosContaining(elemento);
    }

    @Transactional(readOnly = true)
    public List<Criatura> buscarPorElementosSecundarios(Set<Elemento> elementos) {
        if (elementos == null || elementos.isEmpty()) {
            return List.of();
        }
        if (elementos.contains(null)) {
            throw new IllegalArgumentException("Os elementos secundários não podem conter valores nulos.");
        }
        return criaturaRepository.findByTodosElementosSecundarios(elementos, elementos.size());
    }

    @Transactional
    public Criatura atualizarCriatura(UUID uuid, AtualizarCriaturaDTO novosDados) {
        Criatura criaturaAtual = criaturaRepository.findById(uuid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma criatura encontrada com o ID: " + uuid));

        updateData(criaturaAtual, novosDados);
        validarElementos(criaturaAtual);
        return criaturaAtual;
    }

    @Transactional
    public void deletar(UUID uuid){
        Criatura criatura = criaturaRepository.findById(uuid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma criatura encontrada com o ID: " + uuid));
        criaturaRepository.delete(criatura);
    }

    private void updateData(Criatura atual, AtualizarCriaturaDTO novosDados) {
        if (novosDados.getNome() != null) {
            if (novosDados.getNome().isBlank()) {
                throw new IllegalArgumentException("O nome da criatura não pode ficar vazio.");
            }
            atual.setNome(novosDados.getNome().trim());
        }
        if (novosDados.getTamanho() != null) {
            atual.setTamanho(novosDados.getTamanho());
        }
        if (novosDados.getElementoPrincipal() != null) {
            atual.setElementoPrincipal(novosDados.getElementoPrincipal());
        }
        if (novosDados.getElementosSecundarios() != null) {
            atual.getElementosSecundarios().clear();
            atual.getElementosSecundarios().addAll(novosDados.getElementosSecundarios());
        }
        if (novosDados.getFotoUrl() != null) {
            atual.setFotoUrl(novosDados.getFotoUrl().trim());
        }
    }

    private void validarElementos(Criatura criatura) {
        if (criatura.getElementoPrincipal() != null && criatura.getElementosSecundarios() != null && criatura.getElementosSecundarios().contains(criatura.getElementoPrincipal())) {
            throw new IllegalArgumentException("O elemento principal não pode estar entre os elementos secundários.");
        }
    }
}
