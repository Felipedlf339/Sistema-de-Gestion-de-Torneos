package org.example.modelo;
import java.io.Serializable;

public class Usuario implements Serializable {

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