package org.example.modelo;
import java.util.ArrayList;
import java.util.List;


public class LigaSimple implements FormatoTorneo {
    //Arreglos para los participantes y sus puntos correspondientes.
    private List<Participante> clasificacion = new ArrayList<>();
    private List<Integer> puntos = new ArrayList<>();
    /**
     * Genera todos los enfrentamientos posibles entre participantes.
     * Cada par se enfrenta exactamente una vez.
     * Se clasifican por puntos en una tabla: victoria suma 3, empate suma 1, derrota suma 0.
     *
     * @param participantes lista de participantes inscritos.
     * @return lista completa de partidos del torneo.
     */
    @Override
    public List<Partido> generarEnfrentamientos(List<Participante> participantes) {
        List<Partido> partidos = new ArrayList<>();
        //Inicializar cada participante con 0 puntos.
        for(Participante p : participantes) {
            clasificacion.add(p);
            puntos.add(0);
        }

        for(int i = 0; i < participantes.size(); i++) {
            for(int j = i + 1; j < participantes.size(); j++) {
                partidos.add(new Partido(participantes.get(i), participantes.get(j)));
            }
        }
        return partidos;
    }

    /**
     * Actualiza la tabla de puntos tras finalizar un partido.
     * Victoria suma 3, empate suma 1, derrota suma 0.
     * @param partido partido finalizado con resultado registrado.
     */
    @Override
    public void actualizarClasificacion(Partido partido) {
        Resultado resultado = partido.getResultado();

        if(resultado == null) return;

        int puntajeA = resultado.getPuntajeParticipanteA();
        int puntajeB = resultado.getPuntajeParticipanteB();

        //Buscar la posicion de cada participante en la lista.
        int posA = clasificacion.indexOf(partido.getParticipanteA());
        int posB = clasificacion.indexOf(partido.getParticipanteB());

        if(puntajeA > puntajeB) {
            //Gana A, suma 3 puntos.
            puntos.set(posA, puntos.get(posA) + 3);
        } else if (puntajeB > puntajeA) {
            //Gana B, suma 3 puntos.
            puntos.set(posB, puntos.get(posB) + 3);
        } else {
            //Empate, ambos suman un punto.
            puntos.set(posA, puntos.get(posA) + 1);
            puntos.set(posB, puntos.get(posB) + 1);

        }
    }

    /**
     * Retorna la lista de participantes en orden de inscripción.
     * Usar junto con getPuntos() para mostrar la clasificación.
     * @return lista de participantes.
     */
    public List<Participante> getClasificacion() {
        return clasificacion;
    }

    /**
     * Retorna los puntos acumulados en el mismo orden de getClasificación().
     * @return lista de puntos.
     */
    public List<Integer> getPuntos() {
        return puntos;
    }

}
