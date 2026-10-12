package com.scarfox.jack.service;

import com.scarfox.jack.domain.entity.Equipe;
import com.scarfox.jack.exception.RecursoNaoEncontradoException;
import com.scarfox.jack.repository.EquipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;

    public EquipeService(EquipeRepository equipeRepository){
        this.equipeRepository = equipeRepository;
    }

    @Transactional
    public Equipe inserir(Equipe equipe) {
        if (equipe.getNome() == null || equipe.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da equipe deve ser informado.");
        }
        equipe.setNome(equipe.getNome().trim());
        if (equipeRepository.existsByNomeIgnoreCase(equipe.getNome())) {
            throw new IllegalArgumentException("Já existe uma equipe cadastrada com esse nome.");
        }

        if (equipe.getDescricao() != null) {
            equipe.setDescricao(equipe.getDescricao().trim());
        }
        return equipeRepository.save(equipe);
    }

    @Transactional(readOnly = true)
    public List<Equipe> listarTodas(){
        return equipeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Equipe buscarPorId(UUID id){
        return equipeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma equipe encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Equipe> pesquisarPorNome(String nome){
        if (nome == null || nome.trim().length() < 2){
            return List.of();
        }
        return equipeRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
    }

    @Transactional
    public Equipe atualizarEquipe(UUID uuid, Equipe novosDados) {
        Equipe equipeAtual = equipeRepository.findById(uuid).orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma equipe encontrada com o ID: " + uuid));
        updateData(equipeAtual, novosDados);
        return equipeAtual;
    }

    @Transactional
    public void deletar(UUID uuid) {
        Equipe equipe = equipeRepository.findById(uuid).orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma equipe encontrada com o ID: " + uuid));
        if (!equipe.getAgentes().isEmpty()) {
            throw new IllegalArgumentException("Não é possível excluir uma equipe que possui agentes associados.");
        }
        equipeRepository.delete(equipe);
    }

    private void updateData(Equipe atual, Equipe novosDados) {
        if (novosDados.getNome() != null) {
            if (novosDados.getNome().isBlank()) {
                throw new IllegalArgumentException("O nome da equipe não pode ficar vazio.");
            }
            String nome = novosDados.getNome().trim();
            if (equipeRepository.existsByNomeIgnoreCaseAndUuidNot(nome, atual.getUuid())) {
                throw new IllegalArgumentException("Já existe outra equipe com esse nome.");
            }
            atual.setNome(nome);
        }
        if (novosDados.getDescricao() != null) {
            atual.setDescricao(novosDados.getDescricao().trim());
        }
    }
}
