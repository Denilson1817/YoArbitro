package com.denilsonperez.yoarbitro.modelo;

public class Equipo {
    private String nombre;
    private String delegado;

    public Equipo() {

    }

    public Equipo(String nombre, String delegado) {
        this.nombre = nombre;
        this.delegado = delegado;
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
}
