package com.example.basuras.model;

public class Reporte {
    String idreporte, fecha_update, estado, lat, lng, usuario, caneca;

    public Reporte() {
    }

    public Reporte(String idreporte, String fecha_update, String estado, String lat, String lng, String usuario, String caneca) {
        this.idreporte = idreporte;
        this.fecha_update = fecha_update;
        this.estado = estado;
        this.lat = lat;
        this.lng = lng;
        this.usuario = usuario;
        this.caneca = caneca;
    }

    public String getIdreporte() {
        return idreporte;
    }

    public void setIdreporte(String idreporte) {
        this.idreporte = idreporte;
    }

    public String getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(String fecha_update) {
        this.fecha_update = fecha_update;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCaneca() {
        return caneca;
    }

    public void setCaneca(String caneca) {
        this.caneca = caneca;
    }
}
