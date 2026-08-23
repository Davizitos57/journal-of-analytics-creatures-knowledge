package com.scarfox.jack.domain.entity;

import com.scarfox.jack.domain.enums.Elemento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rituais")
public class Ritual {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Elemento elemento;

    private String descricao;

    @ManyToMany(mappedBy = "rituais")
    private Set<Ocorrencia> ocorrencias = new HashSet<>();

    public Ritual(String nome, Elemento elemento, String descricao) {
        this.nome = nome;
        this.elemento = elemento;
        this.descricao = descricao;
    }
}
