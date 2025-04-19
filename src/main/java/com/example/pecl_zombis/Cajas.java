package com.example.pecl_zombis;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Cajas extends Pane {
    private VBox box;

    public Cajas() throws FileNotFoundException {
        ImageView i= new ImageView(new Image(new FileInputStream("src/main/resources/Imagenes/Boton.png")));

        i.setFitWidth(500);
        i.setFitHeight(115);



        box = new VBox(5);
        box.setTranslateX(25);
        box.setTranslateY(25);


        getChildren().addAll(i, box);

    }

    public void addItems(Boton... items) {
        for (Boton item : items)
            addItem(item);
    }

    public void addItem(Boton item) {
        box.getChildren().addAll(item);
    }
}

