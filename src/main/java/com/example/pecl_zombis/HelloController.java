package com.example.pecl_zombis;

import Parte1.Humano;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class HelloController {
    @FXML
    private Label welcomeText;


    @FXML
    protected void onHelloButtonClick() throws FileNotFoundException {
            Pane root = new Pane();
            /*
            Image imagen= new Image(new FileInputStream("ruta"));
            ImageView fondo= new ImageView(imagen);
            fondo.setFitWidth(1280);
            fondo.setFitHeight(720);

             */


            Label titulo = new Label("Simulación - Apocalipsis Zombi");
            titulo.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            titulo.setTextFill(Color.WHITE);
            titulo.setTranslateX(450);
            titulo.setTranslateY(20);



            root.getChildren().addAll( titulo);




    }


}