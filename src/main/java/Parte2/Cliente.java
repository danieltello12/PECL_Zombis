package Parte2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        Socket cliente;
        DataInputStream entrada;
        DataOutputStream salida;
        try{
            cliente= new Socket(Inet4Address.getLocalHost(), 5000);
            entrada= new DataInputStream(cliente.getInputStream());
            salida= new DataOutputStream(cliente.getOutputStream());

            salida.writeUTF("NUM_HUMANOS_REFUGIO");
            int numeroHumanosRefugio= entrada.readInt();
            System.out.println("Numero de humanos en el refugio: "+ numeroHumanosRefugio);


            for (int i = 1; i <= 4; i++) {
                salida.writeUTF("NUM_HUMANOS_TUNEL" + i);
                int numeroHumanosTunel = entrada.readInt();
                System.out.println("  Túnel " + i + ": " + numeroHumanosTunel);
            }


            for (int i = 1; i <= 4; i++) {
                salida.writeUTF("NUM_ZOMBIES_ZONAINSEG_" + i);
                int numeroZombiesZonaInseg = entrada.readInt();
                System.out.println("  Zona insegura " + i + ": " + numeroZombiesZonaInseg);
            }
        }catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
