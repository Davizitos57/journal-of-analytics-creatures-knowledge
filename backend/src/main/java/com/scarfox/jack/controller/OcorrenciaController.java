package com.scarfox.jack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scarfox.jack.domain.DTOs.AtualizarOcorrenciaDTO;
import com.scarfox.jack.domain.DTOs.CadastroOcorrenciaDTO;
import com.scarfox.jack.domain.DTOs.RespostaOcorrenciaDTO;
import com.scarfox.jack.domain.DTOs.ResumoEntidadeDTO;
import com.scarfox.jack.domain.entity.Ocorrencia;
import com.scarfox.jack.domain.enums.Status;
import com.scarfox.jack.service.OcorrenciaService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController 
@RequestMapping("/ocorrencias")
public class OcorrenciaController {
    
    private final OcorrenciaService ocorrenciaService;

    public OcorrenciaController(OcorrenciaService ocorrenciaService){
        this.ocorrenciaService = ocorrenciaService;
    }

    @PostMapping 
    public ResponseEntity<RespostaOcorrenciaDTO> criar(@Valid @RequestBody CadastroOcorrenciaDTO ocorrenciaDTO, @AuthenticationPrincipal Jwt jwt){
        Ocorrencia ocorrencia = ocorrenciaService.criar(ocorrenciaDTO, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaResposta(ocorrencia));
    }

    @GetMapping
    public ResponseEntity<List<RespostaOcorrenciaDTO>> listarTodos(){
        List<Ocorrencia> ocorrencias = ocorrenciaService.listarTodos();
        List<RespostaOcorrenciaDTO> resposta = ocorrencias.stream().map(this::converterParaResposta).toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RespostaOcorrenciaDTO> buscarPorID(@PathVariable UUID uuid){
        Ocorrencia ocorrencia = ocorrenciaService.buscarPorID(uuid);
        return ResponseEntity.ok(converterParaResposta(ocorrencia));
    }

    @GetMapping("/pesquisar/status")
    public ResponseEntity<List<RespostaOcorrenciaDTO>> buscarPorStatus(@RequestParam Status status) {
        List<Ocorrencia> ocorrencias = ocorrenciaService.buscarPorStatus(status);
        List<RespostaOcorrenciaDTO> resposta = ocorrencias.stream().map(this::converterParaResposta).toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/pesquisar/{UUIDagente}/agente")
    public ResponseEntity<List<RespostaOcorrenciaDTO>> buscarPorAgente(@PathVariable UUID UUIDagente) {
        List<Ocorrencia> ocorrencias = ocorrenciaService.buscarPorAgenteRegistrador(UUIDagente);
        List<RespostaOcorrenciaDTO> resposta = ocorrencias.stream().map(this::converterParaResposta).toList();
        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{UUIDOcorrencias}")
    public ResponseEntity<RespostaOcorrenciaDTO> atualizar(@PathVariable UUID UUIDOcorrencias, @Valid @RequestBody AtualizarOcorrenciaDTO ocorrenciaDTO){
        Ocorrencia ocorrencia = ocorrenciaService.atualizar(UUIDOcorrencias, ocorrenciaDTO);
        return ResponseEntity.ok(converterParaResposta(ocorrencia));
    }

    @PostMapping("/{uuidOcorrencia}/criaturas/{uuidCriatura}")
    public ResponseEntity<Void> associarCriatura(@PathVariable UUID uuidOcorrencia, @PathVariable UUID uuidCriatura) {
        ocorrenciaService.associarCriatura(uuidOcorrencia, uuidCriatura);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{uuidOcorrencia}/criaturas/{uuidCriatura}")
    public ResponseEntity<Void> desassociarCriatura(@PathVariable UUID uuidOcorrencia, @PathVariable UUID uuidCriatura) {
        ocorrenciaService.desassociarCriatura(uuidOcorrencia, uuidCriatura);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuidOcorrencia}/rituais/{uuidRitual}")
    public ResponseEntity<Void> associarRitual(@PathVariable UUID uuidOcorrencia,@PathVariable UUID uuidRitual) {
        ocorrenciaService.associarRitual(uuidOcorrencia, uuidRitual);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{uuidOcorrencia}/rituais/{uuidRitual}")
    public ResponseEntity<Void> desassociarRitual(@PathVariable UUID uuidOcorrencia, @PathVariable UUID uuidRitual) {
        ocorrenciaService.desassociarRitual(uuidOcorrencia, uuidRitual);
        return ResponseEntity.noContent().build();
    }

    private RespostaOcorrenciaDTO converterParaResposta(Ocorrencia ocorrencia) {
        Set<ResumoEntidadeDTO> criaturas = ocorrencia.getCriaturas().stream()
                .map(criatura -> new ResumoEntidadeDTO(criatura.getUuid(), criatura.getNome()))
                .collect(Collectors.toSet());

        Set<ResumoEntidadeDTO> rituais = ocorrencia.getRituais().stream()
                .map(ritual -> new ResumoEntidadeDTO(ritual.getUuid(), ritual.getNome()))
                .collect(Collectors.toSet());

        return new RespostaOcorrenciaDTO(
                ocorrencia.getUuid(),
                ocorrencia.getTitulo(),
                ocorrencia.getDescricao(),
                ocorrencia.getDataHoraOcorrencia(),
                ocorrencia.getDataHoraRegistro(),
                ocorrencia.getStatus(),
                ocorrencia.getNumMortos(),
                ocorrencia.getNumFeridos(),
                ocorrencia.getAgenteRegistrador().getUuid(),
                ocorrencia.getAgenteRegistrador().getNome(),
                ocorrencia.getLocal().getUuid(),
                ocorrencia.getLocal().getNome(),
                criaturas,
                rituais
        );
    }
    
}
