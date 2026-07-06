package org.example.modelo;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para la creación de un nuevo torneo.
 * Recopila los datos de la interfaz, utiliza el patrón Builder para instanciar el torneo, y lo guarda en el registro.
 */
public class PanelCrearTorneo extends JPanel {
    private JTextField txtNombre;
    private JComboBox<Disciplina> cbDisciplina;
    private JComboBox<String> cbFormato;
    private JButton btnCrearTorneo;
    private JButton btnVolver;

    public PanelCrearTorneo(VentanaPrincipal ventana, RegistroTorneos registro) {
        // fondo
        setBackground(new Color(43, 43, 43));
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 15));
        panelFormulario.setBackground(new Color(43, 43, 43));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 150));

        JLabel lblNombre = new JLabel("Nombre del Torneo:");
        lblNombre.setForeground(Color.WHITE);
        panelFormulario.add(lblNombre);

        // espacio para escribir el nombre del evento
        txtNombre = new JTextField();
        txtNombre.setBackground(new Color(69, 73, 74));
        txtNombre.setForeground(Color.WHITE);
        txtNombre.setCaretColor(Color.WHITE);
        panelFormulario.add(txtNombre);

        JLabel lblDisciplina = new JLabel("Disciplina:");
        lblDisciplina.setForeground(Color.WHITE);
        panelFormulario.add(lblDisciplina);
        cbDisciplina = new JComboBox<>(Disciplina.values());
        cbDisciplina.setBackground(Color.WHITE); // Fondo blanco para legibilidad
        cbDisciplina.setForeground(Color.BLACK);
        panelFormulario.add(cbDisciplina);

        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setForeground(Color.WHITE);
        panelFormulario.add(lblFormato);
        String[] formatos = {"Liga Simple", "Eliminatoria Directa"};
        cbFormato = new JComboBox<>(formatos);
        cbFormato.setBackground(Color.WHITE);
        cbFormato.setForeground(Color.BLACK);
        panelFormulario.add(cbFormato);

        // boton para volver al menu de inicio
        btnVolver = new JButton("Volver al Menú");
        btnVolver.setBackground(new Color(77, 77, 77));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setOpaque(true);
        panelFormulario.add(btnVolver);

        // boton de crear y guardar torneo torneo
        btnCrearTorneo = new JButton("Crear y Guardar");
        btnCrearTorneo.setBackground(new Color(77, 77, 77));
        btnCrearTorneo.setForeground(Color.WHITE);
        btnCrearTorneo.setFocusPainted(false);
        btnCrearTorneo.setContentAreaFilled(false);
        btnCrearTorneo.setOpaque(true);
        panelFormulario.add(btnCrearTorneo);

        add(panelFormulario, BorderLayout.NORTH);

        // Lógica para volver al menu de inicio
        btnVolver.addActionListener(e -> ventana.cambiarPantalla("MENU_INICIO"));
        // Logica de la creación del torneo
        btnCrearTorneo.addActionListener(e -> {
            try {
                FormatoTorneo formato = cbFormato.getSelectedItem().equals("Liga Simple") ?
                        new LigaSimple() : new EliminatoriaDirecta();
                TorneoBuilder builder = new TorneoBuilder();
                Torneo nuevoTorneo = builder
                        .conNombre(txtNombre.getText())
                        .conDisciplina((Disciplina) cbDisciplina.getSelectedItem())
                        .conFormato(formato)
                        .conCreador(registro.getUsuarioActual())
                        .build();
                // Aqui lo dejamos registrado registrado
                boolean guardado = registro.registrarTorneo(nuevoTorneo);
                if (guardado) {
                    JOptionPane.showMessageDialog(this, "¡Torneo creado exitosamente!\nID para invitar: " + nuevoTorneo.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    txtNombre.setText("");
                    ventana.cambiarPantalla("MENU_INICIO");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar el torneo en el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}