package com.pedrox.whatsclone.model;

public class ConversasModel {

    private String name,date,newMessage,message;
    private String imagemPerfil,id;

    public ConversasModel(String name, String date, String newMessage, String message,String imagemPerfil,String id) {
        this.name = name;
        this.date = date;
        this.newMessage = newMessage;
        this.message = message;
        this.imagemPerfil=imagemPerfil;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConversasModel() {
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
