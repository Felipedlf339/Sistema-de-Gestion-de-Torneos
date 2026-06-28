package org.example.modelo;
import java.util.UUID;

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
        if (nombre == null || formato == null || disciplina == null) {
            throw new IllegalStateException("Error, faltan datos para crear el torneo. ");
        }

            String idTorneo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

            return new Torneo(idTorneo, nombre, disciplina, formato);
        }

}
