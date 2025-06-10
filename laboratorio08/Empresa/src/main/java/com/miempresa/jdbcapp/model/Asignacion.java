package com.miempresa.jdbcapp.model;

import java.time.LocalDateTime;

public class Asignacion {
    private int idAsignacion;
    private int idIng;
    private int idProy;
    private String rolProyecto;
    private LocalDateTime createdAt;

    public Asignacion() { }

    public Asignacion(int idAsignacion, int idIng, int idProy, String rolProyecto) {
        this.idAsignacion = idAsignacion;
        this.idIng = idIng;
        this.idProy = idProy;
        this.rolProyecto = rolProyecto;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public int getIdIng() {
        return idIng;
    }

    public void setIdIng(int idIng) {
        this.idIng = idIng;
    }

    public int getIdProy() {
        return idProy;
    }

    public void setIdProy(int idProy) {
        this.idProy = idProy;
    }

    public String getRolProyecto() {
        return rolProyecto;
    }

    public void setRolProyecto(String rolProyecto) {
        this.rolProyecto = rolProyecto;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
