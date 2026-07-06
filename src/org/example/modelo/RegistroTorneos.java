package org.example.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroTorneos {

    private final List<Torneo> listaTorneos;
    private final List<Usuario> listaUsuarios;
    private Usuario usuarioActual;

    public RegistroTorneos() {
        this.listaTorneos = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
    }

    public Usuario iniciarSesionORegistrar(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            return null;
        }
        String username = nombreUsuario.trim().toLowerCase();

        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().trim().toLowerCase().equals(username)) {
                this.usuarioActual = u;
                return u;
            }
        }

        Usuario nuevo = new Usuario(nombreUsuario.trim());
        listaUsuarios.add(nuevo);
        this.usuarioActual = nuevo;
        return nuevo;
    }

    public boolean registrarTorneo(Torneo torneo) {
        if (torneo == null || torneo.getId() == null) {
            return false;
        }
        if (buscarTorneoPorInvitacion(torneo.getId()) != null) {
            return false;
        }
        listaTorneos.add(torneo);
        return true;
    }

    public Torneo buscarTorneoPorInvitacion(String codigoInvitacion) {
        if (codigoInvitacion == null) {
            return null;
        }

        for (Torneo t : listaTorneos) {
            if (t.getId().equalsIgnoreCase(codigoInvitacion)) {
                return t;
            }
        }
        return null;
    }

    public List<Torneo> obtenerTodosLosTorneos() {
        return Collections.unmodifiableList(new ArrayList<>(listaTorneos));
    }

    public List<Torneo> obtenerMisTorneosCreados() {
        List<Torneo> misTorneos = new ArrayList<>();
        if (usuarioActual == null) {
            return misTorneos;
        }
        for (Torneo t : listaTorneos) {
            if (t.esDueño(usuarioActual)) {
                misTorneos.add(t);
            }
        }
        return misTorneos;
    }

    public boolean eliminarTorneo(String codigoInvitacion, Usuario usuarioSolicitante) {
        Torneo torneo = buscarTorneoPorInvitacion(codigoInvitacion);
        if (torneo != null && torneo.esDueño(usuarioSolicitante)) {
            listaTorneos.remove(torneo);
            return true;
        }
        return false;
    }

    public int cantidadTorneos() {
        return listaTorneos.size();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}