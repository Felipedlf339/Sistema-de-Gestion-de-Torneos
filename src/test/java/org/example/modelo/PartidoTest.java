package org.example.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PartidoTest {

    @Test
    void testCrearYRegistrar() {
        // creo un par de jugadores de prueba rapido
        Jugador j1 = new Jugador("1", "vidal", "+5694378247");
        Jugador j2 = new Jugador("2", "pinacho", "sin");

        Partido partido = new Partido(j1, j2);

        assertEquals(j1, partido.getParticipanteA());
        assertEquals(j2, partido.getParticipanteB());

        // al principio no deberia haber resultado
        assertNull(partido.getResultado());

        Resultado res = new Resultado(3, 1);
        partido.registrarResultado(res);

        assertNotNull(partido.getResultado());
        assertEquals(res, partido.getResultado());
    }
}