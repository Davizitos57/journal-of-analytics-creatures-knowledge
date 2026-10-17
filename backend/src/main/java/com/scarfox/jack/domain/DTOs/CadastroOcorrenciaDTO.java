package com.scarfox.jack.domain.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CadastroOcorrenciaDTO(
    @NotBlank(message = "O título da ocorrência deve ser informado.")
    String titulo,

    String descricao,

    @NotNull(message = "A data e hora da ocorrência devem ser informadas.")
    LocalDateTime dataHoraOcorrencia,

    @NotNull(message = "O local da ocorrência deve ser informado.")
    UUID localUUID,

    @PositiveOrZero(message = "O número de mortos não pode ser negativo.")
    Integer numMortos,

    @PositiveOrZero(message = "O número de feridos não pode ser negativo.")
    Integer numFeridos
) {}