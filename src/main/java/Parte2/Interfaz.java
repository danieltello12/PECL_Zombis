package Parte2;

import javax.swing.*;
import java.awt.*;

public class Interfaz extends JFrame {
    protected JLabel lblHumanosRefugio;
    protected JLabel[] lblHumanosTuneles;
    protected JLabel[] lblZombisZonas;
    protected JLabel[] lblHumanosZonas;
    protected JTextArea txtTopZombis;
    protected JButton btnPausar;
    protected JButton btnReanudar;

    public Interfaz(int width, int height) {
        setTitle("Monitor Apocalipsis Zombi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        lblHumanosRefugio = new JLabel("Humanos en Refugio: ");
        lblHumanosRefugio.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(lblHumanosRefugio);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 10));

        // Túneles
        JPanel panelTuneles = new JPanel(new GridLayout(5, 1, 5, 5));
        panelTuneles.setBorder(BorderFactory.createTitledBorder("Túneles"));
        lblHumanosTuneles = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            lblHumanosTuneles[i] = new JLabel("Túnel " + (i + 1) + ": ");
            panelTuneles.add(lblHumanosTuneles[i]);
        }
        panelCentral.add(panelTuneles);

        // Zonas inseguras
        JPanel panelZonas = new JPanel(new GridLayout(8, 1, 5, 5));
        panelZonas.setBorder(BorderFactory.createTitledBorder("Zonas Inseguras"));
        lblZombisZonas = new JLabel[4];
        lblHumanosZonas = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            lblZombisZonas[i] = new JLabel("Zombis en Zona " + (i + 1) + ": ");
            lblHumanosZonas[i] = new JLabel("Humanos en Zona " + (i + 1) + ": ");
            panelZonas.add(lblZombisZonas[i]);
            panelZonas.add(lblHumanosZonas[i]);
        }
        panelCentral.add(panelZonas);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        JPanel panelRanking = new JPanel(new BorderLayout());
        panelRanking.setBorder(BorderFactory.createTitledBorder("Top 3 Zombis Más Letales"));
        txtTopZombis = new JTextArea(4, 30);
        txtTopZombis.setEditable(false);
        txtTopZombis.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panelRanking.add(new JScrollPane(txtTopZombis), BorderLayout.CENTER);
        panelInferior.add(panelRanking, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPausar = new JButton("Pausar Simulación");
        btnReanudar = new JButton("Reanudar Simulación");
        panelBotones.add(btnPausar);
        panelBotones.add(btnReanudar);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }
}
