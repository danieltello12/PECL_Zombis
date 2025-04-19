package com.example.pecl_zombis;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PantallaPrincipal extends Pane {

    public PantallaPrincipal() throws FileNotFoundException {

        Image imagen= new Image(new FileInputStream("src/main/resources/Imagenes/ChatGPT Image 19 abr 2025, 21_08_03.png"));
        ImageView fondo= new ImageView(imagen);
        fondo.setFitWidth(1280);
        fondo.setFitHeight(720);


        getChildren().add(fondo);

    }

}
