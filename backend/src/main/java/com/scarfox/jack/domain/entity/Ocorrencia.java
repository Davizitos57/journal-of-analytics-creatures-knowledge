package com.scarfox.jack.domain.entity;

import com.scarfox.jack.domain.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ocorrencias")
public class Ocorrencia {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Column(name = "data_hora_ocorrencia", nullable = false)
    private LocalDateTime dataHoraOcorrencia;

    @Column(name = "data_hora_registro", nullable = false, updatable = false)
    private LocalDateTime dataHoraRegistro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "numero_mortos")
    private Integer numMortos;

    @Column(name = "numero_feridos")
    private Integer numFeridos;

    @ManyToOne
    @JoinColumn(name = "agente_registrador_uuid", nullable = false)
    private Agente agenteRegistrador;

    @ManyToOne
    @JoinColumn(name = "local_uuid", nullable = false)
    private Local local;

    @OneToMany(mappedBy = "ocorrencia")
    private List<HistoricoOcorrencia> historicos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "ocorrencia_criaturas", joinColumns = @JoinColumn(name = "ocorrencia_uuid"), inverseJoinColumns = @JoinColumn(name = "criatura_uuid"))
    private Set<Criatura> criaturas = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "ocorrencia_rituais", joinColumns = @JoinColumn(name = "ocorrencia_uuid"), inverseJoinColumns = @JoinColumn(name = "ritual_uuid"))
    private Set<Ritual> rituais = new HashSet<>();

    public Ocorrencia(String titulo, String descricao, LocalDateTime dataHoraOcorrencia,
                      Status status, Integer numMortos, Integer numFeridos, Agente agenteRegistrador, Local local) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataHoraOcorrencia = dataHoraOcorrencia;
        this.dataHoraRegistro = LocalDateTime.now();
        this.status = status;
        this.numMortos = numMortos;
        this.numFeridos = numFeridos;
        this.agenteRegistrador = agenteRegistrador;
        this.local = local;
    }
}
