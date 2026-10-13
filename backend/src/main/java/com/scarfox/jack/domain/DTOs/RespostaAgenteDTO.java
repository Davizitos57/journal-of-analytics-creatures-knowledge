package com.scarfox.jack.domain.DTOs;

import java.time.LocalDate;
import java.util.UUID;

import com.scarfox.jack.domain.enums.Patente;

public record RespostaAgenteDTO(   
    UUID uuid,
    String nome,
    String email,
    String nacionalidade,
    LocalDate dataNascimento,
    String fotoUrl,
    Patente patente
){}
