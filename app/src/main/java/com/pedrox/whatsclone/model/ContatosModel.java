package com.pedrox.whatsclone.model;

public class ContatosModel {

    private String nome,foto,id,sign_up,stats,last_login;

    public String getStats() {
        return stats;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public ContatosModel(String nome, String foto, String id) {
        this.nome = nome;
        this.foto = foto;
        this.id=id;
    }

    public String getSign_up() {
        return sign_up;
    }

    public void setSign_up(String sign_up) {
        this.sign_up = sign_up;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContatosModel() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
