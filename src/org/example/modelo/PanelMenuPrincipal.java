package org.example.modelo;

import javax.swing.*;
import java.awt.*;

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
            String codigo = JOptionPane.showInputDialog(this, "Ingrese el ID del torneo:", "Ver Torneo", JOptionPane.QUESTION_MESSAGE);
            if (codigo == null || codigo.isBlank()) return;
            Torneo torneo = registro.buscarTorneoPorInvitacion(codigo.trim());
            if (torneo != null) {
                ventana.mostrarBracket(torneo);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún torneo con ese ID.", "Atención", JOptionPane.WARNING_MESSAGE);
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