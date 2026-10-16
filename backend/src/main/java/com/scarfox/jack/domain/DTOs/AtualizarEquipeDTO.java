package com.scarfox.jack.domain.DTOs;

import lombok.Getter;

@Getter
public class AtualizarEquipeDTO {

    private String nome;
    private String descricao;

    private boolean nomeInformado;
    private boolean descricaoInformada;

    public void setNome(String nome) {
        this.nome = nome;
        this.nomeInformado = true;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
        this.descricaoInformada = true;
    }

    public boolean informouNome() {
        return nomeInformado;
    }

    public boolean informouDescricao() {
        return descricaoInformada;
    }
}