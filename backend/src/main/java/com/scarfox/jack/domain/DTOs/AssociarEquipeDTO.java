package com.scarfox.jack.domain.DTOs;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AssociarEquipeDTO(@NotNull UUID equipeId) {
    
}