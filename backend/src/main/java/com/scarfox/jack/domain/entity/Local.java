package com.scarfox.jack.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "locais")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    private String rua;

    private String bairro;

    private String cidade;

    @Column(name = "estado_provincia")
    private String estadoProvincia;

    @Column(name = "pais", nullable = false)
    private String pais;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    public Local(String nome, String rua, String bairro, String cidade, String estadoProvincia, String pais, String codigoPostal) {
        this.nome = nome;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estadoProvincia = estadoProvincia;
        this.pais = pais;
        this.codigoPostal = codigoPostal;
    }
}