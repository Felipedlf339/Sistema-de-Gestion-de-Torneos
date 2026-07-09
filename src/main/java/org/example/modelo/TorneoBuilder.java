package org.example.modelo;

import java.util.UUID;
import java.time.LocalDate;

/**
 * Clase que implementa el patrón de diseño Builder para la creación de Torneos.
 * Permite construir un objeto Torneo paso a paso.
 */
public class TorneoBuilder {

    private String nombre;
    private Disciplina disciplina;
    private FormatoTorneo formato;
    private Usuario creador;
    private int minParticipantes;
    private int maxParticipantes;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public TorneoBuilder conFechas(LocalDate inicio, LocalDate fin) {
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        return this;
    }


    /**
     * Asigna el nombre del torneo.
     * @param nombre nombre escogido por el usuario.
     * @return la instancia actual de TorneoBuilder.
     */
    public TorneoBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    /**
     * Asigna la disciplina que se usará en el torneo.
     * @param disciplina escogida por el usuario.
     * @return la instancia actual de TorneoBuilder.
     */
    public TorneoBuilder conDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
        return this;
    }

    /**
     * Asigna el sistema de la competencia.
     * @param formato formato con el que se desarrollara la competencia.
     * @return la instancia actual de TorneoBuilder.
     */
    public TorneoBuilder conFormato(FormatoTorneo formato) {
        this.formato = formato;
        return this;
    }

    /**
     * Quien es el que crea el torneo.
     * @param creador la cuenta de usuario de la persona creando el torneo.
     * @return la instancia actual de TorneoBuilder.
     */
    public TorneoBuilder conCreador(Usuario creador) {
        this.creador = creador;
        return this;
    }

    /**
     * El mínimo de participantes requeridos para iniciar el torneo.
     * @param minParticipantes el mínimo de participates escogido por el usuario.
     * @return la instancia actual de TorneoBuilder.
     */
    public TorneoBuilder conMinParticipantes(int minParticipantes) {
        this.minParticipantes = minParticipantes;
        return this;
    }

    /**
     * El máximo de participantes que pueden inscribirse al torneo.
     * @param maxParticipantes el máximo de participante escogido por el usuario.
     * @return la instancia actual de TorneoBuilder.
     */
    public TorneoBuilder conMaxParticipantes(int maxParticipantes) {
        this.maxParticipantes = maxParticipantes;
        return this;
    }

    /**
     * Constructor final del torneo.
     * Revisa que estén todos los datos necesarios y se genera automáticamente su ID único del torneo.
     * @return una instancia válida de la clase Torneo.
     */
    public Torneo build() {
        if (nombre == null || formato == null || disciplina == null || creador == null || fechaInicio == null || fechaFin == null) {
            throw new IllegalStateException("Error, faltan datos para crear el torneo.");
        }

        if (minParticipantes <= 0 || maxParticipantes < minParticipantes) {
            throw new IllegalStateException("Error, los límites son invalidos.");
        }

        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Error, la fecha de inicio no puede ser anterior a hoy.");
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalStateException("Error, la fecha de fin no puede ser anterior a la de inicio.");
        }


        String idTorneo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return new Torneo(nombre, idTorneo, disciplina, formato, creador, minParticipantes, maxParticipantes, fechaInicio, fechaFin);

    }
}