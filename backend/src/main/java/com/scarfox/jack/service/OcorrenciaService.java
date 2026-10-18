package com.scarfox.jack.service;

import com.scarfox.jack.repository.AgenteRepository;
import com.scarfox.jack.repository.CriaturaRepository;
import com.scarfox.jack.repository.HistoricoOcorrenciaRepository;
import com.scarfox.jack.repository.LocalRepository;

import com.scarfox.jack.repository.RitualRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scarfox.jack.domain.DTOs.AtualizarOcorrenciaDTO;
import com.scarfox.jack.domain.DTOs.CadastroOcorrenciaDTO;
import com.scarfox.jack.domain.entity.Agente;
import com.scarfox.jack.domain.entity.Criatura;
import com.scarfox.jack.domain.entity.HistoricoOcorrencia;
import com.scarfox.jack.domain.entity.Local;
import com.scarfox.jack.domain.entity.Ocorrencia;
import com.scarfox.jack.domain.entity.Ritual;
import com.scarfox.jack.domain.enums.Status;
import com.scarfox.jack.exception.ConflitoEstadoOcorrenciaException;
import com.scarfox.jack.exception.RecursoNaoEncontradoException;
import com.scarfox.jack.repository.OcorrenciaRepository;

@Service 
public class OcorrenciaService {

