package org.example.modelo;
import java.io.Serializable;

/**
 * Representa un enfrentamiento entre dos participantes.
 * Si ya finalizo registra un resultado.
 */
public class Partido implements Serializable {

    private Participante participanteA;
    private Participante participanteB;
    private Resultado resultado;

    /**
     * Constructor de Partido.
     * @param participanteA primer participante del partido.
     * @param participanteB segundo participante del partido.
     */
    public Partido(Participante participanteA, Participante participanteB) {
        this.participanteA = participanteA;
        this.participanteB = participanteB;
    }

    /**
     * Ya finalizado se registra un resultado.
     * @param resultado el resultado obtenido.
     */
    public void registrarResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    // Getters.
    public Participante getParticipanteA() {
        return participanteA;
    }

    public Participante getParticipanteB() {
        return participanteB;
    }

    public Resultado getResultado() {
        return resultado;
    }
}
