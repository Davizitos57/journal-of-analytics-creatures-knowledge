package com.scarfox.jack.domain.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import com.scarfox.jack.domain.enums.Status;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class AtualizarOcorrenciaDTO {

    private String titulo;
    private String descricao;
    private LocalDateTime dataHoraOcorrencia;
    private UUID localUUID;
    private Status status;

    @PositiveOrZero(message = "O número de mortos não pode ser negativo.")
    private Integer numMortos;

    @PositiveOrZero(message = "O número de feridos não pode ser negativo.")
    private Integer numFeridos;

    private boolean tituloInformado;
    private boolean descricaoInformada;
    private boolean dataHoraOcorrenciaInformada;
    private boolean localInformado;
    private boolean statusInformado;
    private boolean numMortosInformado;
    private boolean numFeridosInformado;

    public void setTitulo(String titulo) {
        this.titulo = titulo;
        this.tituloInformado = true;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
        this.descricaoInformada = true;
    }

    public void setDataHoraOcorrencia(LocalDateTime dataHoraOcorrencia) {
        this.dataHoraOcorrencia = dataHoraOcorrencia;
        this.dataHoraOcorrenciaInformada = true;
    }

    public void setLocalUUID(UUID localUUID) {
        this.localUUID = localUUID;
        this.localInformado = true;
    }

    public void setStatus(Status status) {
        this.status = status;
        this.statusInformado = true;
    }

    public void setNumMortos(Integer numMortos) {
        this.numMortos = numMortos;
        this.numMortosInformado = true;
    }

    public void setNumFeridos(Integer numFeridos) {
        this.numFeridos = numFeridos;
        this.numFeridosInformado = true;
    }

    public boolean informouTitulo() {
        return tituloInformado;
    }

    public boolean informouDescricao() {
        return descricaoInformada;
    }

    public boolean informouDataHoraOcorrencia() {
        return dataHoraOcorrenciaInformada;
    }

    public boolean informouLocal() {
        return localInformado;
    }

    public boolean informouStatus() {
        return statusInformado;
    }

    public boolean informouNumMortos() {
        return numMortosInformado;
    }

    public boolean informouNumFeridos() {
        return numFeridosInformado;
    }
}