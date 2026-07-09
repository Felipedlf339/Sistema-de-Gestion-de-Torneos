package org.example.modelo;
import java.io.Serializable;


/**
 * El resultado de un partido ya finalizado, puntaje y ganador.
 */
public class Resultado implements Serializable {

    private int puntajeParticipanteA;
    private int puntajeParticipanteB;

    public Resultado(int puntajeParticipanteA, int puntajeParticipanteB) {
        this.puntajeParticipanteA = puntajeParticipanteA;
        this.puntajeParticipanteB = puntajeParticipanteB;
    }

    // Getters
    public int getPuntajeParticipanteA() {
        return puntajeParticipanteA;
    }

    public int getPuntajeParticipanteB() {
        return puntajeParticipanteB;
    }

    /**
     * @return True si el participante A fue el ganador por puntos.
     */
    public boolean ganoParticipanteA() {
        return puntajeParticipanteA > puntajeParticipanteB;
    }

    /**
     * @return True si el partido terminó empatado.
     */
    public boolean esEmpate() {
        return puntajeParticipanteA == puntajeParticipanteB;
    }
}

