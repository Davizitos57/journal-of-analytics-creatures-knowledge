package com.scarfox.jack.domain.entity;

import com.scarfox.jack.enums.Elemento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Elemento elementos;

    private String descricao;

    public Ritual(String nome, Elemento elementos, String descricao) {
        this.nome = nome;
        this.elementos = elementos;
        this.descricao = descricao;
    }
}
