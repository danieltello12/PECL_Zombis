package Parte2;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Interfaz extends JFrame {
    private JLabel lblHumanosRefugio;
    private JLabel[] lblHumanosTuneles;
    private JLabel[] lblZombisZonas;
    private JLabel[] lblHumanosZonas;
    private JTextArea txtTopZombis;
    private JButton btnPausar;
    private JButton btnReanudar;


    public JLabel getlblhumanosRefugio() {
        return lblHumanosRefugio;
    }
    public JLabel[] getlblHumanosTuneles() {
        return lblHumanosTuneles;
    }
    public JLabel[] getlblZombisZonas() {
        return lblZombisZonas;
    }
    public JLabel[] getlblHumanosZonas() {
        return lblHumanosZonas;
    }
    public JTextArea gettxtTopZombis() {
        return txtTopZombis;
    }

    public Interfaz(int width, int height, ServidorInterfaz servidor) {
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
        // Botón pausa/reanuda

        btnPausar = new JButton("Pausar Simulación");
        btnPausar.setFont(new Font("Arial", Font.BOLD, 16));
        btnPausar.setBackground(Color.LIGHT_GRAY);
        btnPausar.addActionListener(e -> {
            ;
            if (Cliente.getPausado()==0) {
                try {
                    Cliente.setPausado(1);
                    servidor.pausar();
                    int numeroHumanosRefugio = servidor.getNumeroHumanosRefugio();
                    this.lblHumanosRefugio.setText("Humanos en refugio: " + numeroHumanosRefugio);

                    int[] humanosZonas = servidor.getNumeroHumanosZonasInseguras();
                    for (int i = 0; i < 4; i++) {
                        this.lblHumanosZonas[i].setText("Humanos en zona insegura " + (i + 1) + ": " + humanosZonas[i]);
                    }

                    int[] zombisZonas = servidor.getNumeroZombisZonasInseguras();
                    for (int i = 0; i < 4; i++) {
                        this.lblZombisZonas[i].setText("Zombis en zona insegura " + (i + 1) + ": " + zombisZonas[i]);
                    }

                    int[] humanosTuneles = servidor.getNumeroHumanosTuneles();
                    for (int i = 0; i < 4; i++) {
                        this.lblHumanosTuneles[i].setText("Humanos en túnel " + (i + 1) + ": " + humanosTuneles[i]);
                    }

                    String[] rankingZombis = servidor.getRankingZombisLetales();
                    StringBuilder ranking = new StringBuilder();
                    for (String zombi : rankingZombis) {
                        ranking.append(zombi).append("\n");
                    }
                    this.txtTopZombis.setText(ranking.toString());

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
            if (Cliente.getPausado()==1) {
                try {
                    Cliente.setPausado(0);
                    servidor.reanudar();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        JPanel botones = new JPanel(new GridLayout(2, 1, 5, 5));
        botones.add(btnPausar);
        botones.add(btnReanudar);;

        panelInferior.add(botones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
    }
}
