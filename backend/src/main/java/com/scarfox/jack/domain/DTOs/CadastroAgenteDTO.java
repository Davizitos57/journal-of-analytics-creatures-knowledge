package com.scarfox.jack.domain.DTOs;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record CadastroAgenteDTO(
    @NotBlank(message = "O nome deve ser informado.")
    String nome,

    @NotBlank(message = "O email deve ser informado.")
    @Email(message = "O email informado é inválido.")
    String email,

    @NotBlank(message = "A senha deve ser informada.")
    String senha,

    String nacionalidade,
    LocalDate dataNascimento,
    String fotoUrl
) {}
