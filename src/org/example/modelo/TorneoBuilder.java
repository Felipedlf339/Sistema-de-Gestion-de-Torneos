package org.example.modelo;

import java.util.UUID;

public class TorneoBuilder {

    private String nombre;
    private Disciplina disciplina;
    private FormatoTorneo formato;
    private Usuario creador;

    public TorneoBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public TorneoBuilder conDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
        return this;
    }

    public TorneoBuilder conFormato(FormatoTorneo formato) {
        this.formato = formato;
        return this;
    }

    public TorneoBuilder conCreador(Usuario creador) {
        this.creador = creador;
        return this;
    }

    public Torneo build() {
        if (nombre == null || formato == null || disciplina == null || creador == null) {
            throw new IllegalStateException("Error, faltan datos para crear el torneo.");
        }

        String idTorneo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        return new Torneo(idTorneo, nombre, disciplina, formato, creador);
    }
}