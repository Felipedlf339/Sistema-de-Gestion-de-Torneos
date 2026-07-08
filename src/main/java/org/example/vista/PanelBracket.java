package org.example.vista;

import org.example.modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PanelBracket extends JPanel implements Observador {
    private final VentanaPrincipal ventana;
    private final RegistroTorneos registro;

    private final JLabel lblTitulo;
    private final JPanel panelContenido;
    private final JButton btnGenerar;
    private final JButton btnEliminarTorneo;

    private Torneo torneoActual;
    //Evita registrarse mas de una vez como observadores del mismo torneo cada vez que el usuario
    //Vuelva a esta pantalla.
    private final Set<String> torneoObservados = new HashSet<>();

    private static final Color FONDO = new Color(43, 43, 43);
    private static final Color TARJETA = new Color(60, 63, 65);
    private static final Color GRIS_BOTON = new Color(77, 77, 77);
    private static final Color ACENTO = new Color(249, 190, 17);

    public PanelBracket(VentanaPrincipal ventana, RegistroTorneos registro) {
        this.ventana = ventana;
        this.registro = registro;

        setBackground(FONDO);
        setLayout(new BorderLayout());

        //Nombre del Torneo y la acción de iniciar.
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(FONDO);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        lblTitulo = new JLabel("Torneos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelNorte.add(lblTitulo, BorderLayout.WEST);

        btnGenerar = new JButton("Generar Enfrentamientos ");
        estilizarBoton(btnGenerar);
        btnGenerar.addActionListener(e -> {
            if(torneoActual == null) return;
            if(torneoActual.getParticipantes().size() < 2){
                JOptionPane.showMessageDialog(this,
                        "Se necesitan al menos 2 participantes antes de iniciar un torneo",
                        "Atencion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            torneoActual.generarPartidos();
        });
        panelNorte.add(btnGenerar, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);

        //Contenido dinámico(la tabla o el bracket)
        panelContenido = new JPanel();
        panelContenido.setBackground(FONDO);
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(5, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(panelContenido);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        //Volver al menú.
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBackground(FONDO);
        JButton btnVolver = new JButton("Volver al menú");
        estilizarBoton(btnVolver);
        btnVolver.addActionListener(e -> ventana.cambiarPantalla("MENU_INICIO"));
        panelSur.add(btnVolver);
        add(panelSur, BorderLayout.SOUTH);


        //Eliminar el torneo.
        btnEliminarTorneo = new JButton("Eliminar Torneo");
        estilizarBoton(btnEliminarTorneo);

        btnEliminarTorneo.addActionListener(e -> {
            if (torneoActual == null) return;
            Usuario usuarioActual = registro.getUsuarioActual();

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que deseas eliminar el torneo '" + torneoActual.getNombre() + "'?\nEsta acción borrará a todos los participantes y el progreso.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                registro.eliminarTorneo(torneoActual.getId(), usuarioActual);
                JOptionPane.showMessageDialog(this,
                        "Torneo eliminado con éxito.",
                        "Torneo Eliminado",
                        JOptionPane.INFORMATION_MESSAGE);
                ventana.cambiarPantalla("MENU_INICIO"); // Vuelve al inicio porque el torneo ya no existe.
            }
        });

        panelSur.add(btnEliminarTorneo);
        add(panelSur, BorderLayout.SOUTH);
    }

    public void mostrarTorneo(Torneo torneo) {
        this.torneoActual = torneo;
        if(torneoActual != null && torneoObservados.add(torneo.getId())) {
            torneo.agregarObservador(this);
        }
        actualizar(torneo);
        ventana.cambiarPantalla("BRACKET");
    }

    @Override
    public void actualizar(Torneo torneo) {
        if(torneo == null || torneo != torneoActual) return;
        SwingUtilities.invokeLater(this::repintarContenido);
    }

    private void repintarContenido() {
        if(torneoActual == null) return;
        panelContenido.removeAll();

        boolean esAdmin = torneoActual.esDueño(registro.getUsuarioActual());

        lblTitulo.setText(torneoActual.getNombre() + " - " + torneoActual.getDisciplina()
                    + " -  ID: " + torneoActual.getId());
        btnGenerar.setVisible(esAdmin && torneoActual.getPartidos().isEmpty());

        btnEliminarTorneo.setVisible(esAdmin); // Solo el organizador puede borrar el torneo.

        if(!esAdmin) {
            JLabel lblRol = new JLabel("Modo espectador: Solo el administrador puede ingresar los resultados. ");
            lblRol.setForeground(Color.LIGHT_GRAY);
            lblRol.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            panelContenido.add(lblRol);
        }

        if (torneoActual.getPartidos().isEmpty()){
            JLabel lblVacio = new JLabel("Aun no se generan enfrentamientos. Participantes inscritos: "+
                     torneoActual.getParticipantes().size());
            lblVacio.setForeground(Color.LIGHT_GRAY);
            panelContenido.add(lblVacio);
        } else if (torneoActual.getFormatoTorneo() instanceof LigaSimple){
            construirTablaLiga((LigaSimple) torneoActual.getFormatoTorneo(), esAdmin);
        } else {
            construirBracket(esAdmin);
        }

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    //Generacion de la tabla con puntajes
    private void construirTablaLiga(LigaSimple liga, boolean esAdmin) {
        panelContenido.add(crearSubtitulo("Tabla de posiciones"));

        List<Participante> clasificacion = liga.getClasificacion();
        List<Integer> puntos = liga.getPuntos();

        List<Integer> indices = new ArrayList<>();
        for(int i = 0; i < clasificacion.size(); i++) indices.add(i);
        indices.sort((a, b) -> puntos.get(b) - puntos.get(a));

        int posicion = 1;
        for(int idx : indices) {
            JPanel fila = new  JPanel(new BorderLayout());
            fila.setBackground(TARJETA);
            fila.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            fila.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblPos = new JLabel(posicion + "° " + clasificacion.get(idx).getNombre());
            lblPos.setForeground(Color.WHITE);
            JLabel lblPts = new JLabel(puntos.get(idx) + " pts.");
            lblPts.setForeground(ACENTO);
            fila.add(lblPos, BorderLayout.WEST);
            fila.add(lblPts, BorderLayout.EAST);

            panelContenido.add(fila);
            panelContenido.add(Box.createRigidArea(new Dimension(0, 6)));
            posicion++;
        }
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));
        panelContenido.add(crearSubtitulo("Partidos"));
        for(Partido p : torneoActual.getPartidos()){
            panelContenido.add(crearFilaPartido(p, esAdmin));
            panelContenido.add(Box.createRigidArea(new Dimension(0, 6)));
        }
    }

    //Metodo encargado de construir el bracket del torneo actual
    private void construirBracket(boolean esAdmin) {
        List<List<Partido>> rondas = torneoActual.obtenerRondas();
        int totalRondas = rondas.size();

        //Detectar si hubo Bracket Reset de forma global
        boolean tieneReset = false;
        if (torneoActual.getFormatoTorneo() instanceof EliminatoriaDoble) {
            EliminatoriaDoble doble = (EliminatoriaDoble) torneoActual.getFormatoTorneo();
            //Solo podemos confirmar un reset si ya estamos en la final y hay al menos 2 rondas jugadas
            if (doble.isEnGranFinal() && totalRondas >= 2) {
                Partido pUltimo = rondas.get(totalRondas - 1).get(0);
                Partido pPenultimo = rondas.get(totalRondas - 2).get(0);

                //Revisar si los participantes del último partido son exactamente los mismos del penúltimo
                boolean mismoA = pUltimo.getParticipanteA().equals(pPenultimo.getParticipanteA()) || pUltimo.getParticipanteA().equals(pPenultimo.getParticipanteB());
                boolean mismoB = pUltimo.getParticipanteB().equals(pPenultimo.getParticipanteA()) || pUltimo.getParticipanteB().equals(pPenultimo.getParticipanteB());

                if (mismoA && mismoB) {
                    tieneReset = true;
                }
            }
        }

        int numeroRonda = 1;
        for(List<Partido> ronda : rondas){
            //Por defecto, todas las rondas son "Ronda X" para no fijar numero
            String etiqueta = "Ronda " + numeroRonda;

            if (torneoActual.getFormatoTorneo() instanceof EliminatoriaDoble) {
                EliminatoriaDoble doble = (EliminatoriaDoble) torneoActual.getFormatoTorneo();

                //Solo si el torneo ya terminó empezamos a cambiar los títulos finales
                if (doble.isEnGranFinal()) {
                    if (tieneReset) {
                        //Si hubo revancha, los últimos 3 partidos tienen nombres fijos
                        if (numeroRonda == totalRondas) etiqueta = "Gran Final (Set 2)";
                        else if (numeroRonda == totalRondas - 1) etiqueta = "Gran Final (Set 1)";
                        else if (numeroRonda == totalRondas - 2) etiqueta = "Final de Perdedores";
                    } else {
                        //Si el ganador del Winner Bracket ganó a la primera
                        if (numeroRonda == totalRondas) etiqueta = "Gran Final";
                        else if (numeroRonda == totalRondas - 1) etiqueta = "Final de Perdedores";
                    }
                }

            } else {
                //Lógica limpia para Eliminatoria Directa
                if (numeroRonda == totalRondas && ronda.size() == 1) {
                    etiqueta = "Final";
                } else if (numeroRonda == totalRondas - 1 && ronda.size() == 2) {
                    etiqueta = "Semifinal";
                }
            }

            // Dibujar en la interfaz
            panelContenido.add(crearSubtitulo(etiqueta));
            for(Partido p : ronda){
                panelContenido.add(crearFilaPartido(p, esAdmin));
                panelContenido.add(Box.createRigidArea(new Dimension(0, 6)));
            }
            panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));
            numeroRonda++;
        }

        // Mostrar Campeón
        if(torneoActual.getCampeon() != null ){
            JLabel lblCampeon = new JLabel("Campeón: " + torneoActual.getCampeon().getNombre());
            lblCampeon.setForeground(ACENTO);
            lblCampeon.setFont(new Font("Segoe UI", Font.BOLD, 18));
            panelContenido.add(lblCampeon);
        }
    }

    private JPanel crearFilaPartido(Partido partido, boolean esAdmin){
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(TARJETA);
        fila.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        String nombreA = partido.getParticipanteA().getNombre();
        String nombreB = partido.getParticipanteB().getNombre();
        Resultado resultado = partido.getResultado();

        String texto;
        if (resultado != null) {
            texto = nombreA + "   " + resultado.getPuntajeParticipanteA() + " - " + resultado.getPuntajeParticipanteB() + "   " + nombreB;
        } else {
            texto = nombreA + "   vs   " + nombreB;
        }
        JLabel lblPartido = new JLabel(texto);
        lblPartido.setForeground(Color.WHITE);
        fila.add(lblPartido, BorderLayout.CENTER);

        if(resultado == null && esAdmin){
            JButton btnResultado = new JButton("Ingresar Resultado");
            estilizarBoton(btnResultado);
            btnResultado.addActionListener(e -> abrirDialogoResultado(partido, nombreA, nombreB));
            fila.add(btnResultado, BorderLayout.EAST);
        } else if(resultado != null){
            String textoGanador;
            if (resultado.ganoParticipanteA()) {
                textoGanador = "Gana " + nombreA;
            } else {
                textoGanador = "Gana " + nombreB;
            }
            JLabel lblGanador = new JLabel(textoGanador);
            lblGanador.setForeground(ACENTO);
            fila.add(lblGanador, BorderLayout.EAST);
        }
        return fila;
    }

    private void abrirDialogoResultado(Partido partido, String nombreA, String nombreB){
        JTextField txtA = new JTextField();
        JTextField txtB = new JTextField();

        JLabel lblA = new JLabel("Puntaje de " + nombreA + ":");
        lblA.setForeground(Color.WHITE);
        JLabel lblB = new JLabel("Puntaje de " + nombreB + ":");
        lblB.setForeground(Color.WHITE);
        JPanel panelDialogo = new JPanel(new GridLayout(2, 2, 5, 5));
        panelDialogo.setBackground(FONDO);
        panelDialogo.add(lblA);
        panelDialogo.add(txtA);
        panelDialogo.add(lblB);
        panelDialogo.add(txtB);

        int opcion = JOptionPane.showConfirmDialog(this, panelDialogo, "Ingresar Resultado", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(opcion == JOptionPane.OK_OPTION){
            try{
                int puntajeA = Integer.parseInt(txtA.getText());
                int puntajeB = Integer.parseInt(txtB.getText());
                torneoActual.registrarResultado(partido, new Resultado(puntajeA, puntajeB));
            } catch(NumberFormatException ex){
                JLabel lblError = new JLabel("Ingrese puntaje numérico válido.");
                lblError.setForeground(Color.WHITE);
                JOptionPane.showMessageDialog(this, lblError,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JLabel crearSubtitulo(String texto){
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(ACENTO);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return lbl;
    }

    private void estilizarBoton(JButton btn){
        btn.setBackground(GRIS_BOTON);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
    }
}
