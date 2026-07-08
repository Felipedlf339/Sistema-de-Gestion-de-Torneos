package org.example.modelo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Menú principal del sistema.
 * Permite al usuario navegar hacia la creación de un torneo o buscar uno existente.
 */
public class PanelMenuPrincipal extends JPanel {

    /**
     * Constructor del menú principal.
     * @param ventana Referencia a la ventana principal para gestionar la navegación.
     * @param registro registro de los torneos
     */
    public PanelMenuPrincipal(VentanaPrincipal ventana, RegistroTorneos registro) {
        // Fondo
        setBackground(new Color(43, 43, 43));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
        Font btnFont = new Font("Segoe UI", Font.PLAIN, 18);

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTION DE TORNEOS");
        lblTitulo.setFont(titleFont);
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(lblTitulo, gbc);

        // boton de crear nuevo torneo
        JButton btnCrear = new JButton("Crear Nuevo Torneo");
        btnCrear.setFont(btnFont);
        btnCrear.setBackground(new Color(77, 77, 77));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setContentAreaFilled(false);
        btnCrear.setOpaque(true);
        gbc.gridy = 1;
        add(btnCrear, gbc);

        // boton de unirse a evento
        JButton btnUnirse = new JButton("Inscripción a Torneos");
        btnUnirse.setFont(btnFont);
        btnUnirse.setBackground(new Color(77, 77, 77));
        btnUnirse.setForeground(Color.WHITE);
        btnUnirse.setFocusPainted(false);
        btnUnirse.setContentAreaFilled(false);
        btnUnirse.setOpaque(true);
        gbc.gridy = 2;
        add(btnUnirse, gbc);

        // Conectamos los botones con sus respectivos paneles
        btnCrear.addActionListener(e -> ventana.cambiarPantalla("CREAR_TORNEO"));
        btnUnirse.addActionListener(e -> ventana.cambiarPantalla("UNIRSE_TORNEO"));

        JButton btnVerTorneo = new JButton("Ver Estado de un Torneo");
        btnVerTorneo.setFont(btnFont);
        btnVerTorneo.setBackground(new Color(77, 77, 77));
        btnVerTorneo.setForeground(Color.WHITE);
        btnVerTorneo.setFocusPainted(false);
        btnVerTorneo.setContentAreaFilled(false);
        btnVerTorneo.setOpaque(true);
        gbc.gridy = 3;
        add(btnVerTorneo, gbc);

        btnVerTorneo.addActionListener(e -> {
            Usuario usuarioActual = registro.getUsuarioActual();
            java.util.List<Torneo> misTorneos = registro.obtenerMisTorneos(usuarioActual);

            if (misTorneos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aún no has creado ni te has inscrito en ningún torneo.", "Mis Torneos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            UIManager.put("OptionPane.background", new Color(43, 43, 43));
            UIManager.put("Panel.background", new Color(43, 43, 43));


            JComboBox<Torneo> comboTorneos = new JComboBox<>(misTorneos.toArray(new Torneo[0]));
            comboTorneos.setBackground(Color.WHITE);
            comboTorneos.setForeground(Color.BLACK);


            JPanel panelOscuro = new JPanel(new BorderLayout(0, 10));
            panelOscuro.setBackground(new Color(43, 43, 43));

            JLabel lblMensaje = new JLabel("Seleccione su torneo:");
            lblMensaje.setForeground(Color.WHITE);

            panelOscuro.add(lblMensaje, BorderLayout.NORTH);
            panelOscuro.add(comboTorneos, BorderLayout.CENTER);

            int opcion = JOptionPane.showOptionDialog(
                    this,
                    panelOscuro,
                    "Mis Torneos",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[]{"Ver Torneo", "Cancelar"},
                    "Ver Torneo"
            );


            if (opcion == JOptionPane.OK_OPTION) {
                Torneo seleccionado = (Torneo) comboTorneos.getSelectedItem();
                if (seleccionado != null) {
                    ventana.mostrarBracket(seleccionado);
                }
            }
        });

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(btnFont);
        btnCerrarSesion.setBackground(new Color(77, 77, 77));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setOpaque(true);
        gbc.gridy = 4;
        add(btnCerrarSesion, gbc);

        btnCerrarSesion.addActionListener(e -> {
            registro.cerrarSesion();
            ventana.cambiarPantalla("PANTALLA_LOGIN");
        });
    }
}