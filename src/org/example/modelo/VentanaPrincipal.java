package org.example.modelo;

import javax.swing.*;
import java.awt.*;

/**
 * Clase contenedora que representa la ventana principal de la aplicación.
 * Actúa como el marco visual que sostiene y gestiona la navegación entre las
 * diferentes pantallas del sistema (Login, Menú, Crear Torneo, Unirse),
 * utilizando un patrón de diseño estructural basado en CardLayout.
 */
public class VentanaPrincipal extends JFrame {

    // Motor de navegación que permite alternar entre paneles como si fuerancartas de un mazo, mostrando solo uno a la vez
    private CardLayout navegador;

    // Panel principal que actúa como el "mazo" donde se apilan todas las pantallas secundarias de la aplicación
    private JPanel contenedorCartas;

    /**
     * Constructor de la ventana principal.
     * Configura las propiedades básicas de la ventana, inicializa el sistema de almacenamiento y prepara la navegación visual.
     */
    public VentanaPrincipal() {
        // Configuraciones normal de la ventana de Swing
        setTitle("Sistema de Gestión de Torneos - Acceso");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializamos el motor de navegación y el panel que lo utilizará
        navegador = new CardLayout();
        contenedorCartas = new JPanel(navegador);


        // Este objeto central guardará los usuarios y torneos durante la ejecución.
        RegistroTorneos registroGlobal = new RegistroTorneos();

        registroGlobal.cargarDatos();

        // Registramos cada pantalla en el mazo, asignándole un identificador único.
        contenedorCartas.add(new PanelLogin(this, registroGlobal), "PANTALLA_LOGIN");
        contenedorCartas.add(new PanelMenuPrincipal(this), "MENU_INICIO");
        contenedorCartas.add(new PanelCrearTorneo(this, registroGlobal), "CREAR_TORNEO");
        contenedorCartas.add(new PanelUnirseEvento(this, registroGlobal), "UNIRSE_TORNEO");

        // Incorporamos el mazo de cartas a la ventana principal
        add(contenedorCartas);

        // Al iniciar la aplicación, hacemos que la primera sea siempre el Login
        navegador.show(contenedorCartas, "PANTALLA_LOGIN");

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                registroGlobal.guardarDatos();
            }
        });

        // Hacemos visible la interfaz gráfica
        setVisible(true);
    }

    /**
     * Cambia la vista actual de la ventana hacia la pantalla solicitada.
     * Este método es llamado por los botones de los paneles hijos para no abrir ventanas secundarias.
     * @param nombrePantalla El identificador clave de la pantalla a mostrar
     */
    public void cambiarPantalla(String nombrePantalla) {
        // Le indicamos al CardLayout qué carta poner al frente
        navegador.show(contenedorCartas, nombrePantalla);

        if ("PANTALLA_LOGIN".equals(nombrePantalla)) {
            setTitle("Sistema de Gestión de Torneos - Acceso");
        } else {
            setTitle("Sistema de Gestión de Torneos - Panel de Control");
        }
    }

    /**
     * Método de entrada principal de la aplicación.
     * Configura el aspecto visual adaptado al sistema operativo y lanza la interfaz en el hilo seguro de Swing.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Intentamos aplicar el estilo visual nativo del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el tema visual del sistema.");
        }

        // Ejecutamos la creación de la ventana
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal();
        });
    }

}