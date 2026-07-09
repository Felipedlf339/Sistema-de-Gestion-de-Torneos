package org.example.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegistroTorneosTest {

    private RegistroTorneos registro;

    @BeforeEach
    void setUp() {
        registro = new RegistroTorneos();
    }

    @Test
    void iniciarSesionORegistrar_creaUsuarioNuevo_siNoExiste() {
        Usuario u = registro.iniciarSesionORegistrar("javiera");
        assertNotNull(u);
        assertEquals("javiera", u.getNombreUsuario());
        assertEquals(u, registro.getUsuarioActual());
    }

    @Test
    void iniciarSesionORegistrar_reutilizaUsuarioExistente_ignorandoMayusculas() {
        Usuario primero = registro.iniciarSesionORegistrar("Javiera");
        Usuario segundo = registro.iniciarSesionORegistrar("javiera");

        assertSame(primero, segundo);
    }

    @Test
    void iniciarSesionORegistrar_devuelveNull_siNombreEsVacio() {
        assertNull(registro.iniciarSesionORegistrar("   "));
    }

    @Test
    void cerrarSesion_dejaUsuarioActualEnNull() {
        registro.iniciarSesionORegistrar("Javiera");
        registro.cerrarSesion();
        assertNull(registro.getUsuarioActual());
    }


    private Torneo crearTorneoDePrueba(String id, Usuario creador) {
        return new Torneo("Torneo", id, Disciplina.AJEDREZ, new LigaSimple(), creador, 2, 4,
                LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Test
    void registrarTorneo_seGuardaCorrectamente() {
        Usuario creador = registro.iniciarSesionORegistrar("admin");
        Torneo torneo = crearTorneoDePrueba("AB0416", creador);

        assertTrue(registro.registrarTorneo(torneo));
        assertEquals(1, registro.cantidadTorneos());
    }

    @Test
    void registrarTorneo_fallaSiElIdYaExiste() {
        Usuario creador = registro.iniciarSesionORegistrar("admin");
        registro.registrarTorneo(crearTorneoDePrueba("160407", creador));

        boolean segundoRegistro = registro.registrarTorneo(crearTorneoDePrueba("160407", creador));
        assertFalse(segundoRegistro);
        assertEquals(1, registro.cantidadTorneos());
    }

    @Test
    void buscarTorneoPorInvitacion_encuentraTorneo_ignorandoMayusculas() {
        Usuario creador = registro.iniciarSesionORegistrar("admin");
        registro.registrarTorneo(crearTorneoDePrueba("abc123", creador));

        Torneo encontrado = registro.buscarTorneoPorInvitacion("ABC123");
        assertNotNull(encontrado);
        assertEquals("abc123", encontrado.getId());
    }

    @Test
    void buscarTorneoPorInvitacion_devuelveNull_siNoExiste() {
        assertNull(registro.buscarTorneoPorInvitacion("NO_EXISTE"));
    }

    @Test
    void eliminarTorneo_funciona_siElUsuarioEsElDueño() {
        Usuario creador = registro.iniciarSesionORegistrar("admin");
        registro.registrarTorneo(crearTorneoDePrueba("160407", creador));

        assertTrue(registro.eliminarTorneo("160407", creador));
        assertEquals(0, registro.cantidadTorneos());
    }

    @Test
    void eliminarTorneo_falla_siElUsuarioNoEsElDueño() {
        Usuario creador = registro.iniciarSesionORegistrar("admin");
        Usuario otro = new Usuario("random");
        registro.registrarTorneo(crearTorneoDePrueba("160407", creador));

        assertFalse(registro.eliminarTorneo("160407", otro));
        assertEquals(1, registro.cantidadTorneos());
    }


    @Test
    void obtenerMisTorneosCreados_filtraSoloLosDelUsuarioActual() {
        Usuario admin = registro.iniciarSesionORegistrar("admin");
        registro.registrarTorneo(crearTorneoDePrueba("160407", admin));

        Usuario otro = new Usuario("lol");
        registro.registrarTorneo(crearTorneoDePrueba("1604JV", otro));

        List<Torneo> misTorneos = registro.obtenerMisTorneos(admin);
        assertEquals(1, misTorneos.size());
        assertEquals("160407", misTorneos.get(0).getId());
    }

    @Test
    void obtenerMisTorneosCreados_devuelveVacio_siNoHayUsuarioActual() {
        assertTrue(registro.obtenerMisTorneos(new Usuario(null)).isEmpty());
    }
}