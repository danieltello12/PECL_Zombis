package Parte2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Cliente {
    public static void main(String[] args) {
        Socket cliente;
        DataInputStream entrada;
        DataOutputStream salida;
        try{
            Interfaz i= new Interfaz(500,600);
            cliente= new Socket(Inet4Address.getLocalHost(), 5000);
            entrada= new DataInputStream(cliente.getInputStream());
            salida= new DataOutputStream(cliente.getOutputStream());
            Thread th1= new Thread(()->{
                while(true){
                    try {
                        actualizar_humanos_ref(entrada, salida,i);
                        sleep(500);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            th1.start();
            Thread th2= new Thread(()->{
                while(true){
                    try {
                        actualizar_humanos_zonas_inseg(entrada, salida,i);
                        sleep(500);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            th2.start();
            Thread th3= new Thread(()->{
                while(true){
                    try {
                        actualizar_zombis_zonas_inseg(entrada, salida,i);
                        sleep(500);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            th3.start();
            Thread th4= new Thread(()->{
                while(true){
                    try {
                        actualizar_humanos_tuneles(entrada, salida,i);
                        sleep(500);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            th4.start();
            i.setVisible(true);


        }catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
   public static void actualizar_humanos_ref(DataInputStream entrada, DataOutputStream salida,Interfaz in) throws IOException {

        salida.writeUTF("NUM_HUMANOS_REFUGIO");
       int numeroHumanosRefugio= entrada.readInt();
       in.lblHumanosRefugio.setText("Humanos en refugio: ");
       in.lblHumanosRefugio.setText("Humanos en refugio: " + numeroHumanosRefugio);
   }
    public static void actualizar_humanos_zonas_inseg(DataInputStream entrada, DataOutputStream salida,Interfaz in) throws IOException {
        salida.writeUTF("NUM_HUMANOS_ZONAINSEG");
        for (int i = 1; i <= 4; i++) {
            int numeroHumanosZonaInseg = entrada.readInt();
            in.lblHumanosZonas[i-1].setText("Humanos en zona insegura: ");
            in.lblHumanosZonas[i-1].setText("Humanos en zona insegura: " + numeroHumanosZonaInseg);
        }
    }
    public static void actualizar_zombis_zonas_inseg(DataInputStream entrada, DataOutputStream salida,Interfaz in) throws IOException {
        salida.writeUTF("NUM_ZOMBIS_ZONAINSEG");
        for (int i = 1; i <= 4; i++) {
            int numeroZombisZonaInseg = entrada.readInt();
            in.lblZombisZonas[i-1].setText("Zombis en zona insegura: ");
            in.lblZombisZonas[i-1].setText("Zombis en zona insegura: " + numeroZombisZonaInseg);
        }
    }
    public static void actualizar_humanos_tuneles(DataInputStream entrada, DataOutputStream salida, Interfaz in) throws IOException {
        salida.writeUTF("NUM_HUMANOS_TUNEL");
        for (int i = 1; i <= 4; i++) {
            int numeroHumanosTunel = entrada.readInt();
            in.lblHumanosTuneles[i-1].setText("Humanos en túnel: ");
            in.lblHumanosTuneles[i-1].setText("Humanos en túnel: " + numeroHumanosTunel);
        }
    }

}
