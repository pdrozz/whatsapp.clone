package com.pedrox.whatsclone.model;

import java.io.Serializable;

public class StatusModel implements Serializable {

    private String nome,data,foto,urlStatus,texto;

    public String getTexto() {
        return texto;
    }

    public StatusModel(String nome, String data, String foto, String urlStatus, String texto) {
        this.nome = nome;
        this.data = data;
        this.foto = foto;
        this.urlStatus = urlStatus;
        this.texto = texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrlStatus() {
        return urlStatus;
    }

    public void setUrlStatus(String urlStatus) {
        this.urlStatus = urlStatus;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public StatusModel() {
    }
}
