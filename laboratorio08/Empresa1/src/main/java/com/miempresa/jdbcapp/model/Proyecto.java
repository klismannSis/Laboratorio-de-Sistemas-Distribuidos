package main.java.com.miempresa.jdbcapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Proyecto {
    private int idProy;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public Proyecto(int idProy, String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idProy = idProy;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdProy() {
        return idProy;
    }

    public void setIdProy(int idProy) {
        this.idProy = idProy;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
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
        return String.format("Proyecto{idProy=%d, nombre='%s', fechaInicio=%s, fechaFin=%s, createdAt=%s, updatedAt=%s}",
                             idProy, nombre, fechaInicio, fechaFin, createdAt, updatedAt);
    }
}
