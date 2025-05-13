package Parte2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Cliente {
    static volatile boolean pausado = false;
    public static void main(String[] args) {

        Socket cliente;
        DataInputStream entrada;
        DataOutputStream salida;
        try{
            Interfaz i= new Interfaz(500,600);
            cliente= new Socket(Inet4Address.getLocalHost(), 5000);
            entrada= new DataInputStream(cliente.getInputStream());
            salida= new DataOutputStream(cliente.getOutputStream());
            Thread actualizador = new Thread(() -> {
                while (true) {
                    if(!pausado)
                        try {
                            actualizar_humanos_ref(entrada, salida, i);
                            actualizar_humanos_zonas_inseg(entrada, salida, i);
                            actualizar_zombis_zonas_inseg(entrada, salida, i);
                            actualizar_humanos_tuneles(entrada, salida, i);
                            actualizar_ranking_zombis(entrada, salida, i);
                            sleep(500);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                }
            });
            actualizador.start();
            i.setVisible(true);


        }catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
   public static void actualizar_humanos_ref(DataInputStream entrada, DataOutputStream salida,Interfaz in) throws IOException {

        salida.writeUTF("NUM_HUMANOS_REFUGIO");
       int numeroHumanosRefugio= entrada.readInt();
       in.lblHumanosRefugio.setText("Humanos en refugio: ");
       in.lblHumanosRefugio.setText("Humanos en refugio: "  + numeroHumanosRefugio);
   }
    public static void actualizar_humanos_zonas_inseg(DataInputStream entrada, DataOutputStream salida,Interfaz in) throws IOException {
        salida.writeUTF("NUM_HUMANOS_ZONASINSEG");
        for (int i = 1; i <= 4; i++) {
            int numeroHumanosZonaInseg = entrada.readInt();
            in.lblHumanosZonas[i-1].setText("Humanos en zona insegura "+i+" :" + numeroHumanosZonaInseg);
        }
    }
    public static void actualizar_zombis_zonas_inseg(DataInputStream entrada, DataOutputStream salida,Interfaz in) throws IOException {
        salida.writeUTF("NUM_ZOMBIES_ZONASINSEG");
        for (int i = 1; i <= 4; i++) {
            int numeroZombisZonaInseg = entrada.readInt();
            in.lblZombisZonas[i-1].setText("Zombis en zona insegura"+i+" :"+numeroZombisZonaInseg);
        }
    }
    public static void actualizar_humanos_tuneles(DataInputStream entrada, DataOutputStream salida, Interfaz in) throws IOException {
        salida.writeUTF("NUM_HUMANOS_TUNEL");
        for (int i = 1; i <= 4; i++) {
            int numeroHumanosTunel = entrada.readInt();
            in.lblHumanosTuneles[i-1].setText("Humanos en túnel: " +i+" :"+numeroHumanosTunel);
        }
    }

    public static void actualizar_ranking_zombis(DataInputStream entrada, DataOutputStream salida, Interfaz in) throws IOException {
        salida.writeUTF("TOP_ZOMBIS_LETALES");
        StringBuilder ranking = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            ranking.append(entrada.readUTF()).append("\n");
        }
        in.txtTopZombis.setText(ranking.toString());
    }

}
