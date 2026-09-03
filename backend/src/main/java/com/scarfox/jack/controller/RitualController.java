package com.scarfox.jack.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scarfox.jack.domain.entity.Ritual;
import com.scarfox.jack.domain.enums.Elemento;
import com.scarfox.jack.service.RitualService;

@RestController
@RequestMapping("/rituais")
public class RitualController {

    private final RitualService ritualService;

    public RitualController(RitualService ritualService){
        this.ritualService = ritualService;
    }

    @PostMapping
    public ResponseEntity<Ritual> adicionarRitual(@RequestBody Ritual ritual){
        Ritual ritualInserido = ritualService.inserir(ritual);
        return ResponseEntity.status(HttpStatus.CREATED).body(ritualInserido);
    }

    @GetMapping
    public ResponseEntity<List<Ritual>> buscarTodos(@RequestParam(required=false) String nome, @RequestParam(required=false) Elemento elemento){
        List<Ritual> rituais = ritualService.listarTodos(nome, elemento);
        return ResponseEntity.ok(rituais);
    }
    
    @GetMapping("/nome")
    public ResponseEntity<Ritual> buscarPorNome(@RequestParam String nome){
        return ResponseEntity.ok(ritualService.buscarPorNome(nome));
    }

    @GetMapping("/pesquisar/nome")
    public ResponseEntity<List<Ritual>> pesquisarPorNome(@RequestParam String termo){
        return ResponseEntity.ok(ritualService.pesquisarPorNome(termo));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Ritual> buscarPorID(@PathVariable UUID uuid){
        return ResponseEntity.ok(ritualService.buscarPorId(uuid));
    }

    @GetMapping("/pesquisar/elemento")
    public ResponseEntity<List<Ritual>> pesquisarPorElemento(@RequestParam Elemento elemento){
        return ResponseEntity.ok(ritualService.buscarPorElemento(elemento));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Ritual> atualizarRitual(@PathVariable UUID uuid, @RequestBody Ritual ritualAtualizado){
        return ResponseEntity.ok(ritualService.atualizarRitual(uuid, ritualAtualizado));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletarRitual(@PathVariable UUID uuid){
        ritualService.deletar(uuid);
        return ResponseEntity.noContent().build();
    }
}
