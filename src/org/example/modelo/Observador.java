package org.example.modelo;

/**
 * Interfaz del patrón Observer
 *
 * Cualquier clase que quiera recibir notificaciones del estado
 * de un Torneo debe implementar esta interfaz.
 */

public interface Observador {
    void actualizar(Torneo torneo);
}