    private final HistoricoOcorrenciaRepository historicoOcorrenciaRepository;
    private final CriaturaRepository criaturaRepository;
    private final RitualRepository ritualRepository;
    private final LocalRepository localRepository;
    private final AgenteRepository agenteRepository;
    private final OcorrenciaRepository ocorrenciaRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository, AgenteRepository agenteRepository, LocalRepository localRepository, RitualRepository ritualRepository, CriaturaRepository criaturaRepository, HistoricoOcorrenciaRepository historicoOcorrenciaRepository, HistoricoOcorrenciaRepository historicoOcorrenciaRepository_1){
        this.agenteRepository = agenteRepository;
        this.localRepository = localRepository;
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.ritualRepository = ritualRepository;
        this.criaturaRepository = criaturaRepository;
        this.historicoOcorrenciaRepository = historicoOcorrenciaRepository;
    }

    @Transactional 
    public Ocorrencia criar(CadastroOcorrenciaDTO ocorrenciaDTO, String keycloakId){
        if(ocorrenciaDTO == null){
            throw new IllegalArgumentException("Os dados da ocorrencia devem ser informadas.");
        }
        if(ocorrenciaDTO.titulo() == null || ocorrenciaDTO.titulo().isBlank()){
            throw new IllegalArgumentException("O título da ocorrência deve ser informada");
        }   
        if (ocorrenciaDTO.dataHoraOcorrencia() == null) {
            throw new IllegalArgumentException("A data e hora da ocorrência devem ser informadas.");
        }
        if (ocorrenciaDTO.localUUID() == null) {
            throw new IllegalArgumentException("O local da ocorrência deve ser informado.");
        }
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("O identificador do agente autenticado deve ser informado.");
        }

        int numMortos = ocorrenciaDTO.numMortos() == null ? 0 : ocorrenciaDTO.numMortos();
        int numFeridos = ocorrenciaDTO.numFeridos() == null ? 0 : ocorrenciaDTO.numFeridos();

        if (numMortos < 0 || numFeridos < 0) {
            throw new IllegalArgumentException("O número de mortos e feridos não pode ser negativo.");
        }

        Local local = localRepository.findById(ocorrenciaDTO.localUUID())  
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum local encontrado com o ID: " + ocorrenciaDTO.localUUID()));

        Agente agente = agenteRepository.findByKeycloakId(keycloakId)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum agente encontrado para o usuário autenticado."));

        Ocorrencia ocorrencia = new Ocorrencia(
            ocorrenciaDTO.titulo().trim(),
            ocorrenciaDTO.descricao() != null ? ocorrenciaDTO.descricao().trim() : null,
            ocorrenciaDTO.dataHoraOcorrencia(),
            Status.ABERTA,
            numMortos,
            numFeridos,
            agente,
            local
        );

        Ocorrencia salva = ocorrenciaRepository.save(ocorrencia);
        registrarAlteracao(salva, agente, "criacao", null, "Ocorrência registrada");
        return salva;
    }

    @Transactional(readOnly = true)
    public List<Ocorrencia> listarTodos(){
        return ocorrenciaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ocorrencia buscarPorID(UUID uuid){
        return ocorrenciaRepository.findById(uuid)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma ocorrência encontrada com o ID: " + uuid));
    }

    @Transactional(readOnly = true)
    public List<Ocorrencia> buscarPorStatus(Status status){
        return ocorrenciaRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Ocorrencia> buscarPorAgenteRegistrador(UUID uuidAgenteREgistrador){
        return ocorrenciaRepository.findByAgenteRegistradorUuid(uuidAgenteREgistrador);
    }

    @Transactional
    public Ocorrencia atualizar(UUID uuid, AtualizarOcorrenciaDTO dto, String keycloakId) {
        if (dto == null) {
            throw new IllegalArgumentException("Os dados da atualização devem ser informados.");
        }
        Ocorrencia ocorrencia = buscarPorID(uuid);

        if (ocorrencia.getStatus() == Status.ARQUIVADA) {
            boolean somenteReabertura =
                    dto.informouStatus()
                    && dto.getStatus() == Status.ABERTA
                    && !dto.informouTitulo()
                    && !dto.informouDescricao()
                    && !dto.informouDataHoraOcorrencia()
                    && !dto.informouLocal()
                    && !dto.informouNumMortos()
                    && !dto.informouNumFeridos();

            if (!somenteReabertura) {
                throw new ConflitoEstadoOcorrenciaException("A ocorrência está arquivada. Somente sua reabertura é permitida.");
            }
        }

        Agente agente = buscarAgenteAutenticado(keycloakId);

        String tituloAnterior = ocorrencia.getTitulo();
        String descricaoAnterior = ocorrencia.getDescricao();
        LocalDateTime dataAnterior = ocorrencia.getDataHoraOcorrencia();
        UUID localAnterior = ocorrencia.getLocal().getUuid();
        Status statusAnterior = ocorrencia.getStatus();
        Integer mortosAnterior = ocorrencia.getNumMortos();
        Integer feridosAnterior = ocorrencia.getNumFeridos();

        updateData(ocorrencia, dto);

        registrarSeAlterado(ocorrencia, agente, "titulo", tituloAnterior, ocorrencia.getTitulo());
        registrarSeAlterado(ocorrencia, agente, "descricao", descricaoAnterior, ocorrencia.getDescricao());
        registrarSeAlterado(ocorrencia, agente, "dataHoraOcorrencia", dataAnterior, ocorrencia.getDataHoraOcorrencia());
        registrarSeAlterado(ocorrencia, agente, "local", localAnterior, ocorrencia.getLocal().getUuid());
        registrarSeAlterado(ocorrencia, agente, "status", statusAnterior, ocorrencia.getStatus());
        registrarSeAlterado(ocorrencia, agente, "numMortos", mortosAnterior, ocorrencia.getNumMortos());
        registrarSeAlterado(ocorrencia, agente, "numFeridos", feridosAnterior, ocorrencia.getNumFeridos());

        return ocorrencia;
    }

    @Transactional
    public void associarCriatura(UUID uuidOcorrencia, UUID uuidCriatura, String keycloakId) {
        Ocorrencia ocorrencia = buscarPorID(uuidOcorrencia);
        validarEdicao(ocorrencia);
        Criatura criatura = criaturaRepository.findById(uuidCriatura)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma criatura encontrada com o ID: " + uuidCriatura));

        if (ocorrencia.getCriaturas().add(criatura)) {
            Agente agente = buscarAgenteAutenticado(keycloakId);
            registrarAlteracao(ocorrencia, agente, "criatura", null, criatura.getNome() + " (" + criatura.getUuid() + ")"
            );
        }
    }

    @Transactional
    public void desassociarCriatura(UUID uuidOcorrencia, UUID uuidCriatura, String keycloakId) {
        Ocorrencia ocorrencia = buscarPorID(uuidOcorrencia);
        validarEdicao(ocorrencia);
        Criatura criatura = criaturaRepository.findById(uuidCriatura)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma criatura encontrada com o ID: " + uuidCriatura));

        if (ocorrencia.getCriaturas().remove(criatura)) {
            Agente agente = buscarAgenteAutenticado(keycloakId);
            registrarAlteracao(ocorrencia, agente, "criatura", criatura.getNome() + " (" + criatura.getUuid() + ")", null);
        }
    }

    @Transactional
    public void associarRitual(UUID uuidOcorrencia, UUID uuidRitual, String keycloakId) {
        Ocorrencia ocorrencia = buscarPorID(uuidOcorrencia);
        validarEdicao(ocorrencia);
        Ritual ritual = ritualRepository.findById(uuidRitual)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum ritual encontrado com o ID: " + uuidRitual));

        if (ocorrencia.getRituais().add(ritual)) {
            Agente agente = buscarAgenteAutenticado(keycloakId);
            registrarAlteracao(ocorrencia, agente, "ritual", null, ritual.getNome() + " (" + ritual.getUuid() + ")");
        }
    }

    @Transactional
    public void desassociarRitual(UUID uuidOcorrencia, UUID uuidRitual, String keycloakId) {
        Ocorrencia ocorrencia = buscarPorID(uuidOcorrencia);
        validarEdicao(ocorrencia);
        Ritual ritual = ritualRepository.findById(uuidRitual)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum ritual encontrado com o ID: " + uuidRitual));

        if (ocorrencia.getRituais().remove(ritual)) {
            Agente agente = buscarAgenteAutenticado(keycloakId);
            registrarAlteracao(ocorrencia, agente, "ritual", ritual.getNome() + " (" + ritual.getUuid() + ")", null);
        }
    }

    @Transactional(readOnly = true)
    public List<HistoricoOcorrencia> listarHistorico(UUID ocorrenciaUUID) {
        if (!ocorrenciaRepository.existsById(ocorrenciaUUID)) {
            throw new RecursoNaoEncontradoException("Nenhuma ocorrência encontrada com o ID: " + ocorrenciaUUID);
        }
        return historicoOcorrenciaRepository.findByOcorrencia_UuidOrderByDataHoraDesc(ocorrenciaUUID);
    }

    private void validarEdicao(Ocorrencia ocorrencia) {
        if (ocorrencia.getStatus() == Status.ARQUIVADA) {
            throw new ConflitoEstadoOcorrenciaException("Não é possível modificar uma ocorrência arquivada. Reabra a ocorrência primeiro.");
        }
    }

    private void updateData(Ocorrencia ocorrencia, AtualizarOcorrenciaDTO dto) {
        if (dto.informouTitulo()) {
            if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
                throw new IllegalArgumentException("O título da ocorrência deve ser informado.");
            }
            ocorrencia.setTitulo(dto.getTitulo().trim());
        }
        if (dto.informouDescricao()) {
            ocorrencia.setDescricao(dto.getDescricao() == null ? null : dto.getDescricao().trim());
        }
        if (dto.informouDataHoraOcorrencia()) {
            if (dto.getDataHoraOcorrencia() == null) {
                throw new IllegalArgumentException("A data e hora da ocorrência devem ser informadas.");
            }
            ocorrencia.setDataHoraOcorrencia(dto.getDataHoraOcorrencia());
        }
        if (dto.informouLocal()) {
            if (dto.getLocalUUID() == null) {
                throw new IllegalArgumentException("O local da ocorrência deve ser informado.");
            }
            Local local = localRepository.findById(dto.getLocalUUID())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum local encontrado com o ID: " + dto.getLocalUUID()));
            ocorrencia.setLocal(local);
        }
        if (dto.informouStatus()) {
            if (dto.getStatus() == null) {
                throw new IllegalArgumentException("O status da ocorrência deve ser informado.");
            }
            ocorrencia.setStatus(dto.getStatus());
        }
        if (dto.informouNumMortos()) {
            if (dto.getNumMortos() == null || dto.getNumMortos() < 0) {
                throw new IllegalArgumentException("O número de mortos deve ser maior ou igual a zero.");
            }
            ocorrencia.setNumMortos(dto.getNumMortos());
        }
        if (dto.informouNumFeridos()) {
            if (dto.getNumFeridos() == null || dto.getNumFeridos() < 0) {
                throw new IllegalArgumentException("O número de feridos deve ser maior ou igual a zero.");
            }
            ocorrencia.setNumFeridos(dto.getNumFeridos());
        }
    }

    private Agente buscarAgenteAutenticado(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("O identificador do agente deve ser informado.");
        }
        return agenteRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum agente encontrado para o usuário autenticado."));
    }

    private void registrarAlteracao(Ocorrencia ocorrencia, Agente agente, String campo, String valorAnterior, String valorNovo) {
        HistoricoOcorrencia historico = new HistoricoOcorrencia(campo, valorAnterior, valorNovo, agente, ocorrencia);
        historicoOcorrenciaRepository.save(historico);
    }   
    
    private void registrarSeAlterado(Ocorrencia ocorrencia, Agente agente, String campo, Object valorAnterior, Object valorNovo) {
        if (Objects.equals(valorAnterior, valorNovo)) {
            return;
        }
        registrarAlteracao(ocorrencia, agente, campo, Objects.toString(valorAnterior, null), Objects.toString(valorNovo, null)
        );
    }
}
