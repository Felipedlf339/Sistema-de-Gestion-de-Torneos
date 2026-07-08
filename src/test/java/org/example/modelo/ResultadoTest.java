package org.example.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResultadoTest {

    @Test
    void testGanadorA() {
        // puntaje mayor para el participante A

        Resultado res = new Resultado(5, 2);

        assertTrue(res.ganoParticipanteA());
    }

    @Test
    void testNoGanaA() {
        Resultado res =new Resultado(1, 3);
        assertFalse(res.ganoParticipanteA());

        // prueba de empate
        Resultado empate = new Resultado(2,2);
        assertFalse(empate.ganoParticipanteA());
    }


    @Test
    void testGetters() {
        Resultado res = new Resultado(10, 5);
        assertEquals(10, res.getPuntajeParticipanteA());
        assertEquals(5, res.getPuntajeParticipanteB());
    }
}