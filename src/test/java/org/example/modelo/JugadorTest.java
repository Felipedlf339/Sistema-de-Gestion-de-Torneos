package org.example.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JugadorTest {

    @Test
    void testCrearYDatos() {
        // creamos un jugador cualquiera para probar
        Jugador jug = new Jugador("999", "Cristiano", "cr7@udec.cl");

        assertEquals("999", jug.getId());
        assertEquals("Cristiano", jug.getNombre());

        // un jugador individual siempre es 1 solo
        assertEquals(1, jug.cantidadDeMiembros());

        // probando el tostring por si acaso
        assertEquals("Cristiano", jug.toString());
    }
}