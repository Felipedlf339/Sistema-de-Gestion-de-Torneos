package org.example.modelo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;

/**
 * Clase principal
 * Representa un Torneo deportivo o de juegos.
 * Implementa {@link Serializable} para persistencia de datos.
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

    //Atributos de las fechas
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoTorneo estado;

    /**
     * Constructor del torneo
     * @param nombre nombre del torneo, no puede ser null.
     * @param disciplina disciplina del torneo, no puede ser null.
     * @param formatoTorneo formato de juego, no puede ser null.
     * @param id identificador único asociado al torneo.
     * @param creador usuario asociado como dueño del torneo.
     * @param minParticipantes el mínimo de integrantes inscritos para iniciar el torneo.
     * @param maxParticipantes el máximo de integrantes que pueden inscribirse al torneo.
     */

    public Torneo(String nombre, String id, Disciplina disciplina,  FormatoTorneo formatoTorneo, Usuario creador, int minParticipantes, int maxParticipantes,
                  LocalDate fechaInicio, LocalDate fechaFin) {
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
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = EstadoTorneo.INSCRIPCION;

    }

    /**
     * Verifica si un usuario es el creador del torneo.
     * @param usuario el usuario que se desea identificar.
     * @return si el usuario si es el dueño del torneo, false en caso contrario.
     */
    public boolean esDueño(Usuario usuario) {
        return usuario != null && this.creador.getNombreUsuario().equalsIgnoreCase(usuario.getNombreUsuario());
    }

    /**
     * Para verificar si una cuenta esta inscrita en un torneo especifico.
     * @param usuario la cuenta a verificar.
     * @return true si ya está en la lista de inscripciones.
     */
    public boolean estaInscrito(Usuario usuario) {
        if (this.cuentasInscritas == null) return false;

        for (Usuario u : this.cuentasInscritas) {
            if (u.getNombreUsuario().equalsIgnoreCase(usuario.getNombreUsuario())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inscribe a un participante en el torneo.
     * @param participante participante a inscribir, no puede ser null.
     * @param usuario la cuenta del usuario que realiza la inscripción.
     */
    public void agregarParticipante(Participante participante, Usuario usuario) {
        if (participante == null) {
            throw new IllegalArgumentException("El participante no puede ser null.");
        }

        if (!partidos.isEmpty()) {
            throw new IllegalArgumentException("No se pueden inscribir participantes una vez comenzado el torneo.");
        }

        if (this.cuentasInscritas.contains(usuario) && !this.esDueño(usuario)) {
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
        this.estado = EstadoTorneo.EN_CURSO;
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

    /**
     * Evalúa el estado actual del torneo y ejecuta la lógica de avanzar las rondas
     * correspondiente según el formato definido.
     * Este método actúa como un controlador central:
     * 1. Verifica si existen rondas registradas.
     * 2. Determina el formato de competencia (Eliminatoria Directa o Doble)
     * y delega el proceso de avance al método privado correspondiente.
     * 3. Al finalizar la evaluación de avance, actualiza el estado del torneo
     * a {@link EstadoTorneo#FINALIZADO}.
     */
    private void avanzarRondaSiCorresponde() {
        if (rondaLimites.isEmpty()) return;

        if (formatoTorneo instanceof EliminatoriaDirecta) {
            avanzarEliminatoriaDirecta();
        } else if (formatoTorneo instanceof EliminatoriaDoble) {
            avanzarEliminatoriaDoble();
        }
        this.estado = EstadoTorneo.FINALIZADO;
    }

    /**
     * Avanza el torneo a la siguiente ronda cuando el formato es Eliminatoria Directa.
     * El proceso sigue estos pasos:
     * 1. Verifica que todos los partidos de la ronda actual tengan un resultado registrado.
     * 2. Recopila los ganadores de cada partido.
     * 3. Integra al participante que tuvo un "Bye" (pase directo) en la ronda anterior,
     * si es que aún permanece en competencia.
     * 4. Si solo queda un participante, lo establece como el campeón definitivo del torneo.
     * 5. De lo contrario, genera los enfrentamientos de la siguiente ronda y actualiza
     * los límites de control de rondas.

     */
    private void avanzarEliminatoriaDirecta() {
        int inicioRondaActual = rondaLimites.size() > 1 ? rondaLimites.get(rondaLimites.size() - 2) : 0;
        int finRondaActual = rondaLimites.get(rondaLimites.size() - 1);
        List<Partido> rondaActual = partidos.subList(inicioRondaActual, finRondaActual);

        for (Partido p : rondaActual) {
            if (p.getResultado() == null) return;
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
     * Avanza el torneo a la siguiente ronda cuando el formato es Eliminatoria Doble.
     * Este método verifica que todos los partidos de la ronda actual tengan un resultado registrado.
     * Luego, solicita al formato de torneo que genere los nuevos enfrentamientos.
     * Finalmente, actualiza la lista de partidos del torneo y marca el límite de la nueva ronda.
     * Si no se generan nuevos enfrentamientos, determina y asigna al campeón del torneo.
     */
    private void avanzarEliminatoriaDoble() {
        int inicioRondaActual = rondaLimites.size() > 1 ? rondaLimites.get(rondaLimites.size() - 2) : 0;
        int finRondaActual = rondaLimites.get(rondaLimites.size() - 1);
        List<Partido> rondaActual = partidos.subList(inicioRondaActual, finRondaActual);

        for (Partido p : rondaActual) {
            if (p.getResultado() == null) return;
        }

        EliminatoriaDoble doble = (EliminatoriaDoble) formatoTorneo;
        List<Partido> siguientes = doble.generarSiguienteRonda(rondaActual);

        if (siguientes.isEmpty()) {
            this.campeon = doble.getWinnerBracket().isEmpty() ? null : doble.getWinnerBracket().get(0);
            return;
        }

        partidos.addAll(siguientes);
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

    // Getters.
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
    public EstadoTorneo getEstado() { return estado; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }

    /**
     * Se ejecuta automáticamente al cargar el torneo desde el archivo datos.dat.
     * Se utiliza para volver a iniciar observadores, evitando NullPointerExceptions.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.observadores = new ArrayList<>();
    }

    /**
     * @return representación en texto del torneo.
     */
    @Override
    public String toString() {
        return this.nombre + " - " + this.disciplina + " (ID: " + this.id + ")";
    }


}
