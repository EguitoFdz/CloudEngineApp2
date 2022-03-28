package com.example.cloudengine.data.model;

public class Persona {

    private String uid;
    private String Nombre;
    private String Descripcion;
    private String Password;
    private String Precio;
    private String Video;

    public Persona() {
    }

    public String getUid() {
        return uid;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return Nombre + " || " + Descripcion + " || " + "$" + Precio + " pesos";
    }

}
