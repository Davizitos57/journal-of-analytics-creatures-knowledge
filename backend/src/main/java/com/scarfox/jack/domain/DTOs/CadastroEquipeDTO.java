package com.scarfox.jack.domain.DTOs;

import jakarta.validation.constraints.NotBlank;

public record CadastroEquipeDTO(@NotBlank(message = "O nome da equipe deve ser informado.") String nome, String descricao) {
    
}