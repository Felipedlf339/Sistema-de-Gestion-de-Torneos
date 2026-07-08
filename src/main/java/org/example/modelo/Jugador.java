package org.example.modelo;
import java.io.Serializable;


public class Jugador implements Participante , Serializable {
    //Datos identificadores del jugador
    private String id;
    private String nombre;
    private String contacto;

    /**
     * Constructor que crea un jugador con sus datos básicos.
     * @param id    identificador único.
     * @param nombre nombre completo.
     * @param contacto dato de contacto.
     */
    public Jugador(String id, String nombre, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
    }
    @Override
    public String getId() {return id;}
    @Override
    public String getNombre() {return nombre;}
    @Override
    public int cantidadDeMiembros() {return 1;}
    @Override
    public String toString(){ return nombre;}







}
