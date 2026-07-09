package org.example.modelo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Implementación del formato de competencia de Eliminatoria Doble
 * En este formato, un participante debe perder dos veces para ser eliminado definitivamente.
 * El sistema mantiene dos cuadros de competencia:
 * Winner Bracket: Participantes que no han perdido.
 * Loser Bracket: Participantes que han perdido una vez.
 * El torneo termina en una Gran Final entre el ganador del Winner Bracket y el ganador del Loser Bracket.
 */

public class EliminatoriaDoble implements FormatoTorneo, Serializable {

    private List<Participante> winnerBracket = new ArrayList<>();
    private List<Participante> loserBracket = new ArrayList<>();
    private List<Partido> partidosWinner = new ArrayList<>();
    private List<Partido> partidosLoser = new ArrayList<>();

    private Participante campeonWinner;
    private Participante campeonLoser;
    private boolean enGranFinal = false;
    private boolean granFinalJugada = false;

    /**
     * Inicializa la estructura del torneo creando el bracket de ganadores inicial.
     * @param participantes Lista de participantes inscritos.
     * @return Lista de los primeros partidos del torneo.
     */
    @Override
    public List<Partido> generarEnfrentamientos(List<Participante> participantes) {
        winnerBracket = new ArrayList<>(participantes);
        loserBracket = new ArrayList<>();
        partidosWinner = new ArrayList<>();
        partidosLoser = new ArrayList<>();

        List<Partido> partidos = new ArrayList<>();
        for (int i = 0; i + 1 < winnerBracket.size(); i += 2) {
            Partido p = new Partido(winnerBracket.get(i), winnerBracket.get(i + 1));
            partidos.add(p);
            partidosWinner.add(p);
        }
        return partidos;
    }

    /**
     * Valida que no existan empates en el resultado de un partido, ya que este formato
     * requiere un ganador para avanzar a los brackets correspondientes.
     * @param partido Partido a evaluar.
     * @throws IllegalStateException si el resultado es un empate.
     */
    @Override
    public void actualizarClasificacion(Partido partido) {
        Resultado resultado = partido.getResultado();
        if (resultado == null) return;

        if (resultado.getPuntajeParticipanteA() == resultado.getPuntajeParticipanteB()) {
            throw new IllegalStateException("En eliminatoria doble no puede haber empate.");
        }
    }

    /**
     * Calcula la siguiente ronda de partidos basándose en los resultados de la ronda anterior.
     * Esta lógica gestiona:
     * El descenso de perdedores del Winner Bracket al Loser Bracket.
     * La eliminación definitiva de perdedores en el Loser Bracket.
     * La transición hacia la Gran Final.
     * El posible "reset" del bracket si el ganador del Loser Bracket vence al del Winner.
     * @param rondaActual Lista de partidos que se acaban de jugar.
     * @return Lista de nuevos partidos generados para la siguiente fase.
     */
    public List<Partido> generarSiguienteRonda(List<Partido> rondaActual) {
        List<Partido> siguientes = new ArrayList<>();

        //Listas temporales para construir la siguiente ronda
        List<Participante> nuevosWinner = new ArrayList<>();
        List<Participante> nuevosLoser = new ArrayList<>();

        //Identificar quiénes jugaron en esta ronda para no perder a los que quedaron impares (Byes)
        List<Participante> jugaron = new ArrayList<>();
        for (Partido p : rondaActual) {
            jugaron.add(p.getParticipanteA());
            jugaron.add(p.getParticipanteB());
        }

        //Arrastrar a la siguiente ronda a los que no tuvieron rival
        for (Participante p : winnerBracket) {
            if (!jugaron.contains(p)) nuevosWinner.add(p);
        }
        for (Participante p : loserBracket) {
            if (!jugaron.contains(p)) nuevosLoser.add(p);
        }

        //Procesar los resultados
        for (Partido p : rondaActual) {
            Resultado r = p.getResultado();
            if (r == null) continue;

            Participante ganador = r.ganoParticipanteA() ? p.getParticipanteA() : p.getParticipanteB();
            Participante perdedor = r.ganoParticipanteA() ? p.getParticipanteB() : p.getParticipanteA();

            if (partidosWinner.contains(p)) {
                //Partido del Winner Bracket
                nuevosWinner.add(ganador);
                nuevosLoser.add(perdedor); // Cae al loser bracket

            } else if (partidosLoser.contains(p)) {
                //Partido del Loser Bracket
                nuevosLoser.add(ganador);
                //El perdedor es eliminado automáticamente al no agregarlo a ninguna lista

            } else if (enGranFinal) {
                //Lógica de la Gran Final
                if (ganador.equals(campeonLoser) && !granFinalJugada) {
                    granFinalJugada = true;
                    //Revancha
                    Partido revancha = new Partido(campeonWinner, campeonLoser);
                    siguientes.add(revancha);
                    return siguientes;
                } else {
                    //Campeon definitivo
                    winnerBracket = new ArrayList<>();
                    winnerBracket.add(ganador);
                    loserBracket = new ArrayList<>();
                    return siguientes; //Retorna la lista vacía, finaliza el torneo
                }
            }
        }

        //Actualizar los brackets oficiales
        winnerBracket = nuevosWinner;
        loserBracket = nuevosLoser;

        //Detectar si llegamos a la Gran Final
        if (winnerBracket.size() == 1 && loserBracket.size() == 1) {
            enGranFinal = true;
            campeonWinner = winnerBracket.get(0);
            campeonLoser = loserBracket.get(0);
            Partido granFinal = new Partido(campeonWinner, campeonLoser);
            siguientes.add(granFinal);
            return siguientes;
        }

        //Armar los próximos partidos del Winner Bracket
        partidosWinner.clear();
        for (int i = 0; i + 1 < winnerBracket.size(); i += 2) {
            Partido p = new Partido(winnerBracket.get(i), winnerBracket.get(i + 1));
            siguientes.add(p);
            partidosWinner.add(p);
        }

        //Armar los próximos partidos del Loser Bracket
        partidosLoser.clear();
        for (int i = 0; i + 1 < loserBracket.size(); i += 2) {
            Partido p = new Partido(loserBracket.get(i), loserBracket.get(i + 1));
            siguientes.add(p);
            partidosLoser.add(p);
        }

        return siguientes;
    }

    public boolean isEnGranFinal() { return enGranFinal; }
    public List<Participante> getWinnerBracket() { return winnerBracket; }
    public List<Participante> getLoserBracket() { return loserBracket; }
}