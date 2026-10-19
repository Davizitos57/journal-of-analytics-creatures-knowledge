package com.scarfox.jack.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "equipes")
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable=false, unique = true)
    private String nome;

    private String descricao;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private boolean ativo = true;

    @JsonIgnore
    @OneToMany(mappedBy = "equipe")
    private List<Agente> agentes = new ArrayList<>();

    public void desativar() {
        this.ativo = false;
    }

    public void reativar() {
        this.ativo = true;
    }

    public Equipe(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
