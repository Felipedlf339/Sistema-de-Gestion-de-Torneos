package org.example.vista;

import org.example.modelo.*;

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
    private JTextField txtMinParticipantes;
    private JTextField txtMaxParticipantes;

    public PanelCrearTorneo(VentanaPrincipal ventana, RegistroTorneos registro) {
        // fondo
        setBackground(new Color(43, 43, 43));
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 15));
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

        JLabel lblMin = new JLabel("Mínimo de participantes:");
        lblMin.setForeground(Color.WHITE);
        panelFormulario.add(lblMin);

        txtMinParticipantes = new JTextField();
        txtMinParticipantes.setBackground(new Color(69, 73, 74));
        txtMinParticipantes.setForeground(Color.WHITE);
        txtMinParticipantes.setCaretColor(Color.WHITE);
        panelFormulario.add(txtMinParticipantes);

        JLabel lblMax = new JLabel("Máximo de participantes:");
        lblMax.setForeground(Color.WHITE);
        panelFormulario.add(lblMax);

        txtMaxParticipantes = new JTextField();
        txtMaxParticipantes.setBackground(new Color(69, 73, 74));
        txtMaxParticipantes.setForeground(Color.WHITE);
        txtMaxParticipantes.setCaretColor(Color.WHITE);
        panelFormulario.add(txtMaxParticipantes);

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
                String nombreTorneo = txtNombre.getText().trim();
                if (nombreTorneo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar un nombre para el torneo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int minPart = Integer.parseInt(txtMinParticipantes.getText().trim());
                int maxPart = Integer.parseInt(txtMaxParticipantes.getText().trim());

                FormatoTorneo formato = cbFormato.getSelectedItem().equals("Liga Simple") ?
                        new LigaSimple() : new EliminatoriaDirecta();

                TorneoBuilder builder = new TorneoBuilder();
                Torneo nuevoTorneo = builder
                        .conNombre(nombreTorneo)
                        .conDisciplina((Disciplina) cbDisciplina.getSelectedItem())
                        .conFormato(formato)
                        .conCreador(registro.getUsuarioActual())
                        .conMinParticipantes(minPart)
                        .conMaxParticipantes(maxPart)
                        .build();

                boolean guardado = registro.registrarTorneo(nuevoTorneo);
                if (guardado) {
                    JOptionPane.showMessageDialog(this, "¡Torneo creado exitosamente!\nID para invitar: " + nuevoTorneo.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiamos los campos para la próxima vez
                    txtNombre.setText("");
                    txtMinParticipantes.setText("");
                    txtMaxParticipantes.setText("");

                    ventana.mostrarBracket(nuevoTorneo);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar el torneo en el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en los cupos de participantes.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al crear", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}