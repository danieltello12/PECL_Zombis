package Parte1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class Humano extends Thread{
    private Mundo mundo;
    private String id;
    Boolean atacado;
    Boolean vivo;



    public Humano(String id, Mundo mundo){
        this.id=id;
        this.atacado=false;
        this.vivo=true;
        this.mundo=mundo;
    }
    public synchronized void setAtacado(){
        this.atacado=true;
    }
    public synchronized void setMuerto(){
        this.vivo=false;
    }


    public String getHumanoId(){
        return id;
    }
    public void run() {
        System.out.println("Hola desde " + id + " en hilo " + Thread.currentThread().getName());


        while (vivo) {
            int tunel = zonaComun();
            try {
                zonaExterior(tunel);
            } catch (InterruptedException e) {
                System.out.println("El humano " + id + " ahora es un zombie .");
                break;
            }
            try {
                zonaComedor();
            } catch (InterruptedException e) {
                System.out.println("El humano " + id + " ahora es un zombie .");
                break;
            }
            if (atacado) {
                System.out.println("El humano " + id +
                        " ha sido marcado por un zombi y va a descansar");
                try {
                    Thread.sleep((int) (Math.random() * 3000) + 5000);
                } catch (InterruptedException e) {
                    System.out.println("El humano " + id + " ahora es un zombie .");
                    break;
                }

            }

        }


    }


    public int zonaComun(){
        /*

            Modela el comportamiento del humano en la zona común

             */

        System.out.println("El humano "+id+" ha accedido a la zona común");
        mundo.getControlador().zonaComun(this);
        //Accede a la zona común

        try {

            //Permanecen un tiempo aleatorio entre 1 segundo y 2 segundos

            Thread.sleep((int)(Math.random()*1000)+2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Sale de la zona común
        //El humano se dirige a un túnel aleatorio

        int eleccion_tunel=(int)(Math.random()*4);

        //Espera a que haya 3 en ese tunel

        CyclicBarrier barrera=mundo.barrerasTuneles.get(eleccion_tunel);
        System.out.println("El humano "+id+" esta esperando en el tunel "
                +eleccion_tunel+" a que lleguen el resto de compañeros");
        mundo.getControlador().zonaEsperaEntradaTunel(eleccion_tunel,this);
        try {
            //Así hacemos que no se imprima tres veces lo mismo
            int waiting= barrera.getNumberWaiting();
            barrera.await();
            if(waiting==barrera.getParties()-1){
                System.out.println("El grupo del tunel " +eleccion_tunel+ " está formado");
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
    }

    Lock cerrojo=mundo.tuneles.get(eleccion_tunel);

    //El grupo accede al tunel de 1 en 1
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cerrojo.lock();
    try{

        System.out.println("El humano "+id+" ha accedido al tunel");
        mundo.getControlador().tunel(eleccion_tunel,this);
        sleep(1000);
        System.out.println("El humano "+id+" ha cruzado el tunel");
        mundo.getControlador().zonaEsperaSalidaTunel(eleccion_tunel,this);


    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    finally {
        cerrojo.unlock();
    }

    //El humano ha salido del tunel y accede al exterior
    return eleccion_tunel;
}


    public void zonaExterior(int zona) throws InterruptedException {

    /*
    Modela el comportamiento del humano en la zona exterior
     */
        mundo.zonasInseguras.get(zona).add(this);
        mundo.getControlador().ZonaExterior(zona, this);
        Thread.sleep((int) (Math.random() * 3000) + 5000);

        if (atacado) {
            //El humano ha sido atacado por un zombi
            System.out.println("El humano " + id + " ha sido atacado por un zombi");
        } else {
            //El humano no ha sido atacado por un zombi
            System.out.println("El humano " + id + " ha recogido 2 piezas de comida y vuelve");
            Lock cerrojo = mundo.tuneles.get(zona);
            if(cerrojo.tryLock()){
                try{
                System.out.println("El humano " + id + " ha accedido al tunel");
                mundo.getControlador().tunel(zona, this);
                Thread.sleep(1000);
                System.out.println("El humano " + id + " ha regresado al refugio");
                mundo.getRefugio().setComida(2);
                System.out.println("El humano " + id + " ha dejado la comida y va a descansar");
                mundo.getControlador().zonaDescanso(this);
                Thread.sleep((int) (Math.random() * 2000) + 4000);

                }finally{
                    cerrojo.unlock();
                }
            }else{
                System.out.println("El humano " + id + " encontró el túnel ocupado y espera su turno.");
                Thread.sleep((int) (Math.random() * 1000) + 2000); // Espera antes de intentar de nuevo
            }
        }

    }

    public void zonaComedor() throws InterruptedException {
    /*Modela el comportamiento del humano en la zona comedor
     */
        System.out.println("El humano " + id + " ha accedido a la zona comedor" );
        mundo.getRefugio().comer(this);

    }




}

