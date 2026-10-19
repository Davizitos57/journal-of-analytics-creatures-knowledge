package com.scarfox.jack.service;

import com.scarfox.jack.repository.EquipeRepository;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.scarfox.jack.domain.DTOs.AtualizarAgenteDTO;
import com.scarfox.jack.domain.DTOs.CadastroAgenteDTO;
import com.scarfox.jack.domain.entity.Agente;
import com.scarfox.jack.domain.entity.Equipe;
import com.scarfox.jack.domain.enums.Patente;
import com.scarfox.jack.exception.RecursoNaoEncontradoException;
import com.scarfox.jack.repository.AgenteRepository;

@Service 
public class AgenteService {
    
    private final EquipeRepository equipeRepository;
    private final AgenteRepository agenteRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final TransactionTemplate transactionTemplate;

    public AgenteService(AgenteRepository agenteRepository, KeycloakAdminService keycloakAdminService, PlatformTransactionManager transactionManager, EquipeRepository equipeRepository) {
        this.agenteRepository = agenteRepository;
        this.equipeRepository = equipeRepository;
        this.keycloakAdminService = keycloakAdminService;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public Agente inserir(CadastroAgenteDTO agenteDTO) {
        if (agenteDTO == null) {
            throw new IllegalArgumentException("Os dados do agente devem ser informados.");
        }
        if (agenteDTO.nome() == null || agenteDTO.nome().isBlank()) {
            throw new IllegalArgumentException("O nome do agente deve ser informado.");
        }
        if (agenteDTO.email() == null || agenteDTO.email().isBlank()) {
            throw new IllegalArgumentException("O email do agente deve ser informado.");
        }
        if (agenteDTO.senha() == null || agenteDTO.senha().isBlank()) {
            throw new IllegalArgumentException("A senha do agente deve ser informada.");
        }

        String nome = agenteDTO.nome().trim();
        String email = agenteDTO.email().trim().toLowerCase(Locale.ROOT);

        if (agenteRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Já existe um agente cadastrado com esse email.");
        }

        String keycloakId = keycloakAdminService.criarUsuario(email, agenteDTO.senha());

        try {
            Agente agente = new Agente(nome, email, agenteDTO.nacionalidade(), agenteDTO.dataNascimento(), Patente.RECRUTA);

            agente.setFotoUrl(agenteDTO.fotoUrl());
            agente.setKeycloakId(keycloakId);

            return transactionTemplate.execute(status -> agenteRepository.saveAndFlush(agente));
        } 
        catch (RuntimeException erro) {
            try {
                keycloakAdminService.excluirUsuario(keycloakId);
            } 
            catch (RuntimeException erroExclusao) {
                erro.addSuppressed(erroExclusao);
            }
            throw erro;
        }
    }

    @Transactional(readOnly = true)
    public List<Agente> listarTodos(){
        return agenteRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Agente buscarPorId(UUID id){
        return agenteRepository.findByUuidAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum agente encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Agente> buscarPorPatente(Patente patente){
        return agenteRepository.findByPatenteAndAtivoTrue(patente); 
    }

    @Transactional
    public Agente atualizar(UUID agenteUUID, AtualizarAgenteDTO agenteDTO) {
        if (agenteDTO == null) {
            throw new IllegalArgumentException("Os dados do agente devem ser informados.");
        }
        Agente agente = agenteRepository.findByUuidAndAtivoTrue(agenteUUID)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum agente encontrado com o ID: " + agenteUUID));

        atualizarDados(agente, agenteDTO);
        return agente;
    }

    @Transactional 
    public Agente associarEquipe(UUID agenteUUID, UUID equipeUUID){
        Agente agente = agenteRepository.findByUuidAndAtivoTrue(agenteUUID)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum agente encontrado com o ID: " + agenteUUID));
        Equipe equipe = equipeRepository.findById(equipeUUID) 
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma equipe encontrada com o ID: " + equipeUUID));

        if (agente.getEquipe() != null && agente.getEquipe().getUuid().equals(equipeUUID)) {
            return agente;
        }

        agente.setEquipe(equipe);
        return agente; 
    }

    @Transactional
    public void desvincularEquipe(UUID agenteUUID){
        Agente agente = agenteRepository.findByUuidAndAtivoTrue(agenteUUID)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum agente encontrado com o ID: " + agenteUUID));
        agente.setEquipe(null);
    }

    @Transactional 
    public void reativarAgente(UUID agenteUUID){
        Agente agente = agenteRepository.findById(agenteUUID)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum agente encontrado com o ID: " + agenteUUID));
        if (agente.isAtivo()) {
            return;
        }
        if (agente.getKeycloakId() == null) {
            throw new IllegalStateException("O agente não possui vínculo com o Keycloak.");
        }
        keycloakAdminService.alterarStatusUsuario(agente.getKeycloakId(), true);
        agente.reativar();
    }
    
    @Transactional 
    public void desativarAgente(UUID agenteUUID){
        Agente agente = agenteRepository.findById(agenteUUID)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum agente encontrado com o ID: " + agenteUUID));
        if (!agente.isAtivo()) {
            return;
        }
        if (agente.getKeycloakId() == null) {
            throw new IllegalStateException("O agente não possui vínculo com o Keycloak.");
        }
        keycloakAdminService.alterarStatusUsuario(agente.getKeycloakId(), false);
        agente.desativar();
    }

    private void atualizarDados(Agente agente, AtualizarAgenteDTO agenteDTO) {
        if (agenteDTO.informouNome()) {
            if (agenteDTO.getNome() == null || agenteDTO.getNome().isBlank()) {
                throw new IllegalArgumentException("O nome do agente não pode estar vazio.");
            }
            agente.setNome(agenteDTO.getNome().trim());
        }
        if (agenteDTO.informouNacionalidade()) {
            agente.setNacionalidade(agenteDTO.getNacionalidade() == null ? null : agenteDTO.getNacionalidade().trim());
        }
        if (agenteDTO.informouDataNascimento()) {
            agente.setDataNascimento(agenteDTO.getDataNascimento());
        }
        if (agenteDTO.informouFotoUrl()) {
            agente.setFotoUrl(agenteDTO.getFotoUrl() == null ? null : agenteDTO.getFotoUrl().trim());
        }
    }
}
