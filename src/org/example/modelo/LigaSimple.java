package org.example.modelo;
import java.util.ArrayList;
import java.util.List;

public class LigaSimple implements FormatoTorneo {
    /**
     * Genera todos los enfrentamientos posibles entre participantes.
     * Cada par se enfrenta exactamente una vez.
     *
     * @param participantes lista de participantes inscritos.
     * @return lista completa de partidos del torneo.
     */
    @Override
    public List<Partido> generarEnfrentamientos(List<Participante> participantes) {
        List<Partido> partidos = new ArrayList<>();
        for(int i = 0; i < participantes.size(); i++){
            for(int j = i + 1; j < participantes.size(); j++){
                partidos.add(new Partido(participantes.get(i), participantes.get(j)));
            }
        }
        return partidos;
    }
    @Override
    public void actualizarClasificacion(Partido partido) {

    }

}
