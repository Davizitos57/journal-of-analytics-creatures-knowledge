package com.scarfox.jack.domain.entity;

import com.scarfox.jack.enums.Patente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @Column(nullable=false)
    private String senha;

    private String nacionalidade;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Patente patente;

    @ManyToOne
    @JoinColumn(name = "equipe_uuid")
    private Equipe equipe;

    public Agente(String nome, String email, String senha, String nacionalidade, LocalDate dataNascimento, Patente patente) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
        this.patente = patente;
    }
}
