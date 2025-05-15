package ProgramaPrincipal;

import Parte1.Humano;
import Parte1.Mundo;
import Parte1.Zombie;
import Parte2.Cliente;
import Parte2.Servidor;
import com.example.pecl_zombis.Boton;
import com.example.pecl_zombis.Cajas;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.Thread.sleep;

public class Inicio extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane root = new Pane();


        Image imagen = new Image(new FileInputStream("src/main/resources/Imagenes/ChatGPT Image 19 abr 2025, 21_08_03.png"));
        ImageView fondo = new ImageView(imagen);
        fondo.setFitWidth(1280);
        fondo.setFitHeight(720);



        Boton inicio = new Boton("INICIO", 500);
        inicio.setTranslateX(170);
        inicio.setTranslateY(0);
        inicio.setOnAction(() -> {
            try {
                Stage ventana = (Stage) root.getScene().getWindow();
                ventana.close();
                SwingUtilities.invokeLater(() -> {
                    new Thread(() -> {
                       try {

                           Mundo mundo = new Mundo();
                           Servidor servidor = new Servidor(mundo);
                           Registry registry = LocateRegistry.createRegistry(5099);
                           registry.rebind("ServidorZombis", servidor);
                           System.out.println("Servidor RMI iniciado...");
                           SwingUtilities.invokeLater(() -> {
                               try {
                                   Cliente.main(new String[0]);
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                           });
                           Zombie paciente0= new Zombie("Z0000",mundo);
                           mundo.getZombis().add(paciente0);
                           paciente0.start();
                           for(int i=1;i<=10000;i++){
                               while (Mundo.isPausado()) {
                                   try {
                                       sleep(100);
                                   } catch (InterruptedException e) {
                                       throw new RuntimeException(e);
                                   }
                               }
                               String id=String.format("H%04d", i);
                               sleep((int) (Math.random() * 500) + 2000);
                               Humano humano = new Humano(id, mundo);
                               mundo.getHumanos().add(humano);
                               humano.start();
                           }

                       } catch (RemoteException | InterruptedException e) {
                           e.printStackTrace();
                       }
                   }).start();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add the button to a container
        Cajas caja = new Cajas();
        caja.setTranslateX(370);
        caja.setTranslateY(600);
        caja.addItems(inicio);

        // Add components to the root pane
        root.getChildren().addAll(fondo, caja);
        stage.setScene(new Scene(root, 1280, 720));
        stage.setTitle("Simulación - Apocalipsis Zombi");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}