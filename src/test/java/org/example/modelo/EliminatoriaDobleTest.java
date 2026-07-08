package org.example.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EliminatoriaDobleTest {

    private EliminatoriaDoble elim;
    private Jugador j1, j2, j3, j4;

    @BeforeEach
    void setup() {
        elim = new EliminatoriaDoble();
        j1 = new Jugador("1", "beckenbauer", "alemania@gmail.com");
        j2 = new Jugador("2", "rivaldo", "brasil@gmail.com");
        j3 = new Jugador("3", "maldini", "italia@gmail.com");
        j4 = new Jugador("4", "elias figueroa", "chile@gmail.com");
    }

    @Test
    void testExcepcionEmpate() {
        Partido p = new Partido(j1, j2);
        p.registrarResultado(new Resultado(1, 1));

        // comprobando que tire error el empate, igual que en eliminatoria directa
        assertThrows(IllegalStateException.class, () -> {
            elim.actualizarClasificacion(p);
        });
    }

    @Test
    void testTorneoCompleto4Jugadores() {
        List<Participante> inscritos = new ArrayList<>();
        inscritos.add(j1); inscritos.add(j2); inscritos.add(j3); inscritos.add(j4);

        // RONDA 1
        List<Partido> r1 = elim.generarEnfrentamientos(inscritos);
        assertEquals(2, r1.size()); // j1 vs j2 y j3 vs j4

        r1.get(0).registrarResultado(new Resultado(2, 0)); // gana j1
        r1.get(1).registrarResultado(new Resultado(2, 0)); // gana j3

        // RONDA 2
        List<Partido> r2 = elim.generarSiguienteRonda(r1);
        // deberia haber 1 partido en winner y 1 partido en loser
        // winner: j1 vs j3   loser: j2 vs j4
        assertEquals(2, r2.size());

        r2.get(0).registrarResultado(new Resultado(3,1)); // j1 le gana a j3 en winner
        r2.get(1).registrarResultado(new Resultado(2,1)); // j2 elimina a j4 en loser

        // RONDA 3
        // j3 cayo al loser bracket y ahora juega contra j2
        List<Partido> r3 = elim.generarSiguienteRonda(r2);
        assertEquals(1, r3.size());

        r3.get(0).registrarResultado(new Resultado(2, 0)); // j3 se recupera y le gana a j2

        // GRAN FINAL
        // j1 que viene invicto vs j3 que viene del loser
        List<Partido> r4 = elim.generarSiguienteRonda(r3);
        assertTrue(elim.isEnGranFinal());
        assertEquals(1, r4.size());

        // hacemos que j3 gane para forzar el reset del bracket (revancha)
        r4.get(0).registrarResultado(new Resultado(1, 2)); // gana j3

        // REVANCHA
        List<Partido> r5 = elim.generarSiguienteRonda(r4);
        assertEquals(1, r5.size());

        // ahora si gana j1 definitivo
        r5.get(0).registrarResultado(new Resultado(3, 0));

        // TERMINA EL TORNEO
        List<Partido> r6 = elim.generarSiguienteRonda(r5);

        assertEquals(0, r6.size()); // ya no hay mas partidos que generar
        assertEquals(1, elim.getWinnerBracket().size()); // j1 es el campeón
        assertEquals(j1, elim.getWinnerBracket().get(0));
    }
}