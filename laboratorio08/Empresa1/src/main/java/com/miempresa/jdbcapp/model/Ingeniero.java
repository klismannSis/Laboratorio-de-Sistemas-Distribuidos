package main.java.com.miempresa.jdbcapp.model;

import java.time.LocalDateTime;

public class Ingeniero {
    private int idIng;
    private int idDpto;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String cargo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public Ingeniero(int idIng, int idDpto, String nombre, String apellido, String especialidad, String cargo) {
        this.idIng = idIng;
        this.idDpto = idDpto;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
        this.cargo = cargo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdIng() {
        return idIng;
    }

    public void setIdIng(int idIng) {
        this.idIng = idIng;
    }

    public int getIdDpto() {
        return idDpto;
    }

    public void setIdDpto(int idDpto) {
        this.idDpto = idDpto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return String.format(
            "Ingeniero{idIng=%d, idDpto=%d, nombre='%s', apellido='%s', especialidad='%s', cargo='%s', createdAt=%s, updatedAt=%s}",
            idIng, idDpto, nombre, apellido, especialidad, cargo, createdAt, updatedAt
        );
    }
}
