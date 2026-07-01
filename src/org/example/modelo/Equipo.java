package org.example.modelo;
import java.util.List;

public class Equipo implements Participante {
    private String id;
    private String nombre;
    private List<Jugador> integrantes;

    public Equipo(String id, String nombreEquipo, List<Jugador> integrantes) {
        this.id = id;
        this.nombre = nombre;
        this.integrantes = integrantes;
    }
    @Override
    public String getId() {return id;}
    @Override
    public String getNombre() {return nombre;}
    @Override
    public int cantidadDeMiembros() {return integrantes.size();}

    public List<Jugador> getIntegrantes() {return integrantes;}

    @Override
    public String toString() { return nombre;}

}
