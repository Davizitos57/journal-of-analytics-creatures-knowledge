
package com.scarfox.jack.domain.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

public record RespostaHistoricoOcorrenciaDTO(
    UUID uuid,
    LocalDateTime dataHora,
    String campoAlterado,
    String valorAnterior,
    String valorNovo,
    UUID agenteUUID,
    String agenteNome,
    UUID ocorrenciaUUID
) {}
