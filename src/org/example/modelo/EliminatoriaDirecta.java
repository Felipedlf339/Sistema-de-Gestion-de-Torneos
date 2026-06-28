package org.example.modelo;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta del patrón Strategy para el formato
 * de eliminatoria directa.
 *
 * En este formato, los participantes se enfrentan de dos en dos.
 * El perdedor queda eliminado y el ganador avanza a la siguiente ronda.
 * Los enfrentamientos se generan emparejando participantes en orden:
 * [0] vs [1], [2] vs [3], y así sucesivamente.
 */
public class EliminatoriaDirecta implements FormatoTorneo {
    /**
     * Genera los enfrentamientos de la primera ronda emparejando
     * participantes de dos en dos en orden de inscripción.
     *
     * @param participantes lista de participantes inscritos en el torneo
     * @return lista de partidos de la primera ronda
     */
    @Override
    public List<Partido> generarEnfrentamientos(List<Participante> participantes) {
        List<Partido> partidos = new ArrayList<>();
        for (int i = 0; i + 1 < participantes.size(); i += 2) {
            partidos.add(new Partido(participantes.get(i), participantes.get(i + 1)));
        }
        return partidos;
    }

    /**
     * Actualiza la clasificación tras finalizar un partido.
     * En eliminatoria directa, el ganador avanza a la siguiente ronda
     * y el perdedor queda eliminado del torneo.
     *
     * @param partidoFinalizado el partido que acaba de terminar con resultado registrado
     */
    @Override
    public void actualizarClasificacion(Partido partidoFinalizado) {
        //Ayudenme a implementar avance de ganadores que pasan a siguiente ronda
    }

}
