package org.example.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;

/**
 * La base de datos del sistema.
 * Se encarga de guardar los datos de registro, torneos creados, entre otros.
 */
public class RegistroTorneos {

    private final List<Torneo> listaTorneos;
    private final List<Usuario> listaUsuarios;
    private Usuario usuarioActual;

    /**
     * Constructor que inicia con listas vacías.
     */
    public RegistroTorneos() {
        this.listaTorneos = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
    }

    /**
     * Inicia sesión si el usuario ya existe y se registra si es uno nuevo.
     * La asociación se hace ignorando mayús.
     * @param nombreUsuario el nombre de usuario escrito por el usuario.
     * @return el Usuario con el que se inició sesión.
     */
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

    /**
     * Se registra en los datos un nuevo torneo.
     * @param torneo el torneo que se desea guardar.
     * @return true si no existen problemas en el registro, false si el torneo es null o la ID ya existe.
     */
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

    /**
     * Busca un torneo específico asociado a su ID.
     * @param codigoInvitacion el código identificador de 6 letras único del torneo.
     * @return el torneo encontrado o null si es que no existe un torneo asociado al ID.
     */
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

    /**
     * @return lista de torneos no modificable.
     */
    public List<Torneo> obtenerTodosLosTorneos() {
        return Collections.unmodifiableList(new ArrayList<>(listaTorneos));
    }

    /**
     * Para obtener los torneos a los cuales el usuario actual esté inscrito o sea el organizador.
     * @param usuarioActual el usuario que inició sesión.
     * @return la lista de torneos de los que forma parte el usuario.
     */
    public List<Torneo> obtenerMisTorneos(Usuario usuarioActual) {
        List<Torneo> misTorneos = new ArrayList<>();

        for (Torneo t : this.listaTorneos) {
            if (t.esDueño(usuarioActual) || t.estaInscrito(usuarioActual)) {
                misTorneos.add(t);
            }
        }
        return misTorneos;
    }

    /**
     * Se elimina el registro del torneo siempre y cuando el usuario actual sea el organizador.
     * @param codigoInvitacion ID del torneo a eliminar.
     * @param usuarioSolicitante quien es el usuario que quiere eliminarlo.
     * @return true si se elimino correctamente.
     */
    public boolean eliminarTorneo(String codigoInvitacion, Usuario usuarioSolicitante) {
        Torneo torneo = buscarTorneoPorInvitacion(codigoInvitacion);
        if (torneo != null && torneo.esDueño(usuarioSolicitante)) {
            listaTorneos.remove(torneo);
            return true;
        }
        return false;
    }

    /**
     * Guarda los datos de usuario y torneos en un archivo local (datos.dat).
     */
    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datos.dat"))) {
            oos.writeObject(this.listaTorneos);
            oos.writeObject(this.listaUsuarios);
            System.out.println("Datos guardados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de torneos y los usuarios desde el archivo local.
     * Si el archivo no existe (primera vez que se abre) no hace nada.
     */
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

    // Getters.
    public List<Torneo> getListaTorneos() {
        return this.listaTorneos;
    }

    public int cantidadTorneos() {
        return listaTorneos.size();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Cierra la sesión de la cuenta activa por medio de null.
     */
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}