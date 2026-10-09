package com.scarfox.jack.domain.DTOs;

import com.scarfox.jack.domain.enums.Elemento;
import com.scarfox.jack.domain.enums.Tamanho;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AtualizarCriaturaDTO {
    private String nome;
    private Tamanho tamanho;
    private Elemento elementoPrincipal;
    private Set<Elemento> elementosSecundarios;
    private String fotoUrl;
}