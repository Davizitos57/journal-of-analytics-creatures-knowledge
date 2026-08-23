package com.scarfox.jack.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "historico_ocorrencias")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HistoricoOcorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "campo_alterado", nullable = false)
    private String campoAlterado;

    @Column(name = "valor_anterior")
    private String valorAnterior;

    @Column(name = "valor_novo")
    private String valorNovo;

    @ManyToOne
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;

    @ManyToOne
    @JoinColumn(name = "ocorrencia_id", nullable = false)
    private Ocorrencia ocorrencia;

    public HistoricoOcorrencia(
            String campoAlterado,
            String valorAnterior,
            String valorNovo,
            Agente agente,
            Ocorrencia ocorrencia
    ) {

        this.dataHora = LocalDateTime.now();
        this.campoAlterado = campoAlterado;
        this.valorAnterior = valorAnterior;
        this.valorNovo = valorNovo;
        this.agente = agente;
        this.ocorrencia = ocorrencia;
    }
}