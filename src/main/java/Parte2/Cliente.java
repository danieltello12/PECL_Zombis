package Parte2;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    private static int pausado = 0;
    private static ServidorInterfaz servidor;

    public synchronized static void setPausado(int pausado) {
        Cliente.pausado = pausado;
    }
    public synchronized static int getPausado() {
        return pausado;
    }

    public static void main(String[] args) {
        try {

            Registry registry = LocateRegistry.getRegistry("localhost", 5099);
            servidor = (ServidorInterfaz) registry.lookup("ServidorZombis");


            Interfaz interfaz = new Interfaz(700, 600,servidor);
            Thread actualizador = new Thread(() -> {
                while (true) {
                    try {
                        int numeroHumanosRefugio = servidor.getNumeroHumanosRefugio();
                        interfaz.getlblhumanosRefugio().setText("Humanos en refugio: " + numeroHumanosRefugio);

                        int[] humanosZonas = servidor.getNumeroHumanosZonasInseguras();
                        for (int i = 0; i < 4; i++) {
                            interfaz.getlblHumanosZonas()[i].setText("Humanos en zona insegura " + (i + 1) + ": " + humanosZonas[i]);
                        }

                        int[] zombisZonas = servidor.getNumeroZombisZonasInseguras();
                        for (int i = 0; i < 4; i++) {
                            interfaz.getlblZombisZonas()[i].setText("Zombis en zona insegura " + (i + 1) + ": " + zombisZonas[i]);
                        }

                        int[] humanosTuneles = servidor.getNumeroHumanosTuneles();
                        for (int i = 0; i < 4; i++) {
                            interfaz.getlblHumanosTuneles()[i].setText("Humanos en túnel " + (i + 1) + ": " + humanosTuneles[i]);
                        }

                        String[] rankingZombis = servidor.getRankingZombisLetales();
                        StringBuilder ranking = new StringBuilder();
                        for (String zombi : rankingZombis) {
                            ranking.append(zombi).append("\n");
                        }
                        interfaz.gettxtTopZombis().setText(ranking.toString());

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
