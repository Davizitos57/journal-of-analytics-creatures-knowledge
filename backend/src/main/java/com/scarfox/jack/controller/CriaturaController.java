package com.scarfox.jack.controller;

import com.scarfox.jack.domain.DTOs.AtualizarCriaturaDTO;
import com.scarfox.jack.domain.entity.Criatura;
import com.scarfox.jack.domain.enums.Elemento;
import com.scarfox.jack.domain.enums.Tamanho;
import com.scarfox.jack.service.CriaturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/criaturas")
public class CriaturaController {

    private final CriaturaService criaturaService;

    public CriaturaController(CriaturaService criaturaService){
        this.criaturaService = criaturaService;
    }

    @PostMapping
    public ResponseEntity<Criatura> adicionarCriatura(@RequestBody Criatura criatura){
        Criatura criaturaInserida = criaturaService.inserir(criatura);
        return ResponseEntity.status(HttpStatus.CREATED).body(criaturaInserida);
    }

    @GetMapping
    public ResponseEntity<List<Criatura>> buscarTodos(@RequestParam(required = false) String nome, @RequestParam(required = false) Tamanho tamanho, @RequestParam(required = false) Elemento elementoPrincipal, @RequestParam(required = false) Elemento elementoSecundario) {
        return ResponseEntity.ok(criaturaService.listarTodos(nome, tamanho, elementoPrincipal, elementoSecundario));
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Criatura>> buscarPorNome(@RequestParam String nome){
        return ResponseEntity.ok(criaturaService.buscarPorNome(nome));
    }

    @GetMapping("/pesquisar/nome")
    public ResponseEntity<List<Criatura>> pesquisarPorNome(@RequestParam String termo){
        return ResponseEntity.ok(criaturaService.pesquisarPorNome(termo));
    }

    @GetMapping("/pesquisar/tamanho")
    public ResponseEntity<List<Criatura>> pesquisarPorTamanho(@RequestParam Tamanho tamanho){
        return ResponseEntity.ok(criaturaService.buscarPorTamanho(tamanho));
    }

    @GetMapping("/pesquisar/principal/elemento")
    public ResponseEntity<List<Criatura>> pesquisarPorElemento(@RequestParam Elemento elemento){
        return ResponseEntity.ok(criaturaService.buscarPorElementoPrincipal(elemento));
    }

    @GetMapping("/pesquisar/secundario/elemento")
    public ResponseEntity<List<Criatura>> pesquisarPorElementoSecundario(@RequestParam Elemento elemento){
        return ResponseEntity.ok(criaturaService.buscarPorElementoSecundario(elemento));
    }

    @GetMapping("/pesquisar/secundario/elementos")
    public ResponseEntity<List<Criatura>> pesquisarPorElementosSecundarios(@RequestParam Set<Elemento> elementos){
        return ResponseEntity.ok(criaturaService.buscarPorElementosSecundarios(elementos));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Criatura> buscarPorID(@PathVariable UUID uuid){
        return ResponseEntity.ok(criaturaService.buscarPorId(uuid));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Criatura> atualizarCriatura(@PathVariable UUID uuid, @RequestBody AtualizarCriaturaDTO criaturaAtualizada){
        return ResponseEntity.ok(criaturaService.atualizarCriatura(uuid, criaturaAtualizada));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletarCriatura(@PathVariable UUID uuid){
        criaturaService.deletar(uuid);
        return ResponseEntity.noContent().build();
    }
}
