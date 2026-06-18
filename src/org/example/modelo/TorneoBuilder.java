package org.example.modelo;

// Para configurar a medida cada torneo.
// Hay que agregar algún metodo para generar el torneo con una ID.

public class TorneoBuilder {

    private String nombre;
    private Disciplina disciplina;
    private FormatoTorneo formato;

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

}
