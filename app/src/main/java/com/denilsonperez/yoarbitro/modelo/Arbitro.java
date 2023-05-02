package com.denilsonperez.yoarbitro.modelo;

public class Arbitro {
    private String contraseña;
    private String correo;
    private String edad;
    private Long last_login;
    private String localidad;
    private String nombre;
    private String numero;
    private String uid;

    public Arbitro() {
    }

    public Arbitro(String contraseña, String correo, String edad, Long last_login, String localidad, String nombre, String numero, String uid) {
        this.contraseña = contraseña;
        this.correo = correo;
        this.edad = edad;
        this.last_login = last_login;
        this.localidad = localidad;
        this.nombre = nombre;
        this.numero = numero;
        this.uid = uid;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public  Long getLast_login(){
        return last_login;
    }
    public void  setLast_login(Long last_login){
        this.last_login = last_login;
    }
    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
