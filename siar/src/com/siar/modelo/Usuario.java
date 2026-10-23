package com.siar.modelo;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private Rol rol;
    private String nombre;
    private String email;
    private String passwordHash;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    public Usuario() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}