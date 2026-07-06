package org.example.modelo;

public class Usuario {

    private final String nombreUsuario;

    public Usuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    @Override
    public String toString() {
        return nombreUsuario;
    }
}