package com.example.basuras.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    String idusuario, nombre_usu, correo_usu, password,  telefono, direccion,  fecha_update, rol_idrol;


    public Usuario(String idusuario, String nombre_usu, String correo_usu, String password, String telefono, String direccion, String fecha_update, String rol_idrol) {
        this.idusuario = idusuario;
        this.nombre_usu = nombre_usu;
        this.correo_usu = correo_usu;
        this.password = password;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fecha_update = fecha_update;
        this.rol_idrol = rol_idrol;
    }

    public Usuario() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre_usu() {
        return nombre_usu;
    }

    public void setNombre_usu(String nombre_usu) {
        this.nombre_usu = nombre_usu;
    }

    public String getCorreo_usu() {
        return correo_usu;
    }

    public void setCorreo_usu(String correo_usu) {
        this.correo_usu = correo_usu;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(String fecha_update) {
        this.fecha_update = fecha_update;
    }

    public String getRol_idrol() {
        return rol_idrol;
    }

    public void setRol_idrol(String rol_idrol) {
        this.rol_idrol = rol_idrol;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
