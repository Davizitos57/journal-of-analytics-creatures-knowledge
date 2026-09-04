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

import com.scarfox.jack.domain.entity.Local;
import com.scarfox.jack.service.LocalService;

@RestController
@RequestMapping("/locais")
public class LocalController {

    private final LocalService localService;

    public LocalController(LocalService localService){
        this.localService = localService;
    }

    @PostMapping
    public ResponseEntity<Local> adicionar(@RequestBody Local local){
        Local localInserido = localService.inserir(local);
        return ResponseEntity.status(HttpStatus.CREATED).body(localInserido);
    }

    @GetMapping
    public ResponseEntity<List<Local>> listarTodos() {
        return ResponseEntity.ok(localService.listarTodos());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Local> buscarPorID(@PathVariable UUID uuid) {
        return ResponseEntity.ok(localService.buscarPorID(uuid));
    }

    @GetMapping("/pesquisar/nome")
    public ResponseEntity<List<Local>> pesquisarPorNome(@RequestParam String termo) {
        return ResponseEntity.ok(localService.pesquisarPorNome(termo));
    }

    @GetMapping("/paises")
    public ResponseEntity<List<String>> listarPaises(){
        return ResponseEntity.ok(localService.listarPaises());
    }

    @GetMapping("/estados")
    public ResponseEntity<List<String>> listarEstados(@RequestParam String pais){
        return ResponseEntity.ok(localService.listarEstados(pais));
    }

    @GetMapping("/cidades")
    public ResponseEntity<List<String>> listarCidades(@RequestParam String pais, @RequestParam String estadoProvincia) {
        return ResponseEntity.ok(localService.listarCidades(pais, estadoProvincia));
    }
    
    @GetMapping("/bairros")
    public ResponseEntity<List<String>> listarBairros(@RequestParam String pais, @RequestParam String estadoProvincia, @RequestParam String cidade) {
        return ResponseEntity.ok(localService.listarBairros(pais, estadoProvincia, cidade));
    }

    @GetMapping("/ruas")
    public ResponseEntity<List<String>> listarRuas(@RequestParam String pais, @RequestParam String estadoProvincia, @RequestParam String cidade, @RequestParam String bairro) {
        return ResponseEntity.ok(localService.listarRuas(pais, estadoProvincia, cidade, bairro));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Local> atualizarLocal(@PathVariable UUID uuid, @RequestBody Local local){
        return ResponseEntity.ok(localService.atualizarLocal(uuid, local));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletarLocal(@PathVariable UUID uuid){
        localService.deletar(uuid);
        return ResponseEntity.noContent().build();
    }
}
