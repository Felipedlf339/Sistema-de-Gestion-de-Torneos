package org.example.modelo;

import java.util.ArrayList;
import java.util.List;

public class Torneo {
    private String nombre;
    private Disciplina disciplina;
    private FormatoTorneo formatoTorneo;
    private List<Participante> participantes;
    private List<Partido> partidos;
    private List<Observador> observadores;

    public Torneo(String nombre, Disciplina disciplina,  FormatoTorneo formatoTorneo) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.formatoTorneo = formatoTorneo;
        this.participantes = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    public void agregarParticipante(Participante participante){
        this.participantes.add(participante);
    }
    public void generarPartidos(){
        partidos = formatoTorneo.generarEnfrentamientos(participantes);
        notificarObservadores();
    }

    public void registrarResultado(Partido partido, Resultado resultado){
        partido.registrarResultado(resultado);
        formatoTorneo.actualizarClasificacion(partido);
        notificarObservadores();
    }

    private void agregarObservador(Observador o){
        observadores.add(o);
    }
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
