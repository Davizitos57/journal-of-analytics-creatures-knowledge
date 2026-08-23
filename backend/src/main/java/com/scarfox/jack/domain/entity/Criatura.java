package com.scarfox.jack.domain.entity;

import com.scarfox.jack.domain.enums.Elemento;
import com.scarfox.jack.domain.enums.Tamanho;
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
@Table(name = "criaturas")
public class Criatura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tamanho tamanho;

    @Enumerated(EnumType.STRING)
    @Column(name = "elemento_principal", nullable = false)
    private Elemento elementoPrincipal;

    @ElementCollection
    @CollectionTable(
            name = "criatura_elementos_secundarios",
            joinColumns = @JoinColumn(name = "criatura_uuid")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "elemento")
    private Set<Elemento> elementosSecundarios = new HashSet<>();

    @ManyToMany(mappedBy = "criaturas")
    private Set<Ocorrencia> ocorrencias = new HashSet<>();

    public Criatura(String nome, Tamanho tamanho, Elemento elementoPrincipal) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.elementoPrincipal = elementoPrincipal;
    }
}