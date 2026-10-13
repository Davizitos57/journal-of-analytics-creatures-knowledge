package com.scarfox.jack.domain.DTOs;

import java.time.LocalDate;

public record CadastroAgenteDTO(String nome, String email, String senha, String nacionalidade, LocalDate dataNascimento, String fotoUrl) {
    
}
