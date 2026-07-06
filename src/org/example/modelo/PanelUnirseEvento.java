package org.example.modelo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Pantalla que permite a un usuario buscar un torneo existente mediante su ID e inscribirse como participante.
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

        // boton para buscar un torneo con ese id
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
        JLabel lblNombre = new JLabel("Nombre del Jugador/Equipo:");
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

        // boton para inscribirse en el torneo seleccionado
        btnInscribirse = new JButton("Inscribirse al Torneo");
        btnInscribirse.setBackground(btnGray);
        btnInscribirse.setForeground(Color.WHITE);
        btnInscribirse.setFocusPainted(false);
        btnInscribirse.setContentAreaFilled(false);
        btnInscribirse.setOpaque(true);
        btnInscribirse.setEnabled(false);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelInscripcion.add(btnInscribirse, gbc);

        // Parte Inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.setBackground(new Color(43, 43, 43));
        btnVolver = new JButton("Volver al Menú");
        btnVolver.setBackground(btnGray);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setOpaque(true);
        panelInferior.add(btnVolver);

        // se unen las partes
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelInscripcion, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Logica del boton para volver al menu de inicio
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            ventana.cambiarPantalla("MENU_INICIO");
        });
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
                lblEstadoTorneo.setForeground(new Color(249, 190, 17));
                txtNombreJugador.setEnabled(true);
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
                JOptionPane.showMessageDialog(this, "Debe ingresar su nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String idGenerado = "JUG-" + System.currentTimeMillis();
                Jugador nuevoJugador = new Jugador(idGenerado, nombreJugador, "Sin contacto");
                torneoEncontrado.agregarParticipante(nuevoJugador);

                JOptionPane.showMessageDialog(this,
                        "¡Inscripción exitosa en el torneo '" + torneoEncontrado.getNombre() + "'!",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarCampos();
                ventana.cambiarPantalla("MENU_INICIO");
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

        lblEstadoTorneo.setText("");
        lblEstadoTorneo.setForeground(Color.LIGHT_GRAY);
        torneoEncontrado = null;
    }
}