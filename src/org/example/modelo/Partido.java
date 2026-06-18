package org.example.modelo;

// Enfrentamiento 1vs1

public class Partido {

    private Participante participanteA;
    private Participante participanteB;
    private Resultado resultado;

    public Partido(Participante participanteA, Participante participanteB) {
        this.participanteA = participanteA;
        this.participanteB = participanteB;
    }

    public void registrarResultado(Resultado resultado) {
        this.resultado = resultado;
    }

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
