package org.example.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal de la interfaz gráfica.
 * Permite al usuario ingresar los datos básicos (nombre, disciplina y formato)
 * y utiliza un patrón Builder para instanciar el torneo.
 */
public class VentanaPrincipal extends JFrame {

    private JTextField txtNombre;
    private JComboBox<Disciplina> cbDisciplina;
    private JComboBox<String> cbFormato;
    private JButton btnCrearTorneo;

    /**
     * Constructor de VentanaPrincipal.
     * Inicializa y configura toda la parte de los componentes visuales de la interfaz.
     */
    public VentanaPrincipal() {

        Color colorFondo = new Color(30, 30, 36);
        Color colorTexto = new Color(240, 240, 240);
        Color colorBoton = new Color(52, 152, 219);
        Color colorTextoBoton = Color.WHITE;


        Font fuentePrincipal = new Font("Arial", Font.BOLD, 14);  //cambiamos la fuente a Arial


        setTitle("Sistema de Gestión de Torneos");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        getContentPane().setBackground(colorFondo);                          //cambiamos el color de fondo


        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 15));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 150));
        panelFormulario.setBackground(colorFondo); // Fondo del panel

        // Nombre del torneo
        JLabel lblNombre = new JLabel("Nombre del Torneo:");
        lblNombre.setForeground(colorTexto);
        lblNombre.setFont(fuentePrincipal);
        panelFormulario.add(lblNombre);

        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        // Disciplina
        JLabel lblDisciplina = new JLabel("Disciplina:");
        lblDisciplina.setForeground(colorTexto);
        lblDisciplina.setFont(fuentePrincipal);
        panelFormulario.add(lblDisciplina);

        cbDisciplina = new JComboBox<>(Disciplina.values());
        panelFormulario.add(cbDisciplina);

        // Formato
        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setForeground(colorTexto);
        lblFormato.setFont(fuentePrincipal);
        panelFormulario.add(lblFormato);

        String[] formatos = {"Liga Simple", "Eliminatoria Directa"};
        cbFormato = new JComboBox<>(formatos);
        panelFormulario.add(cbFormato);

        // Espacio vacío para alineación visual
        panelFormulario.add(new JLabel(""));

        // Botón de Creación
        btnCrearTorneo = new JButton("Crear Torneo");
        btnCrearTorneo.setBackground(colorBoton);
        btnCrearTorneo.setForeground(colorTextoBoton);
        btnCrearTorneo.setFont(fuentePrincipal);
        btnCrearTorneo.setFocusPainted(false); // Quita el borde feo al hacer clic
        panelFormulario.add(btnCrearTorneo);

        // Agregamos el panel al NORTE
        add(panelFormulario, BorderLayout.NORTH);


        btnCrearTorneo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearTorneo();
            }
        });
    }

    /**
     * Captura los datos ingresados en la interfaz gráfica, procesa la selección
     * del formato y utiliza la clase TorneoBuilder para construir el objeto.
     * Muestra cuadros de diálogo según el resultado de la operación.
     */
    private void crearTorneo() {
        try {
            String nombre = txtNombre.getText();
            Disciplina disciplina = (Disciplina) cbDisciplina.getSelectedItem();
            String formatoSeleccionado = (String) cbFormato.getSelectedItem();

            FormatoTorneo formato = null;
            if ("Liga Simple".equals(formatoSeleccionado)) {
                formato = new LigaSimple();
            } else if ("Eliminatoria Directa".equals(formatoSeleccionado)) {
                formato = new EliminatoriaDirecta();
            }

            // Llamada al patrón Builder
            TorneoBuilder builder = new TorneoBuilder();
            Torneo nuevoTorneo = builder
                    .conNombre(nombre.trim().isEmpty() ? null : nombre)
                    .conDisciplina(disciplina)
                    .conFormato(formato)
                    .build();

            JOptionPane.showMessageDialog(this,
                    "¡El torneo '" + nuevoTorneo.getNombre() + "' fue creado con éxito!",
                    "Torneo Creado",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Faltan Datos",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al crear el torneo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método principal de ejecución.
     * Inicia la aplicación creando y mostrando la ventana principal en el hilo
     * de despacho de eventos de Swing.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}