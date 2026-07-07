package org.example.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;

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

    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datos.dat"))) {
            oos.writeObject(this.listaTorneos);
            oos.writeObject(this.listaUsuarios);
            System.out.println("Datos guardados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public void cargarDatos() {
        File archivo = new File("datos.dat");
        if (!archivo.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            List<Torneo> torneosGuardados = (List<Torneo>) ois.readObject();
            List<Usuario> usuariosGuardados = (List<Usuario>) ois.readObject();

            this.listaTorneos.clear();
            this.listaTorneos.addAll(torneosGuardados);

            this.listaUsuarios.clear();
            this.listaUsuarios.addAll(usuariosGuardados);
            System.out.println("Datos cargados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    public List<Torneo> getListaTorneos() {
        return this.listaTorneos;
    }

    public int cantidadTorneos() {
        return listaTorneos.size();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}