package com.scarfox.jack.controller;

import com.scarfox.jack.domain.DTOs.AtualizarEquipeDTO;
import com.scarfox.jack.domain.DTOs.CadastroEquipeDTO;
import com.scarfox.jack.domain.DTOs.RespostaAgenteDTO;
import com.scarfox.jack.domain.DTOs.RespostaEquipeDTO;
import com.scarfox.jack.domain.entity.Agente;
import com.scarfox.jack.domain.entity.Equipe;
import com.scarfox.jack.service.EquipeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @PostMapping
    public ResponseEntity<RespostaEquipeDTO> criar(@Valid @RequestBody CadastroEquipeDTO equipeDTO) {
        Equipe equipe = equipeService.inserir(equipeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaResposta(equipe));
    }

    @GetMapping
    public ResponseEntity<List<RespostaEquipeDTO>> listarTodas() {
        List<RespostaEquipeDTO> equipes = equipeService.listarTodas().stream().map(this::converterParaResposta).toList();
        return ResponseEntity.ok(equipes);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RespostaEquipeDTO> buscarPorID(@PathVariable UUID uuid) {
        Equipe equipe = equipeService.buscarPorId(uuid);
        return ResponseEntity.ok(converterParaResposta(equipe));
    }

    @GetMapping("/pesquisar/nome")
    public ResponseEntity<List<RespostaEquipeDTO>> pesquisarPorNome(@RequestParam String nome) {
        List<RespostaEquipeDTO> equipes = equipeService.pesquisarPorNome(nome).stream().map(this::converterParaResposta).toList();
        return ResponseEntity.ok(equipes);
    }

    @GetMapping("/{uuidEquipe}/agentes")
    public ResponseEntity<List<RespostaAgenteDTO>> verAgentesEquipe(@PathVariable UUID uuidEquipe) {
        List<RespostaAgenteDTO> agentes = equipeService.listarAgentesDaEquipe(uuidEquipe).stream().map(this::converterAgenteParaResposta).toList();
        return ResponseEntity.ok(agentes);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<RespostaEquipeDTO> atualizarEquipe(@PathVariable UUID uuid, @Valid @RequestBody AtualizarEquipeDTO equipeDTO) {
        Equipe equipe = equipeService.atualizarEquipe(uuid, equipeDTO);
        return ResponseEntity.ok(converterParaResposta(equipe));
    }

    @PatchMapping("/{uuid}/reativar")
    public ResponseEntity<Void> reativarEquipe(@PathVariable UUID uuid) {
        equipeService.reativarEquipe(uuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletarEquipe(@PathVariable UUID uuid){
        equipeService.desativar(uuid);
        return ResponseEntity.noContent().build();
    }

    private RespostaEquipeDTO converterParaResposta(Equipe equipe) {
        return new RespostaEquipeDTO(
            equipe.getUuid(),
            equipe.getNome(),
            equipe.getDescricao(),
            Math.toIntExact(equipeService.contarAgentes(equipe.getUuid()))
        );
    }

    private RespostaAgenteDTO converterAgenteParaResposta(Agente agente) {
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
