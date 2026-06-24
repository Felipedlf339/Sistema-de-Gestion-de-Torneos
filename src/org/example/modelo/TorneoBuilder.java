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

    /**
     * Valida que nombre, disciplina y formato esten presentes a la hora de construir.
     * Si alguno falta, lanza una excepción con un mensaje claro, evitando NullPointerException posibles.
     * @return instancia de torneo listo para usar
     * @throws IllegalStateException si nombre, disciplina o formato son null.
     */
    public Torneo build() {
        if(nombre==null || formato==null ||  disciplina==null) {
            throw new IllegalStateException("Error, faltan datos para crear el torneo. ");
        }
        return new Torneo(nombre, disciplina, formato);
    }

}
