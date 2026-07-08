package org.example.modelo;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un enfrentamiento entre dos participantes.
 * Si ya finalizo registra un resultado.
 */
public class Partido implements Serializable {

    private Participante participanteA;
    private Participante participanteB;
    private Resultado resultado;
    private LocalDateTime fechaHora;

    /**
     * Constructor de Partido.
     * @param participanteA primer participante del partido.
     * @param participanteB segundo participante del partido.
     */
    public Partido(Participante participanteA, Participante participanteB) {
        this.participanteA = participanteA;
        this.participanteB = participanteB;
        this.fechaHora = null;
    }

    /**
     * Ya finalizado se registra un resultado.
     * @param resultado el resultado obtenido.
     */
    public void registrarResultado(Resultado resultado) {
        this.resultado = resultado;
    }


    public void programarFecha(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getFechaFormateada() {
        if (fechaHora == null) return "Sin programar";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHora.format(formatter);
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
