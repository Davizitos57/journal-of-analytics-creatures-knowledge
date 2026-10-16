package com.scarfox.jack.domain.DTOs;

import java.util.UUID;

public record RespostaEquipeDTO(
    UUID uuid,
    String nome,
    String descricao,
    Integer quantidadeAgentes
) {}
