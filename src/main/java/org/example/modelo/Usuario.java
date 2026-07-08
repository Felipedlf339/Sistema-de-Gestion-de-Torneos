package org.example.modelo;
import java.io.Serializable;

/**
 * Representa a un usuario del sistema.
 * Implementa Serializable para permitir que las cuentas se guarden.
 */
public class Usuario implements Serializable {

    private final String nombreUsuario;

    public Usuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * @return representación en formato de texto.
     */
    @Override
    public String toString() {
        return nombreUsuario;
    }
}