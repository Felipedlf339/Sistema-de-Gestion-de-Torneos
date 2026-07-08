package org.example.modelo;
import java.io.Serializable;

// Cuantos puntos se hicieron y quien es el ganador del match.
public class Resultado implements Serializable {

    private int puntajeParticipanteA;
    private int puntajeParticipanteB;

    public Resultado(int puntajeParticipanteA, int puntajeParticipanteB) {
        this.puntajeParticipanteA = puntajeParticipanteA;
        this.puntajeParticipanteB = puntajeParticipanteB;
    }

    public int getPuntajeParticipanteA() {
        return puntajeParticipanteA;
    }

    public int getPuntajeParticipanteB() {
        return puntajeParticipanteB;
    }

    public boolean ganoParticipanteA() {
        return puntajeParticipanteA > puntajeParticipanteB;
    }
}
