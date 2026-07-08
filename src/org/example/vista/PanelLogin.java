package org.example.vista;

import org.example.modelo.RegistroTorneos;

import javax.swing.*;
import java.awt.*;

/**
 * Panel inicial de la aplicación.
 * Permite al usuario registrarse o iniciar sesión ingresando un nombre de usuario.
 * Utiliza el RegistroTorneos para guardar la sesión activa.
 */
public class PanelLogin extends JPanel {

    /**
     * Constructor del panel de inicio de sesión.
     * @param ventana Referencia a la ventana principal para cambiar de pantalla.
     * @param registro Referencia a la base de datos central en memoria.
     */
    public PanelLogin(VentanaPrincipal ventana, RegistroTorneos registro) {
        // Fondo del panel
        setBackground(new Color(43, 43, 43));

        // Usamos un GridBagLayout para centrar cosas en la pantalla
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        // Etiqueta de bienvenida
        JLabel lblBienvenida = new JLabel("BIENVENIDO AL SISTEMA DE TORNEOS");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(lblBienvenida, gbc);

        // Espacio de texto para el nombre
        JTextField txtUsuario = new JTextField(15);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBackground(new Color(69, 73, 74));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 120, 140), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridy = 1;
        add(txtUsuario, gbc);

        // Botón de ingreso/registro
        JButton btnIngresar = new JButton("Ingresar / Registrarse");
        btnIngresar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnIngresar.setBackground(new Color(77, 77, 77));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setOpaque(true);
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 120, 140), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        gbc.gridy = 2;
        add(btnIngresar, gbc);

        // Funcionamiento del botón
        btnIngresar.addActionListener(e -> {
            String nombre = txtUsuario.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                registro.iniciarSesionORegistrar(nombre);
                ventana.cambiarPantalla("MENU_INICIO");
            }
        });
    }
}