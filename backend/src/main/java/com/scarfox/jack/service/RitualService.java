package com.scarfox.jack.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scarfox.jack.domain.entity.Ritual;
import com.scarfox.jack.domain.enums.Elemento;
import com.scarfox.jack.exception.RecursoNaoEncontradoException;
import com.scarfox.jack.repository.RitualRepository;

@Service
public class RitualService {

    private final RitualRepository ritualRepository;

    public RitualService(RitualRepository ritualRepository) {
        this.ritualRepository = ritualRepository;
    }

    @Transactional
    public Ritual inserir(Ritual ritual) {
        ritual.setNome(ritual.getNome().trim());
        return ritualRepository.save(ritual);
    }

    @Transactional(readOnly = true)
    public List<Ritual> listarTodos(String nome, Elemento elemento) {
        if (nome != null && !nome.isBlank() && elemento != null) {
            return ritualRepository.findByNomeContainingIgnoreCaseAndElemento(nome.trim(),elemento);
            }
            if (nome != null && !nome.isBlank()) {
                return ritualRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
            }
            if (elemento != null) {
                return ritualRepository.findByElemento(elemento);
            }
        return ritualRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ritual buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do ritual deve ser informado.");
        }
        return ritualRepository.findByNomeIgnoreCase(nome.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum ritual encontrado com o nome: " + nome));
    }

    @Transactional(readOnly = true)
    public List<Ritual> pesquisarPorNome(String termo) {
        if (termo == null || termo.trim().length() < 2) {
            return List.of();
        }
        return ritualRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(termo.trim());
    }

    @Transactional(readOnly = true)
    public Ritual buscarPorId(UUID id) {
        return ritualRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum ritual encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Ritual> buscarPorElemento(Elemento elemento) {
        return ritualRepository.findByElemento(elemento);
    }

    @Transactional
    public Ritual atualizarRitual(UUID uuid, Ritual novosDados) {
        Ritual ritualAtual = ritualRepository.findById(uuid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum ritual encontrado com o ID: " + uuid));
        updateData(ritualAtual, novosDados);
        return ritualAtual;
    }

    @Transactional
    public void deletar(UUID uuid){
        Ritual ritual = ritualRepository.findById(uuid) 
            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum ritual encontrado com o ID: " + uuid));
        
        ritualRepository.delete(ritual);
    }

    private void updateData(Ritual atual, Ritual novosDados) {
        if (novosDados.getNome() != null && !novosDados.getNome().isBlank()) {
            atual.setNome(novosDados.getNome().trim());
        }
        if (novosDados.getDescricao() != null) {
            atual.setDescricao(novosDados.getDescricao());
        }
        if (novosDados.getElemento() != null) {
            atual.setElemento(novosDados.getElemento());
        }   
    }
}