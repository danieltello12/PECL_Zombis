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
            mundo= new Mundo();
            servidor= new ServerSocket(5000);
            System.out.println("Servidor Arrancado...");

            while(true){
                conexion= servidor.accept();

                entrada= new DataInputStream(conexion.getInputStream());
                salida= new DataOutputStream(conexion.getOutputStream());

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
                }



            }

        }catch (InterruptedException e) {
            System.out.println("Error al inicializar el mundo: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
