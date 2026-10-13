package com.scarfox.jack.service;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.scarfox.jack.domain.DTOs.CadastroAgenteDTO;
import com.scarfox.jack.domain.entity.Agente;
import com.scarfox.jack.domain.enums.Patente;
import com.scarfox.jack.repository.AgenteRepository;

@Service 
public class AgenteService {
    
    private final AgenteRepository agenteRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final TransactionTemplate transactionTemplate;

    public AgenteService(AgenteRepository agenteRepository, KeycloakAdminService keycloakAdminService, PlatformTransactionManager transactionManager) {
        this.agenteRepository = agenteRepository;
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


}
