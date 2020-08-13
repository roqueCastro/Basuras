package com.example.basuras.model;

public class Model {
    String usuarios;
    String reporte;
    String canecas;

    public Model(String usuarios, String reporte, String canecas) {
        this.usuarios = usuarios;
        this.reporte = reporte;
        this.canecas = canecas;
    }

    public String getUsuarios() {
        return usuarios;
    }

    public String getReporte() {
        return reporte;
    }

    public String getCanecas() {
        return canecas;
    }
}
