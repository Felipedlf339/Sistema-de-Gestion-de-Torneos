package org.example.modelo;

/**
 * Interfaz que se implementa a cualquier participante ya sea como grupo o individual para
 * ser participe del torneo.
 */

public interface Participante {


    // Getters.
    String getNombre();

    String getId();

    /**
     * @return la cantidad de integrantes que forman parte del grupo (1 si es individual).
     */
    int cantidadDeMiembros();
}
