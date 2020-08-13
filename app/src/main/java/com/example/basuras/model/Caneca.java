package com.example.basuras.model;

public class Caneca {
    String idcaneca;
    String nombre_caneca;
    String color;
    String descripcion;
    String fecha_update;
    int cant_reportes;

    public Caneca() {
    }

    public String getIdcaneca() {
        return idcaneca;
    }

    public void setIdcaneca(String idcaneca) {
        this.idcaneca = idcaneca;
    }

    public String getNombre_caneca() {
        return nombre_caneca;
    }

    public void setNombre_caneca(String nombre_caneca) {
        this.nombre_caneca = nombre_caneca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(String fecha_update) {
        this.fecha_update = fecha_update;
    }

    public int getCant_reportes() {
        return cant_reportes;
    }

    public void setCant_reportes(int cant_reportes) {
        this.cant_reportes = cant_reportes;
    }
}
