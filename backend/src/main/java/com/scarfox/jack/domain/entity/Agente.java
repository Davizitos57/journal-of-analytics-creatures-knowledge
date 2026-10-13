package com.scarfox.jack.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scarfox.jack.domain.enums.Patente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agentes")
public class Agente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable=false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    private String nacionalidade;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Patente patente;

    @ManyToOne
    @JoinColumn(name = "equipe_uuid")
    private Equipe equipe;

    @JsonIgnore
    @OneToMany(mappedBy = "agente")
    private List<HistoricoOcorrencia> historicoOcorrencias = new ArrayList<>();

    public Agente(String nome, String email, String nacionalidade, LocalDate dataNascimento, Patente patente) {
        this.nome = nome;
        this.email = email;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
        this.patente = patente;
    }
}
