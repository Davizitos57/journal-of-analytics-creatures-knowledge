package com.scarfox.jack.domain.DTOs;

import java.time.LocalDate;
import lombok.Getter;

public class AtualizarAgenteDTO {

    @Getter
    private String nome;

    @Getter
    private String nacionalidade;

    @Getter
    private LocalDate dataNascimento;

    @Getter
    private String fotoUrl;

    private boolean nomeInformado;
    private boolean nacionalidadeInformada;
    private boolean dataNascimentoInformada;
    private boolean fotoUrlInformada;

    public void setNome(String nome) {
        this.nome = nome;
        this.nomeInformado = true;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
        this.nacionalidadeInformada = true;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        this.dataNascimentoInformada = true;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
        this.fotoUrlInformada = true;
    }

    public boolean informouNome() {
        return nomeInformado;
    }

    public boolean informouNacionalidade() {
        return nacionalidadeInformada;
    }

    public boolean informouDataNascimento() {
        return dataNascimentoInformada;
    }

    public boolean informouFotoUrl() {
        return fotoUrlInformada;
    }
}