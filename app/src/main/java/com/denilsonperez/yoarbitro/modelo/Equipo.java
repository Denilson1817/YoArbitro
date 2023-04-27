package com.denilsonperez.yoarbitro.modelo;

public class Equipo {

    private  String uid;
    private String nombre;
    private String delegado;
    private String numContacto;

    public Equipo() {

    }

    public Equipo(String uid, String nombre, String delegado, String numContacto) {
        this.uid = uid;
        this.nombre = nombre;
        this.delegado = delegado;
        this.numContacto = numContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDelegado() {
        return delegado;
    }

    public void setDelegado(String delegado) {
        this.delegado = delegado;
    }

    public String getNumContacto() {
        return numContacto;
    }

    public void setNumContacto(String numContacto) {
        this.numContacto = numContacto;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}