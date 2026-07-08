package org.example.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EliminatoriaDirectaTest {

    private EliminatoriaDirecta elim;
    private Jugador j1, j2, j3;

    @BeforeEach
    void init() {
        elim = new EliminatoriaDirecta();
        j1 = new Jugador("1", "mart", "cr7@ucatolica.net");
        j2 = new Jugador("2", "jav", "messilover@penales.com");
        j3 = new Jugador("3", "fel", "grietadelinvocador@lol.cl");
    }

    @Test
    void testGenerarPares() {
        List<Participante> lista = new ArrayList<>();
        lista.add(j1);
        lista.add(j2);

        List<Partido> partidos = elim.generarEnfrentamientos(lista);
        assertEquals(1, partidos.size()); // 2 personas = 1 partido
        assertNull(elim.getParticipanteConBye());
    }

    @Test
    void testParticipanteImpar() {
        List<Participante> lista = new ArrayList<>();
        lista.add(j1); lista.add(j2); lista.add(j3);

        List<Partido> partidos = elim.generarEnfrentamientos(lista);

        assertEquals(1, partidos.size());
        // el tercero deberia quedar sobrando
        assertNotNull(elim.getParticipanteConBye());
        assertEquals(j3, elim.getParticipanteConBye());
    }

    @Test
    void testExcepcionEnEmpate() {
        Partido p = new Partido(j1, j2);
        p.registrarResultado(new Resultado(2, 2)); // empate

        // probando que salte el error que programó felipe creo
        assertThrows(IllegalStateException.class, () -> {
            elim.actualizarClasificacion(p);
        });
    }
}