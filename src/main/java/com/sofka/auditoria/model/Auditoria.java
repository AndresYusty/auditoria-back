package com.sofka.auditoria.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "auditorias")
public class Auditoria {
    @Id
    private String id;
    private String usuario;
    private String accion;
    private LocalDateTime fecha;

    // Constructor
    public Auditoria() {
    }

    public Auditoria(String usuario, String accion, LocalDateTime fecha) {
        this.usuario = usuario;
        this.accion = accion;
        this.fecha = fecha;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

