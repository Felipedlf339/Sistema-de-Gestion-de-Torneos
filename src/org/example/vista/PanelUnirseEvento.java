package org.example.vista;

import org.example.modelo.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla que permite a un usuario buscar un torneo existente mediante su ID e inscribirse como participante.
 * Dependiendo de la disciplina se inscribe un equipo o un solo jugador.
 */
public class PanelUnirseEvento extends JPanel {
    private JTextField txtIdTorneo;
    private JTextField txtNombreJugador;
    private JButton btnBuscar;
    private JButton btnInscribirse;
    private JButton btnVolver;
    private JLabel lblEstadoTorneo;

    // Guardamos temporalmente el torneo encontrado
    private Torneo torneoEncontrado;

    // Campos globales de interfaz para la gestión dinámica de equipos y contactos
    private JLabel lblNombre;
    private JLabel lblIntegrantes;
    private JTextArea txtIntegrantes;
    private JScrollPane scrollIntegrantes;

    private JLabel lblContactos;
    private JTextArea txtContactos;
    private JScrollPane scrollContactos;

    /**
     * Constructor del panel de búsqueda e inscripción.
     * @param ventana Referencia a la ventana principal para navegación.
     * @param registro Base de datos central para buscar el torneo.
     */
    public PanelUnirseEvento(VentanaPrincipal ventana, RegistroTorneos registro) {
        // fondo
        setBackground(new Color(43, 43, 43));
        setLayout(new BorderLayout());

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color darkGray = new Color(69, 73, 74);
        Color btnGray = new Color(77, 77, 77);

        // Parte Superior
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panelBusqueda.setBackground(new Color(43, 43, 43));

        JLabel lblId = new JLabel("Código de Invitación (ID):");
        lblId.setForeground(Color.WHITE);
        lblId.setFont(labelFont);
        panelBusqueda.add(lblId);

        txtIdTorneo = new JTextField(10);
        txtIdTorneo.setFont(fieldFont);
        txtIdTorneo.setBackground(darkGray);
        txtIdTorneo.setForeground(Color.WHITE);
        txtIdTorneo.setCaretColor(Color.WHITE);
        panelBusqueda.add(txtIdTorneo);

        // Botón para buscar un torneo con ese id
        btnBuscar = new JButton("Buscar Torneo");
        btnBuscar.setBackground(btnGray);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.setOpaque(true);
        panelBusqueda.add(btnBuscar);

        // Parte Central
        JPanel panelInscripcion = new JPanel(new GridBagLayout());
        panelInscripcion.setBackground(new Color(43, 43, 43));
        panelInscripcion.setBorder(BorderFactory.createTitledBorder(null, "Datos de Inscripción",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, labelFont, Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // texto para mostrar si se seleccionó algún evento
        lblEstadoTorneo = new JLabel("");
        lblEstadoTorneo.setForeground(Color.LIGHT_GRAY);
        lblEstadoTorneo.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelInscripcion.add(lblEstadoTorneo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        lblNombre = new JLabel("Nombre del Jugador/Equipo:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(labelFont);
        panelInscripcion.add(lblNombre, gbc);

        txtNombreJugador = new JTextField(15);
        txtNombreJugador.setFont(fieldFont);
        txtNombreJugador.setBackground(darkGray);
        txtNombreJugador.setForeground(Color.WHITE);
        txtNombreJugador.setCaretColor(Color.WHITE);
        txtNombreJugador.setEnabled(false);
        gbc.gridx = 1;
        panelInscripcion.add(txtNombreJugador, gbc);

        // Zona para ingresar los integrantes en caso de ser una disciplina por equipos
        lblIntegrantes = new JLabel("Integrantes (separados por saltos de línea):");
        lblIntegrantes.setForeground(Color.WHITE);
        lblIntegrantes.setFont(labelFont);
        lblIntegrantes.setVisible(false);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelInscripcion.add(lblIntegrantes, gbc);

        txtIntegrantes = new JTextArea(3, 15);
        txtIntegrantes.setFont(fieldFont);
        txtIntegrantes.setBackground(darkGray);
        txtIntegrantes.setForeground(Color.WHITE);
        txtIntegrantes.setCaretColor(Color.WHITE);
        txtIntegrantes.setLineWrap(true);
        txtIntegrantes.setToolTipText("Cada contacto va ligado al jugador mediante la posición, por lo tanto, se debe respetar el orden para registrar cada jugador junto a su contacto correspondiente");
        scrollIntegrantes = new JScrollPane(txtIntegrantes);
        scrollIntegrantes.setVisible(false);
        gbc.gridx = 1;
        panelInscripcion.add(scrollIntegrantes, gbc);

        // Zona para ingresar la información de contacto de los participantes
        lblContactos = new JLabel("Contacto(s):");
        lblContactos.setForeground(Color.WHITE);
        lblContactos.setFont(labelFont);
        lblContactos.setVisible(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panelInscripcion.add(lblContactos, gbc);

        txtContactos = new JTextArea(2, 15);
        txtContactos.setFont(fieldFont);
        txtContactos.setBackground(darkGray);
        txtContactos.setForeground(Color.WHITE);
        txtContactos.setCaretColor(Color.WHITE);
        txtContactos.setLineWrap(true);
        txtContactos.setToolTipText("Si un integrante no tiene contacto, escriba 'Sin contacto'");
        scrollContactos = new JScrollPane(txtContactos);
        scrollContactos.setVisible(false);
        gbc.gridx = 1;
        panelInscripcion.add(scrollContactos, gbc);

        // Botón para inscribirse en el torneo seleccionado
        btnInscribirse = new JButton("Inscribirse al Torneo");
        btnInscribirse.setBackground(btnGray);
        btnInscribirse.setForeground(Color.WHITE);
        btnInscribirse.setFocusPainted(false);
        btnInscribirse.setContentAreaFilled(false);
        btnInscribirse.setOpaque(true);
        btnInscribirse.setEnabled(false);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelInscripcion.add(btnInscribirse, gbc);

        // Parte Inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.setBackground(new Color(43, 43, 43));
        btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            ventana.cambiarPantalla("MENU_INICIO");
        });
        btnVolver.setBackground(btnGray);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setOpaque(true);
        panelInferior.add(btnVolver);

        // se unen las partes
        panelInscripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelInscripcion, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);


        // Logica para buscar un torneo o evento con el id
        btnBuscar.addActionListener(e -> {
            String codigo = txtIdTorneo.getText().trim();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            torneoEncontrado = registro.buscarTorneoPorInvitacion(codigo);

            if (torneoEncontrado != null) {
                lblEstadoTorneo.setText("Torneo encontrado: " + torneoEncontrado.getNombre());
                lblEstadoTorneo.setForeground(new Color(249, 140, 17));

                boolean esEquipo = torneoEncontrado.getDisciplina().getMaxJugadoresPorEquipo() > 1;

                txtNombreJugador.setEnabled(true);
                txtNombreJugador.setText("");

                if (esEquipo) {
                    lblNombre.setText("Nombre del Equipo:");
                    lblIntegrantes.setVisible(true);
                    scrollIntegrantes.setVisible(true);
                    lblContactos.setText("Contactos (separados por salto de línea):");
                } else {
                    lblNombre.setText("Nombre del Jugador:");
                    lblIntegrantes.setVisible(false);
                    scrollIntegrantes.setVisible(false);
                    lblContactos.setText("Dato de Contacto:");
                }

                lblContactos.setVisible(true);
                scrollContactos.setVisible(true);

                btnInscribirse.setEnabled(true);
                txtIdTorneo.setEnabled(false);
                btnBuscar.setEnabled(false);
            } else {
                lblEstadoTorneo.setText("No se encontró ningún torneo con ese código.");
                lblEstadoTorneo.setForeground(Color.RED);
                txtNombreJugador.setEnabled(false);
                btnInscribirse.setEnabled(false);
            }
        });

        // Logica para inscribirse en el evento seleccionado
        btnInscribirse.addActionListener(e -> {
            String nombreJugador = txtNombreJugador.getText().trim();
            if (nombreJugador.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Instanciación del participante, sea Jugador o Equipo, extrayendo los datos de los cuadros de texto
                boolean esEquipo = torneoEncontrado.getDisciplina().getMaxJugadoresPorEquipo() > 1;
                String idGenerado = (esEquipo ? "EQ-" : "JUG-") + System.currentTimeMillis();

                String textoContactos = txtContactos.getText().trim();
                if (textoContactos.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar la información de contacto.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (esEquipo) {
                    String textoIntegrantes = txtIntegrantes.getText().trim();
                    if (textoIntegrantes.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Debe ingresar los integrantes del equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Se separa el texto ingresado en integrantes y en contactos para guardar cada jugador y contacto en formato de lista
                    List<Jugador> listaIntegrantes = new ArrayList<>();
                    String[] nombresSeparados = textoIntegrantes.split("\n");
                    String[] contactosSeparados = textoContactos.split("\n");

                    if (nombresSeparados.length != contactosSeparados.length) {
                        JOptionPane.showMessageDialog(this, "Debe haber la misma cantidad de contactos que de integrantes.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    for (int i = 0; i < nombresSeparados.length; i++) {
                        String nombreMiembro = nombresSeparados[i].trim();
                        String contactoMiembro = contactosSeparados[i].trim();

                        if (nombreMiembro.isEmpty() || contactoMiembro.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No puedes dejar nombres o contactos en blanco.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String idUnicoMiembro = "JUG-" + System.currentTimeMillis() + "-" + i;
                        listaIntegrantes.add(new Jugador(idUnicoMiembro, nombreMiembro, contactoMiembro));
                    }

                    Equipo nuevoEquipo = new Equipo(idGenerado, nombreJugador, listaIntegrantes);
                    torneoEncontrado.agregarParticipante(nuevoEquipo, registro.getUsuarioActual());
                } else {

                    String[] contactosSeparados = textoContactos.split("\n");

                    // Nos aseguramos de que haya un solo dato de contacto
                    if (contactosSeparados.length > 1) {
                        JOptionPane.showMessageDialog(this, "Al ser una disciplina individual, solo debes ingresar un dato de contacto.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Jugador nuevoJugador = new Jugador(idGenerado, nombreJugador, textoContactos);
                    torneoEncontrado.agregarParticipante(nuevoJugador, registro.getUsuarioActual());
                }

                JOptionPane.showMessageDialog(this,
                        "¡Inscripción exitosa en '" + torneoEncontrado.getNombre() + "'!",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                Torneo torneoInscrito = torneoEncontrado;

                // Se reestablece la interfaz para el Administrador o se redirección hacia el bracket para el usuario normal
                Usuario usuarioActual = registro.getUsuarioActual();
                if (torneoEncontrado.esDueño(usuarioActual)) {
                    txtNombreJugador.setText("");
                    if (txtIntegrantes != null) txtIntegrantes.setText("");
                    if (txtContactos != null) txtContactos.setText("");
                } else {
                    limpiarCampos();
                    ventana.mostrarBracket(torneoInscrito);
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Inscripción Fallida", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // este método borra y limpia los espacios
    private void limpiarCampos() {
        txtIdTorneo.setText("");
        txtIdTorneo.setEnabled(true);
        btnBuscar.setEnabled(true);

        txtNombreJugador.setText("");
        txtNombreJugador.setEnabled(false);
        btnInscribirse.setEnabled(false);

        // no solo se limpian, tambien se ocultan los cuadros de texto dinámicos
        if (txtIntegrantes != null) txtIntegrantes.setText("");
        if (lblIntegrantes != null) lblIntegrantes.setVisible(false);
        if (scrollIntegrantes != null) scrollIntegrantes.setVisible(false);

        if (txtContactos != null) txtContactos.setText("");
        if (lblContactos != null) lblContactos.setVisible(false);
        if (scrollContactos != null) scrollContactos.setVisible(false);

        if (lblNombre != null) lblNombre.setText("Nombre del Jugador/Equipo:");

        lblEstadoTorneo.setText("");
        lblEstadoTorneo.setForeground(Color.LIGHT_GRAY);
        torneoEncontrado = null;

        revalidate();
        repaint();
    }
}