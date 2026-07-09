package org.example.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TorneoTest {

    private Usuario creador;
    private Torneo copaEstrella;

    private Jugador javiera, martin, felipe;
    private Usuario cuentaJavi, cuentaMartin, cuentaFelipe;

    @BeforeEach
    void setUp() {
        // Inicializamos los datos antes de cada prueba para tener todo fresquito
        creador = new Usuario("Admin_Torneos");
        copaEstrella = new Torneo("Copa Estrella", "160407", Disciplina.TENIS,
                new LigaSimple(), creador, 2, 4,
                LocalDate.now(), LocalDate.now().plusDays(7));


        javiera = new Jugador("1604JV", "Javiera", "javi@mail.com");
        martin  = new Jugador("123456", "Martín", "martin@mail.com");
        felipe  = new Jugador("654321", "Felipe", "felipe@mail.com");

        cuentaJavi   = new Usuario("Javiera");
        cuentaMartin = new Usuario("Martin");
        cuentaFelipe = new Usuario("Felipe");
    }



    @Test
    void esDueño_devuelveTrue_paraElCreador() {
        assertTrue(copaEstrella.esDueño(creador));
    }

    @Test
    void esDueño_devuelveFalse_paraOtroUsuario() {
        assertFalse(copaEstrella.esDueño(cuentaJavi));
    }

    @Test
    void esDueño_devuelveFalse_siUsuarioEsNull() {
        assertFalse(copaEstrella.esDueño(null));
    }


    @Test
    void agregarParticipante_inscribeAJavieraCorrectamente() {

        copaEstrella.agregarParticipante(javiera, cuentaJavi);

        assertEquals(1, copaEstrella.getParticipantes().size());
        assertTrue(copaEstrella.getParticipantes().contains(javiera));
    }

    @Test
    void agregarParticipante_lanzaExcepcion_siParticipanteEsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                copaEstrella.agregarParticipante(null, cuentaJavi)
        );
    }

    @Test
    void agregarParticipante_lanzaExcepcion_siLaMismaCuentaSeInscribeDosVeces() {

        copaEstrella.agregarParticipante(javiera, cuentaJavi);

        assertThrows(IllegalArgumentException.class, () ->
                copaEstrella.agregarParticipante(martin, cuentaJavi)
        );
    }

    @Test
    void agregarParticipante_lanzaExcepcion_siSeAlcanzaElMaximoDeCupos() {
        Torneo copaChampiñon = new Torneo("Copa Champiñón", "160407", Disciplina.AJEDREZ,
                new LigaSimple(), creador, 1, 1,
                LocalDate.now(), LocalDate.now().plusDays(7));
        copaChampiñon.agregarParticipante(javiera, cuentaJavi);

        assertThrows(IllegalArgumentException.class, () ->
                copaChampiñon.agregarParticipante(martin, cuentaMartin)
        );
    }

    @Test
    void agregarParticipante_lanzaExcepcion_siElTorneoYaComenzo() {
        copaEstrella.agregarParticipante(javiera, cuentaJavi);
        copaEstrella.agregarParticipante(martin, cuentaMartin);
        copaEstrella.generarPartidos();


        assertThrows(IllegalArgumentException.class, () ->
                copaEstrella.agregarParticipante(felipe, cuentaFelipe)
        );
    }

    @Test
    void agregarParticipante_lanzaExcepcion_siEquipoSuperaMaximoDeDisciplina() {

        Torneo copaFlor = new Torneo("Copa Flor", "1604JV", Disciplina.LEAGUE_OF_LEGENDS,
                new LigaSimple(), creador, 2, 8,
                LocalDate.now(), LocalDate.now().plusDays(7));


        List<Jugador> muchosJugadores = List.of(
                new Jugador("1", "Doran", ""), new Jugador("2", "Oner", ""),
                new Jugador("3", "Faker", ""), new Jugador("4", "Peyz", ""),
                new Jugador("5", "Keria", ""), new Jugador("6", "Gumayusi", ""),
                new Jugador("7", "Zeus", "")
        );
        Equipo equipoMuyGrande = new Equipo("ZOFGK1", "T1", muchosJugadores);

        assertThrows(IllegalArgumentException.class, () ->
                copaFlor.agregarParticipante(equipoMuyGrande, cuentaJavi)
        );
    }

    @Test
    void generarPartidos_creaPartidosSegunElFormatoLiga() {

        copaEstrella.agregarParticipante(javiera, cuentaJavi);
        copaEstrella.agregarParticipante(martin, cuentaMartin);
        copaEstrella.agregarParticipante(felipe, cuentaFelipe);

        copaEstrella.generarPartidos();

        assertEquals(3, copaEstrella.getPartidos().size());
    }

    @Test
    void registrarResultado_actualizaElResultadoDelPartido() {

        copaEstrella.agregarParticipante(javiera, cuentaJavi);
        copaEstrella.agregarParticipante(martin, cuentaMartin);
        copaEstrella.generarPartidos();

        Partido partidoJaviVsMartin = copaEstrella.getPartidos().get(0);


        copaEstrella.registrarResultado(partidoJaviVsMartin, new Resultado(20, 0));


        assertNotNull(partidoJaviVsMartin.getResultado());
        assertEquals(20, partidoJaviVsMartin.getResultado().getPuntajeParticipanteA());
    }

    @Test
    void eliminatoriaDirecta_declaraCampeon_alFinalizarTodasLasRondas() {

        Torneo copaGolf = new Torneo("Copa Golf", "160407", Disciplina.AJEDREZ,
                new EliminatoriaDirecta(), creador, 2, 4,
                LocalDate.now(), LocalDate.now().plusDays(7));

        Jugador jugadorX = new Jugador("X", "Extra", "");

        copaGolf.agregarParticipante(javiera, cuentaJavi);
        copaGolf.agregarParticipante(martin, cuentaMartin);
        copaGolf.agregarParticipante(felipe, cuentaFelipe);
        copaGolf.agregarParticipante(jugadorX, new Usuario("CuentaExtra"));


        copaGolf.generarPartidos();
        assertEquals(2, copaGolf.getPartidos().size());


        copaGolf.registrarResultado(copaGolf.getPartidos().get(0), new Resultado(1, 0));
        copaGolf.registrarResultado(copaGolf.getPartidos().get(1), new Resultado(1, 0));


        assertEquals(3, copaGolf.getPartidos().size());
        assertNull(copaGolf.getCampeon());

        Partido laGranFinal = copaGolf.getPartidos().get(2);
        copaGolf.registrarResultado(laGranFinal, new Resultado(1, 0)); // Javiera Gana

        assertNotNull(copaGolf.getCampeon());
        assertEquals(javiera, copaGolf.getCampeon());
    }

    @Test
    void obtenerRondas_agrupaLosPartidosCorrectamente() {

        Torneo copaRapida = new Torneo("Copa DOO", "1604JV", Disciplina.AJEDREZ,
                new EliminatoriaDirecta(), creador, 2, 4,
                LocalDate.now(), LocalDate.now().plusDays(7));

        copaRapida.agregarParticipante(javiera, cuentaJavi);
        copaRapida.agregarParticipante(martin, cuentaMartin);
        copaRapida.generarPartidos();

        List<List<Partido>> rondas = copaRapida.obtenerRondas();

        assertEquals(1, rondas.size());
        assertEquals(1, rondas.get(0).size());
    }
}