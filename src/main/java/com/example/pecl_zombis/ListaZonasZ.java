package com.example.pecl_zombis;

import Parte1.Humano;
import Parte1.Zombie;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ListaZonasZ {
    ArrayList<Zombie> lista;
    JTextArea tf;

    public ListaZonasZ(JTextArea tf)
    {
        lista=new ArrayList<Zombie>();
        this.tf=tf;
    }

    public synchronized void meterH(Zombie t)
    {

        lista.add(t);
        imprimir();
    }
    public synchronized ArrayList<Zombie> getLista()
    {
        return lista;
    }


    public synchronized void sacar(Zombie t)
    {

        lista.remove(t);
        imprimir();


    }

    public void imprimir()
    {

        StringBuilder contenido= new StringBuilder();
        for(int i=0; i<lista.size(); i++)
        {
            contenido.append(lista.get(i).getZombieId()).append(" ");
        }
        tf.setText("");
        tf.setText(contenido.toString());

    }
}
