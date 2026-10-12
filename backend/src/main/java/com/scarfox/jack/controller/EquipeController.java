package com.scarfox.jack.controller;

import com.scarfox.jack.domain.entity.Equipe;
import com.scarfox.jack.service.EquipeService;
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
    public ResponseEntity<Equipe> criar(@RequestBody Equipe equipe) {
        Equipe equipeInserida = equipeService.inserir(equipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipeInserida);
    }

    @GetMapping
    public ResponseEntity<List<Equipe>> listarTodas(){
        return ResponseEntity.ok(equipeService.listarTodas());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Equipe> buscarPorID(@PathVariable UUID uuid){
        return ResponseEntity.ok(equipeService.buscarPorId(uuid));
    }

    @GetMapping("/pesquisar/nome")
    public ResponseEntity<List<Equipe>> pesquisarPorNome(@RequestParam String nome){
        return ResponseEntity.ok(equipeService.pesquisarPorNome(nome));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Equipe> atualizarEquipe(@PathVariable UUID uuid, @RequestBody Equipe equipe){
        return ResponseEntity.ok(equipeService.atualizarEquipe(uuid, equipe));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletarEquipe(@PathVariable UUID uuid){
        equipeService.deletar(uuid);
        return ResponseEntity.noContent().build();
    }
}
