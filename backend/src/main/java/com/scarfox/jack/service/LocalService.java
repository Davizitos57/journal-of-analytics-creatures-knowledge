package com.scarfox.jack.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scarfox.jack.domain.entity.Local;
import com.scarfox.jack.exception.RecursoNaoEncontradoException;
import com.scarfox.jack.repository.LocalRepository;

@Service
public class LocalService {
    
    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository){
        this.localRepository = localRepository;
    }

    @Transactional
    public Local inserir(Local local) {
        if (local.getNome() == null || local.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do local deve ser informado.");
        }
        local.setNome(local.getNome().trim());

        if (local.getRua() != null) {
            local.setRua(local.getRua().trim());
        }
        if (local.getBairro() != null) {
            local.setBairro(local.getBairro().trim());
        }
        if (local.getCidade() != null) {
            local.setCidade(local.getCidade().trim());
        }
        if (local.getEstadoProvincia() != null) {
            local.setEstadoProvincia(local.getEstadoProvincia().trim());
        }
        if (local.getPais() == null || local.getPais().isBlank()) {
            throw new IllegalArgumentException("O país deve ser informado.");
        }
        local.setPais(local.getPais().trim());

        return localRepository.save(local);
    }

    @Transactional(readOnly = true)
    public List<Local> listarTodos() {
        return localRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Local> pesquisarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return List.of();
        }
        return localRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
    }

    @Transactional(readOnly=true)
    public List<String> listarPaises(){
        return localRepository.findAllByOrderByPaisAsc()
            .stream()
            .map(Local::getPais)
            .distinct()
            .toList();
    }

    @Transactional(readOnly=true)
    public List<String> listarEstados(String pais) {
        return localRepository.findByPaisIgnoreCaseOrderByEstadoProvinciaAsc(pais)
            .stream()
            .map(Local::getEstadoProvincia)
            .distinct()
            .toList();
    }

    @Transactional(readOnly=true)
    public List<String> listarCidades(String pais, String estadoProvincia) {
        return localRepository.findByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseOrderByCidadeAsc(pais, estadoProvincia)
            .stream()
            .map(Local::getCidade)
            .distinct()
            .toList();
    }

    @Transactional(readOnly=true)
        public List<String> listarBairros(String pais, String estadoProvincia, String cidade) {
        return localRepository.findByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseAndCidadeIgnoreCaseOrderByBairroAsc(pais, estadoProvincia, cidade)
            .stream()
            .map(Local::getBairro)
            .distinct()
            .toList();
    }

    @Transactional(readOnly=true)
        public List<String> listarRuas(String pais, String estadoProvincia, String cidade, String bairro) {
        return localRepository.findByPaisIgnoreCaseAndEstadoProvinciaIgnoreCaseAndCidadeIgnoreCaseAndBairroIgnoreCaseOrderByRuaAsc(pais, estadoProvincia, cidade, bairro)
            .stream()
            .map(Local::getRua)
            .distinct()
            .toList();
    }

    @Transactional(readOnly=true)
    public Local buscarPorID(UUID uuid){
        return localRepository.findById(uuid)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum local encontrado com o ID: " + uuid));
    }

    @Transactional
    public Local atualizarLocal(UUID uuid, Local novosDados){
        Local localAtual = localRepository.findById(uuid)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum local encontrado com o ID: " + uuid));
        updateData(localAtual, novosDados);
        return localAtual;
    }

    @Transactional
    public void deletar(UUID uuid){
        Local local = localRepository.findById(uuid)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum local encontrado com o ID: " + uuid));
        localRepository.delete(local);
    }

    private void updateData(Local localAtual, Local local) {
        if (local.getNome() != null) {
            if(local.getNome().isBlank()){
                throw new IllegalArgumentException("O nome do local não pode ficar vazio");
            }
            localAtual.setNome(local.getNome().trim());
        }
        if (local.getRua() != null) {
            localAtual.setRua(local.getRua().trim());
        }
        if (local.getBairro() != null) {
            localAtual.setBairro(local.getBairro().trim());
        }
        if (local.getCidade() != null) {
            localAtual.setCidade(local.getCidade().trim());
        }
        if (local.getEstadoProvincia() != null) {
            localAtual.setEstadoProvincia(local.getEstadoProvincia().trim());
        }
        if (local.getPais() != null) {
            if(local.getPais().isBlank()){
                throw new IllegalArgumentException("O pais não pode ficar vazio");
            }
            localAtual.setPais(local.getPais().trim());
        }
        if (local.getCodigoPostal() != null) {
            localAtual.setCodigoPostal(local.getCodigoPostal().trim());
        }
    }
}
