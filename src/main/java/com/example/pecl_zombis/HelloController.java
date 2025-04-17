package com.example.pecl_zombis;

import Parte1.Humano;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

    }
    public void zonaDescanso(Humano humano){}
    public void zonaComedor(Humano humano){
    }
    public void zonaComun(Humano humano){
    }
    public void zonaEsperaEntradaTunel(int tunel,Humano humano){
    }
    public void zonaEsperaSalidaTunel(int tunel,Humano humano){
    }
    public void ZonaExterior(int zona,Humano humano){
    }
    public void tunel(int tunel,Humano humano){}

}