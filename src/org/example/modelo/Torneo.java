package org.example.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal
 * Representa un Torneo deportivo o de juegos.
 */

public class Torneo {
    private String nombre;
    private Disciplina disciplina;
    private FormatoTorneo formatoTorneo;
    private List<Participante> participantes;
    private List<Partido> partidos;
    private List<Observador> observadores;

    /**
     * Constructor del torneo
     * @param nombre nombre del torneo, no puede ser null.
     * @param disciplina disciplina del torneo, no puede ser null.
     * @param formatoTorneo formato de juego, no puede ser null.
     */
    public Torneo(String nombre, Disciplina disciplina,  FormatoTorneo formatoTorneo) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.formatoTorneo = formatoTorneo;
        this.participantes = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    /**
     * Inscribe a un participante en el torneo.
     * @param participante participante a inscribir, no puede ser null.
     */
    public void agregarParticipante(Participante participante){
        this.participantes.add(participante);
    }

    /**
     * Genera los partidos del torneo según el formato asignado.
     * Una vez creados, notifica a todos los observadores para que las vistas
     * se actualicen automáticamente.
     */
    public void generarPartidos(){
        partidos = formatoTorneo.generarEnfrentamientos(participantes);
        notificarObservadores();
    }

    /**
     * Registra el resultado de un partido y actualiza la clasificación.
     * @param partido el partido que finalizó
     * @param resultado el resultado con los puntajes de ambos participantes.
     */
    public void registrarResultado(Partido partido, Resultado resultado){
        partido.registrarResultado(resultado);
        formatoTorneo.actualizarClasificacion(partido);
        notificarObservadores();
    }

    /**
     * Registra un nuevo observador
     * @param o observador a registrar.
     */
    private void agregarObservador(Observador o){
        observadores.add(o);
    }

    /**
     * Notifica a todos los observadores registrados.
     */
    private void notificarObservadores(){
        for(Observador o: observadores){
            o.actualizar(this);
        }
    }

    public String getNombre() {return nombre;}
    public Disciplina getDisciplina() {return disciplina;}
    public List<Participante> getParticipantes() {return participantes;}
    public List<Partido> getPartidos() {return partidos;}



}
