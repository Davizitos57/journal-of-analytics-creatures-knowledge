package com.scarfox.jack.service;

import com.scarfox.jack.domain.DTOs.AtualizarEquipeDTO;
import com.scarfox.jack.domain.DTOs.CadastroEquipeDTO;
import com.scarfox.jack.domain.entity.Agente;
import com.scarfox.jack.domain.entity.Equipe;
import com.scarfox.jack.exception.RecursoNaoEncontradoException;
import com.scarfox.jack.repository.AgenteRepository;
import com.scarfox.jack.repository.EquipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EquipeService {

    private final AgenteRepository agenteRepository;
    private final EquipeRepository equipeRepository;

    public EquipeService(EquipeRepository equipeRepository, AgenteRepository agenteRepository){
        this.equipeRepository = equipeRepository;
        this.agenteRepository = agenteRepository;
    }

    @Transactional
    public Equipe inserir(CadastroEquipeDTO equipeDTO) {
        if (equipeDTO == null || equipeDTO.nome() == null || equipeDTO.nome().isBlank()) {
            throw new IllegalArgumentException("O nome da equipe deve ser informado.");
        }
        String nome = equipeDTO.nome().trim();

        if (equipeRepository.existsByNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Já existe uma equipe cadastrada com esse nome.");
        }

        String descricao = equipeDTO.descricao() != null ? equipeDTO.descricao().trim() : null;
        Equipe equipe = new Equipe(nome, descricao);
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
    public Equipe atualizarEquipe(UUID uuid, AtualizarEquipeDTO equipeDTO) {
        if (equipeDTO == null) {
            throw new IllegalArgumentException("Os dados da equipe devem ser informados.");
        }

        Equipe equipeAtual = equipeRepository.findById(uuid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma equipe encontrada com o ID: " + uuid));

        updateData(equipeAtual, equipeDTO);
        return equipeAtual;
    }

    @Transactional(readOnly = true)
    public List<Agente> listarAgentesDaEquipe(UUID equipeUUID) {
        if (!equipeRepository.existsById(equipeUUID)) {
            throw new RecursoNaoEncontradoException("Nenhuma equipe encontrada com o ID: " + equipeUUID);
        }
        return agenteRepository.findByEquipeUuid(equipeUUID);
    }

    @Transactional(readOnly = true)
    public long contarAgentes(UUID equipeUUID) {
        return agenteRepository.countByEquipeUuid(equipeUUID);
    }

    @Transactional
    public void deletar(UUID uuid) {
        Equipe equipe = equipeRepository.findById(uuid).orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma equipe encontrada com o ID: " + uuid));
        if (!equipe.getAgentes().isEmpty()) {
            throw new IllegalArgumentException("Não é possível excluir uma equipe que possui agentes associados.");
        }
        equipeRepository.delete(equipe);
    }

    private void updateData(Equipe atual, AtualizarEquipeDTO equipeDTO) {
        if (equipeDTO.informouNome()) {
            if (equipeDTO.getNome() == null || equipeDTO.getNome().isBlank()) {
                throw new IllegalArgumentException("O nome da equipe não pode ficar vazio.");
            }

            String nome = equipeDTO.getNome().trim();

            if (equipeRepository.existsByNomeIgnoreCaseAndUuidNot(nome, atual.getUuid())) {
                throw new IllegalArgumentException("Já existe outra equipe com esse nome.");
            }
            atual.setNome(nome);
        }

        if (equipeDTO.informouDescricao()) {
            atual.setDescricao(equipeDTO.getDescricao() == null ? null : equipeDTO.getDescricao().trim());
        }
    }
}
