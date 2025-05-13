package Parte1;

import javax.swing.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Comida {
    private int comida=0;
    JTextArea tf;
    public Comida(JTextArea tf) {
        this.tf=tf;
    }
    private LinkedBlockingQueue<Thread> colaComida = new LinkedBlockingQueue<>();

    public int getComida() {
        return comida;
    }
    public void setComida(int cantidad) {
        synchronized (this) {
            this.comida = cantidad + comida;
            tf.setText(this.comida + "");
            this.notifyAll();
        }//Despertamos a los que esten en cola
    }
    public synchronized void comer(Humano humano) throws InterruptedException {

            if (comida > 0) {
                comida--;
                tf.setText(this.comida+"");
                Thread.sleep((int) (Math.random() * 2000) + 3000);
                System.out.println("El humano " + humano.getHumanoId() + " ha comido");
            } else {
                System.out.println("El humano " + humano.getHumanoId() +
                        " esta en la cola hasta que haya comida disponible");
                colaComida.offer(humano);
                while (colaComida.peek() != humano) {
                    this.wait();
                }
                colaComida.poll();
                comida--;
                tf.setText(this.comida+"");
                Thread.sleep((int) (Math.random() * 3000) + 5000);
                System.out.println("El humano " + humano.getHumanoId() + " ha comido");
                this.notifyAll();
            }

    }

}
