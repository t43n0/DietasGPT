package com.dam.dietasgpt;

import java.util.List;

public class Plato {
    private String id;
    private String nombre;
    private String imagenUrl;
    private String descripcion;
    private List<String> ingredientes;

    public Plato() {
        // Constructor vacío necesario para Firebase
    }

    public Plato(String id, String nombre, String imagenUrl, String descripcion, List<String> ingredientes) {
        this.id = id;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }
}
