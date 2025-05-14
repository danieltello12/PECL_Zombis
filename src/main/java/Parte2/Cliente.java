package Parte2;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    public static int pausado = 0;
    public static ServidorInterfaz servidor;

    public static void main(String[] args) {
        try {

            Registry registry = LocateRegistry.getRegistry("localhost", 5099);
            servidor = (ServidorInterfaz) registry.lookup("ServidorZombis");


            Interfaz interfaz = new Interfaz(700, 600,servidor);

            // Thread to update the interface periodically
            Thread actualizador = new Thread(() -> {
                while (true) {
                    try {
                        int numeroHumanosRefugio = servidor.getNumeroHumanosRefugio();
                        interfaz.lblHumanosRefugio.setText("Humanos en refugio: " + numeroHumanosRefugio);

                        int[] humanosZonas = servidor.getNumeroHumanosZonasInseguras();
                        for (int i = 0; i < 4; i++) {
                            interfaz.lblHumanosZonas[i].setText("Humanos en zona insegura " + (i + 1) + ": " + humanosZonas[i]);
                        }

                        int[] zombisZonas = servidor.getNumeroZombisZonasInseguras();
                        for (int i = 0; i < 4; i++) {
                            interfaz.lblZombisZonas[i].setText("Zombis en zona insegura " + (i + 1) + ": " + zombisZonas[i]);
                        }

                        int[] humanosTuneles = servidor.getNumeroHumanosTuneles();
                        for (int i = 0; i < 4; i++) {
                            interfaz.lblHumanosTuneles[i].setText("Humanos en túnel " + (i + 1) + ": " + humanosTuneles[i]);
                        }

                        String[] rankingZombis = servidor.getRankingZombisLetales();
                        StringBuilder ranking = new StringBuilder();
                        for (String zombi : rankingZombis) {
                            ranking.append(zombi).append("\n");
                        }
                        interfaz.txtTopZombis.setText(ranking.toString());

                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            actualizador.start();
            interfaz.setVisible(true);

        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
