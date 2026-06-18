package org.example.modelo;

import java.util.List;

// decide si el torneo es por puntos, eliminación directa, lo que sea.
public interface FormatoTorneo {

    List<Partido> generarEnfrentamientos(List<Participante> participantes);

    void actualizarClasificacion(Partido partidoFinalizado);
}
