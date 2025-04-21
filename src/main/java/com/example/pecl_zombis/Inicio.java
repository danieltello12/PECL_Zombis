package com.example.pecl_zombis;

import Parte1.Mundo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Inicio extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane root = new Pane();

        Image imagen= new Image(new FileInputStream("src/main/resources/Imagenes/ChatGPT Image 19 abr 2025, 21_08_03.png"));
        ImageView fondo= new ImageView(imagen);
        fondo.setFitWidth(1280);
        fondo.setFitHeight(720);



        Boton inicio= new Boton("INICIO", 500);
        inicio.setTranslateX(170);
        inicio.setTranslateY(0);
        inicio.setOnAction(() -> {
            try {
                Stage ventana = (Stage) root.getScene().getWindow();
                ventana.close();
                SwingUtilities.invokeLater(()-> {
                    try {
                        new Mundo().setVisible(true);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Cajas caja= new Cajas();
        caja.setTranslateX(370);
        caja.setTranslateY(600);
        caja.addItems(inicio);



        root.getChildren().addAll( fondo,caja);
        stage.setScene(new Scene(root, 1280, 720));
        stage.setTitle("Simulación - Apocalipsis Zombi");
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }

}