package org.example.modelo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Clase principal
 * Representa un Torneo deportivo o de juegos.
 */

public class Torneo implements Serializable {
    private String nombre;
    private String id;
    private Disciplina disciplina;
    private FormatoTorneo formatoTorneo;
    private Usuario creador;
    private int minParticipantes;
    private int maxParticipantes;
    private List<Usuario> cuentasInscritas;

    private List<Participante> participantes;
    private List<Partido> partidos;

    private transient List<Observador> observadores;

    private List<Integer> rondaLimites;
    private Participante campeon;

    /**
     * Constructor del torneo
     * @param nombre nombre del torneo, no puede ser null.
     * @param disciplina disciplina del torneo, no puede ser null.
     * @param formatoTorneo formato de juego, no puede ser null.
     */

    public Torneo(String nombre, String id, Disciplina disciplina,  FormatoTorneo formatoTorneo, Usuario creador, int minParticipantes, int maxParticipantes) {
        this.nombre = nombre;
        this.id = id;
        this.disciplina = disciplina;
        this.formatoTorneo = formatoTorneo;
        this.creador = creador;
        this.minParticipantes = minParticipantes;
        this.maxParticipantes = maxParticipantes;
        this.participantes = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.rondaLimites = new ArrayList<>();
        this.cuentasInscritas = new ArrayList<>();

    }

    public boolean esDueño(Usuario usuario) {
        return usuario != null && this.creador.getNombreUsuario().equalsIgnoreCase(usuario.getNombreUsuario());
    }

    /**
     * Inscribe a un participante en el torneo.
     * @param participante participante a inscribir, no puede ser null.
     */
    public void agregarParticipante(Participante participante, Usuario usuario) {
        if (participante == null) {
            throw new IllegalArgumentException("El participante no puede ser null.");
        }

        if (!partidos.isEmpty()) {
            throw new IllegalArgumentException("No se pueden inscribir participantes una vez comenzado el torneo.");
        }

        if (this.cuentasInscritas.contains(usuario)) {
            throw new IllegalArgumentException("Error: Tu cuenta de usuario ya registra una inscripción en este torneo.");
        }

        if (participantes.size() >= maxParticipantes) {
            throw new IllegalArgumentException("El torneo ya alcanzó el máximo de " + maxParticipantes + " participantes inscritos.");
        }

        if (participante instanceof Equipo) {
            int cantidad = participante.cantidadDeMiembros();
            int maxPermitido = disciplina.getMaxJugadoresPorEquipo();
            int minPermitido = disciplina.getMinJugadoresPorEquipo();

            if (cantidad > maxPermitido) {
                throw new IllegalArgumentException(participante.getNombre() +
                        " supera el máximo de " + maxPermitido + " participantes permitidos para " + disciplina);
            }
            if (cantidad < minPermitido) {
                throw new IllegalArgumentException(participante.getNombre() +
                        " no cumple el mínimo de " + minPermitido + " participantes requeridos para " + disciplina);
            }
        }

        this.participantes.add(participante);
        this.cuentasInscritas.add(usuario);
        notificarObservadores();
    }

    /**
     * Genera los partidos del torneo según el formato asignado.
     * Una vez creados, notifica a todos los observadores para que las vistas
     * se actualicen automáticamente.
     */
    public void generarPartidos(){
        partidos = formatoTorneo.generarEnfrentamientos(participantes);
        rondaLimites.clear();
        rondaLimites.add(partidos.size());
        notificarObservadores();
    }

    /**
     * Registra el resultado de un partido y actualiza la clasificación.
     * @param partido el partido que finalizó
     * @param resultado el resultado con los puntajes de ambos participantes.
     */
    public void registrarResultado(Partido partido, Resultado resultado){
        partido.registrarResultado(resultado);
        formatoTorneo.actualizarClasificacion(partido);
        avanzarRondaSiCorresponde();
        notificarObservadores();
    }

    private void avanzarRondaSiCorresponde() {
        if (!(formatoTorneo instanceof EliminatoriaDirecta) || rondaLimites.isEmpty()) {
            return;
        }

        int inicioRondaActual = rondaLimites.size() > 1 ? rondaLimites.get(rondaLimites.size() - 2) : 0;
        int finRondaActual = rondaLimites.get(rondaLimites.size() - 1);
        List<Partido> rondaActual = partidos.subList(inicioRondaActual, finRondaActual);

        for (Partido p : rondaActual) {
            if (p.getResultado() == null) {
                return; //aún faltan partidos por jugarse en esta ronda
            }
        }

        List<Participante> ganadores = new ArrayList<>();
        for (Partido p : rondaActual) {
            Resultado r = p.getResultado();
            ganadores.add(r.ganoParticipanteA() ? p.getParticipanteA() : p.getParticipanteB());
        }

        EliminatoriaDirecta eliminatoria = (EliminatoriaDirecta) formatoTorneo;
        Participante conBye = eliminatoria.getParticipanteConBye();
        if (conBye != null && !ganadores.contains(conBye)) {
            ganadores.add(conBye);
        }

        if (ganadores.size() <= 1) {
            this.campeon = ganadores.isEmpty() ? null : ganadores.get(0);
            return;
        }

        List<Partido> siguienteRonda = formatoTorneo.generarEnfrentamientos(ganadores);
        partidos.addAll(siguienteRonda);
        rondaLimites.add(partidos.size());
    }
    /**
     * Agrupa los partidos por ronda
     * @return lista de rondas, cada una con su lista de partidos en orden cronológico.
     */
    public List<List<Partido>> obtenerRondas() {
        List<List<Partido>> rondas = new ArrayList<>();
        int inicio = 0;
        for (int fin : rondaLimites) {
            rondas.add(new ArrayList<>(partidos.subList(inicio, fin)));
            inicio = fin;
        }
        return rondas;
    }
    /**
     * @return el ganador del torneo si ya finalizó (solo aplica a eliminatoria directa), o null si aún no hay campeón.
     */
    public Participante getCampeon() {
        return campeon;
    }

    /**
     * Registra un nuevo observador
     * @param o observador a registrar.
     */
    public void agregarObservador(Observador o){
        if (o != null) {
            observadores.add(o);
        }
    }

    /**
     * Notifica a todos los observadores registrados.
     */
    private void notificarObservadores(){
        for(Observador o: observadores){
            o.actualizar(this);
        }
    }
    public int getMinParticipantes() {
        return minParticipantes;
    }
    public int getMaxParticipantes() {
        return maxParticipantes;
    }
    public String getNombre() {return nombre;}
    public String getId() { return id; }
    public Disciplina getDisciplina() {return disciplina;}
    public FormatoTorneo getFormatoTorneo() {
        return formatoTorneo;
    }
    public Usuario getCreador() { return creador; }
    public List<Participante> getParticipantes() {return participantes;}
    public List<Partido> getPartidos() {return partidos;}

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.observadores = new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.nombre + " - " + this.disciplina + " (ID: " + this.id + ")";
    }


}
