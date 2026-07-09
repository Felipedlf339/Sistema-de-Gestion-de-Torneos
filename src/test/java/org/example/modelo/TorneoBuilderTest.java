package org.example.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class TorneoBuilderTest {

    private TorneoBuilder builderValido() {
        return new TorneoBuilder()
                .conNombre("Mundial")
                .conDisciplina(Disciplina.FUTBOL)
                .conFormato(new LigaSimple())
                .conCreador(new Usuario("admin"))
                .conMinParticipantes(2)
                .conMaxParticipantes(8)
                .conFechas(LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Test
    void build_creaTorneoCorrectamente_conDatosValidos() {
        Torneo torneo = builderValido().build();

        assertEquals("Mundial", torneo.getNombre());
        assertEquals(Disciplina.FUTBOL, torneo.getDisciplina());
        assertEquals(2, torneo.getMinParticipantes());
        assertEquals(8, torneo.getMaxParticipantes());
        assertNotNull(torneo.getId());
        assertEquals(6, torneo.getId().length());
    }

    @Test
    void build_generaIdsDiferentes_enCadaLlamada() {
        Torneo torneo1 = builderValido().build();
        Torneo torneo2 = builderValido().build();
        assertNotEquals(torneo1.getId(), torneo2.getId());
    }

    @Test
    void build_lanzaExcepcion_siFaltaNombre() {
        TorneoBuilder builder = new TorneoBuilder()
                .conDisciplina(Disciplina.FUTBOL)
                .conFormato(new LigaSimple())
                .conCreador(new Usuario("admin"))
                .conMinParticipantes(2)
                .conMaxParticipantes(8);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void build_lanzaExcepcion_siFaltaCreador() {
        TorneoBuilder builder = new TorneoBuilder()
                .conNombre("Mundial")
                .conDisciplina(Disciplina.FUTBOL)
                .conFormato(new LigaSimple())
                .conMinParticipantes(2)
                .conMaxParticipantes(8);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void build_lanzaExcepcion_siMinParticipantesEsCeroONegativo() {
        TorneoBuilder builder = builderValido().conMinParticipantes(0);
        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void build_lanzaExcepcion_siMaxEsMenorQueMin() {
        TorneoBuilder builder = builderValido().conMinParticipantes(10).conMaxParticipantes(5);
        assertThrows(IllegalStateException.class, () -> builder.build());
    }
}