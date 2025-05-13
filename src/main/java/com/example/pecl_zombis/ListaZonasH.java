package com.example.pecl_zombis;

import Parte1.Humano;
import Parte1.Mundo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ListaZonasH {
    ArrayList<Humano> lista;
    JTextArea tf;

    public ListaZonasH(JTextArea tf)
    {
        lista=new ArrayList<Humano>();
        this.tf=tf;
    }

    public synchronized void meterH(Humano t)
    {

        lista.add(t);
        imprimir();
    }

    public void pausar_si_pausado(){
        while(Mundo.isPausado()){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public synchronized void sacar(Humano t)
    {

        lista.remove(t);
        imprimir();

    }
    public synchronized ArrayList<Humano> getLista()
    {
        return lista;
    }

    public void imprimir()
    {

        StringBuilder contenido= new StringBuilder();
        for(int i=0; i<lista.size(); i++)
        {
            contenido.append(lista.get(i).getHumanoId()).append(" ");
        }
        pausar_si_pausado();
        tf.setText("");
        tf.setText(contenido.toString());

    }
}
