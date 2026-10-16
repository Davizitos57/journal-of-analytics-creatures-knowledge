package com.scarfox.jack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scarfox.jack.domain.DTOs.AssociarEquipeDTO;
import com.scarfox.jack.domain.DTOs.AtualizarAgenteDTO;
import com.scarfox.jack.domain.DTOs.CadastroAgenteDTO;
import com.scarfox.jack.domain.DTOs.RespostaAgenteDTO;
import com.scarfox.jack.domain.entity.Agente;
import com.scarfox.jack.domain.enums.Patente;
import com.scarfox.jack.service.AgenteService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController 
@RequestMapping("/agentes")
public class AgenteController {
    
    private final AgenteService agenteService;

    public AgenteController(AgenteService agenteService){
        this.agenteService = agenteService;
    }

    @PostMapping
    public ResponseEntity<RespostaAgenteDTO> inserir(@Valid @RequestBody CadastroAgenteDTO agenteDTO) {
        Agente agente = agenteService.inserir(agenteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaResposta(agente));
    }

    @GetMapping
    public ResponseEntity<List<RespostaAgenteDTO>> listarTodos() {
        List<RespostaAgenteDTO> agentes = agenteService.listarTodos().stream().map(this::converterParaResposta).toList();
        return ResponseEntity.ok(agentes);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RespostaAgenteDTO> buscarPorID(@PathVariable UUID uuid){
        Agente agente = agenteService.buscarPorId(uuid);
        return ResponseEntity.ok(converterParaResposta(agente));
    }

    @GetMapping("/pesquisar/patente")
    public ResponseEntity<List<RespostaAgenteDTO>> buscarPorPatente(@RequestParam Patente patente) {
        List<RespostaAgenteDTO> agentes = agenteService.buscarPorPatente(patente).stream().map(this::converterParaResposta).toList();
        return ResponseEntity.ok(agentes);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<RespostaAgenteDTO> atualizar(@PathVariable UUID uuid, @Valid @RequestBody AtualizarAgenteDTO agenteDTO){
        Agente agente = agenteService.atualizar(uuid, agenteDTO);
        return ResponseEntity.ok(converterParaResposta(agente));
    }

    @PatchMapping("/{uuidAgente}/equipe")
    public ResponseEntity<Void> associarAgenteEquipe(@PathVariable UUID uuidAgente, @Valid @RequestBody AssociarEquipeDTO dto) {
        agenteService.associarEquipe(uuidAgente, dto.equipeId());
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{uuidAgente}/equipe")
    public ResponseEntity<Void> desvincularEquipe(@PathVariable UUID uuidAgente) {
        agenteService.desvincularEquipe(uuidAgente);            
        return ResponseEntity.noContent().build();        
    }

    @DeleteMapping("/{uuidAgente}")
    public ResponseEntity<Void> deletar(@PathVariable UUID uuidAgente){
        agenteService.deletarAgente(uuidAgente);
        return ResponseEntity.noContent().build();
    }

    private RespostaAgenteDTO converterParaResposta(Agente agente) {
        return new RespostaAgenteDTO(
            agente.getUuid(),
            agente.getNome(),
            agente.getEmail(),
            agente.getNacionalidade(),
            agente.getDataNascimento(),
            agente.getFotoUrl(),
            agente.getPatente(),
            agente.getEquipe() != null ? agente.getEquipe().getUuid() : null,
            agente.getEquipe() != null ? agente.getEquipe().getNome() : "Sem equipe"
        );
    }
    
}
