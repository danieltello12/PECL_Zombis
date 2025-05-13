package Parte2;
import Parte1.Mundo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    public static void main(String[] args) {
        ServerSocket servidor;
        Socket conexion;
        DataOutputStream salida;
        DataInputStream entrada;
        Mundo mundo;

        try{
            servidor= new ServerSocket(5000);
            System.out.println("Servidor Arrancado...");
            conexion= servidor.accept();

            entrada= new DataInputStream(conexion.getInputStream());
            salida= new DataOutputStream(conexion.getOutputStream());

            mundo= new Mundo();


            while(true){

                String mensaje= entrada.readUTF();
                switch(mensaje){
                    case "NUM_HUMANOS_REFUGIO":
                        salida.writeInt(mundo.getNumeroHumanosRefugio());
                        break;
                    case "NUM_ZOMBIES_ZONASINSEG":
                        for (int i = 0; i < 4; i++) {
                            salida.writeInt(mundo.getZonasInsegurasZ(i).getLista().size());
                        }
                        break;
                    case "NUM_HUMANOS_ZONASINSEG":
                        for (int i = 0; i < 4; i++) {
                            salida.writeInt(mundo.getContador_humanos(i).size());
                        }
                        break;
                    case "NUM_HUMANOS_TUNEL":
                        for (int i = 0; i < 4; i++) {
                            salida.writeInt(mundo.getNumeroHumanosTunel(i));
                        }
                        break;
                    case "TOP_ZOMBIS_LETALES":
                        if (mundo.getRankingZombis().size()>=3) {
                            for (int i = 0; i < 3; i++) {
                                salida.writeUTF(mundo.getRankingZombis().get(i).toString()); // Envía el ranking como texto
                            }
                        } else {
                            salida.writeInt(mundo.getRankingZombis().size());
                        }
                        break;


                    case "PAUSAR":
                        mundo.setPausado(true);
                        break;
                    case "REANUDAR":
                        mundo.setPausado(false);
                        break;
                }



            }

        }catch (InterruptedException e) {
            System.out.println("Error al inicializar el mundo: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
