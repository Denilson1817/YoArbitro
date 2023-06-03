package com.denilsonperez.yoarbitro.modelo;

public class Jugador {
    private String uid;
    private String nombre;
    private String numero;

    private String idEquipo;

    public Jugador() {
    }
    public Jugador(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
    }

    public Jugador(String uid, String nombre, String numero, String idEquipo) {
        this.uid = uid;
        this.nombre = nombre;
        this.numero = numero;
        this.idEquipo = idEquipo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }



    @Override
    public String toString() {
        return nombre + "," + numero;
    }


}