package org.example.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LigaSimpleTest {

    private LigaSimple liga;
    private Jugador p1, p2, p3;

    @BeforeEach
    void setup() {
        liga = new LigaSimple();
        p1 = new Jugador("1", "lalaland", "amsdas@oscar");
        p2 = new Jugador("2", "Ryan", "+56354534");
        p3 = new Jugador("3", "Gosling", "+5634244");
    }

    @Test
    void testGenerarPartidos() {
        List<Participante> lista =new ArrayList<>();
        lista.add(p1);
        lista.add(p2);
        lista.add(p3);

        // con 3 equipos deberian ser 3 partidos en total
        List<Partido> partidos = liga.generarEnfrentamientos(lista);

        assertEquals(3, partidos.size());

        assertEquals(3, liga.getClasificacion().size());
        assertEquals(0, liga.getPuntos().get(0)); // todos parten en 0
    }

    @Test
    void testSumaDePuntos() {
        List<Participante> lista = new ArrayList<>();
        lista.add(p1);
        lista.add(p2);
        liga.generarEnfrentamientos(lista);

        Partido partido = new Partido(p1, p2);
        partido.registrarResultado(new Resultado(2, 0)); // gana p1

        liga.actualizarClasificacion(partido);

        // p1 deberia tener 3 puntos y p2 deberia tener 0
        int indiceP1 = liga.getClasificacion().indexOf(p1);
        int indiceP2 = liga.getClasificacion().indexOf(p2);

        assertEquals(3, liga.getPuntos().get(indiceP1));
        assertEquals(0, liga.getPuntos().get(indiceP2));
    }

    @Test
    void testEmpateSumaUno() {
        List<Participante> lista = new ArrayList<>();
        lista.add(p1); lista.add(p2);
        liga.generarEnfrentamientos(lista);

        Partido partido = new Partido(p1, p2);
        partido.registrarResultado(new Resultado(1, 1));

        liga.actualizarClasificacion(partido);

        assertEquals(1, liga.getPuntos().get(0));
        assertEquals(1, liga.getPuntos().get(1));
    }
}