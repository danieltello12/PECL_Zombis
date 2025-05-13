package Parte2;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Interfaz extends JFrame {
    protected JLabel lblHumanosRefugio;
    protected JLabel[] lblHumanosTuneles;
    protected JLabel[] lblZombisZonas;
    protected JLabel[] lblHumanosZonas;
    protected JTextArea txtTopZombis;
    protected JButton btnPausar;
    protected JPanel panelInferior;
    protected JButton btnReanudar;
    protected JPanel panelPrincipal;

    public  Interfaz(int x, int y) {
        setTitle("Monitor Apocalipsis Zombi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null); // Centrar ventana
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panelPrincipal);

        // Panel superior: humanos en refugio
        JPanel panelRefugio = new JPanel();
        lblHumanosRefugio = new JLabel("Humanos en Refugio: ");
        lblHumanosRefugio.setFont(new Font("Arial", Font.BOLD, 16));
        panelRefugio.add(lblHumanosRefugio);
        panelPrincipal.add(panelRefugio, BorderLayout.NORTH);

        // Panel central con túneles y zonas inseguras
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 10));

        // Panel túneles
        JPanel panelTuneles = new JPanel(new GridLayout(5, 1, 5, 5));
        panelTuneles.setBorder(BorderFactory.createTitledBorder("Túneles"));
        lblHumanosTuneles = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            lblHumanosTuneles[i] = new JLabel("Túnel " + (i + 1) + ": ");
            panelTuneles.add(lblHumanosTuneles[i]);
        }
        panelCentral.add(panelTuneles);

        // Panel zonas inseguras
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

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con top zombis y botón
        panelInferior = new JPanel(new BorderLayout(10, 10));

        // Top zombis
        JPanel panelRanking = new JPanel(new BorderLayout());
        panelRanking.setBorder(BorderFactory.createTitledBorder("Top 3 Zombis Más Letales"));
        txtTopZombis = new JTextArea(4, 30);
        txtTopZombis.setEditable(false);
        txtTopZombis.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panelRanking.add(new JScrollPane(txtTopZombis), BorderLayout.CENTER);
        panelInferior.add(panelRanking, BorderLayout.CENTER);

        // Botón pausa/reanuda

        btnPausar = new JButton("Pausar Simulación");
        btnPausar.setFont(new Font("Arial", Font.BOLD, 16));
        btnPausar.setBackground(Color.LIGHT_GRAY);
        btnPausar.addActionListener(e -> {
            ;
            if (Cliente.pausado==0) {
                try {
                    Cliente.pausado=1;
                    Cliente.salida.writeUTF("PAUSAR");

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });


        panelInferior.add(btnPausar, BorderLayout.SOUTH);

        btnReanudar = new JButton("Reanudar Simulación");
        btnReanudar.setFont(new Font("Arial", Font.BOLD, 16));
        btnReanudar.setBackground(Color.LIGHT_GRAY);
        btnReanudar.addActionListener(e -> {
            if (Cliente.pausado==1) {
                try {
                    Cliente.pausado=0;
                    Cliente.salida.writeUTF("REANUDAR");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        JPanel botones = new JPanel(new GridLayout(2, 1, 5, 5));
        botones.add(btnPausar);
        botones.add(btnReanudar);;

        panelInferior.add(botones, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);


    }
}
