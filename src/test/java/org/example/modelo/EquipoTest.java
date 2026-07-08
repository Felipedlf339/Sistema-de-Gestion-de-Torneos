package org.example.modelo;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EquipoTest {

    @Test
    void testCreacionYGetters() {
        // armamos la lista de prueba
        List<Jugador> lista = new ArrayList<>();
        Jugador j1 = new Jugador("1", "vincent", "777");
        Jugador j2 = new Jugador("2", "derek", "Sin contacto");

        lista.add(j1);
        lista.add(j2);

        Equipo eq = new Equipo("EQ-1", "Los Marcianos", lista);

        assertEquals("EQ-1", eq.getId());
        assertEquals("Los Marcianos", eq.getNombre());

        // comprobamos que la lista se guardo bien
        assertEquals(lista, eq.getIntegrantes());
    }

    @Test
    void testCantidadDeMiembros() {
        List<Jugador> lista = new ArrayList<>();
        lista.add(new Jugador("1", "neymar", "ancelotti@gmail.cl"));
        lista.add(new Jugador("2", "vinicius", "Sin contacto"));
        lista.add(new Jugador("3", "casemiro", "+76455736"));

        Equipo eq =  new Equipo("EQ-2", "JOGObonito", lista);

        // se metierom 3 jugadores asi que deberia dar 3
        assertEquals(3, eq.cantidadDeMiembros());
    }
}