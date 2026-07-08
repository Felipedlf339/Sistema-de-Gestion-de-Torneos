package org.example.modelo;

import java.util.List;

/**
 * Interfaz que define que formato de competencia se utilizara para el torneo.
 * Patrón Strategy.
 */
public interface FormatoTorneo {

    /**
     * Genera enfrentamientos según los integrantes inscritos.
     * @param participantes lista de participantes inscritos.
     * @return los enfrentamientos (partidos) generados.
     */
    List<Partido> generarEnfrentamientos(List<Participante> participantes);

    /**
     * Actualiza los datos del torneo al finalizar el partido.
     * @param partidoFinalizado partido con un resultado.
     */
    void actualizarClasificacion(Partido partidoFinalizado);
}
