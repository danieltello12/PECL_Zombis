package Parte1;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Refugio {
    private int comida=0;

    private LinkedBlockingQueue<Thread> colaComida = new LinkedBlockingQueue<>();

    public int getComida() {
        return comida;
    }
    public synchronized void setComida(int cantidad) {
        this.comida=cantidad+comida;
        notifyAll(); //Despertamos a los que esten en cola
    }
    public synchronized void comer(Humano humano) throws InterruptedException {

            if (comida > 0) {
                comida--;
                Thread.sleep((int) (Math.random() * 3000) + 5000);
                System.out.println("El humano " + humano.getHumanoId() + " ha comido");
            } else if (comida == 0) {
                System.out.println("El humano " + humano.getHumanoId() +
                        " esta en la cola hasta que haya comida disponible");
                colaComida.offer(humano);
                while (colaComida.peek() != humano) {
                    wait();
                }
                colaComida.poll();
                comida--;
                Thread.sleep((int) (Math.random() * 3000) + 5000);
                System.out.println("El humano " + humano.getHumanoId() + " ha comido");
                notifyAll();
            }

    }

}
