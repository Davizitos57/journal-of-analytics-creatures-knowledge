package com.scarfox.jack.domain.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import com.scarfox.jack.domain.enums.Status;

public record RespostaOcorrenciaDTO(
    UUID uuid,
    String titulo,
    String descricao,
    LocalDateTime dataHoraOcorrencia,
    LocalDateTime dataHoraRegistro,
    Status status,
    Integer numMortos,
    Integer numFeridos,
    UUID agenteRegistradorUUID,
    String agenteRegistradorNome,
    UUID localUUID,
    String localNome
) {}
