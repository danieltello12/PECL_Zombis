package Parte1;

import com.example.pecl_zombis.Inicio;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Humano extends Thread{
    private Mundo mundo;
    private String id;
    Boolean atacado;
    Boolean vivo;
    Boolean peleando=false;




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
        this.interrupt();
    }
    public synchronized void setPeleando(){
        this.peleando=true;
    }

    public synchronized void setLibre(){
        this.peleando=false;
        notifyAll();
    }

    public String getHumanoId(){
        return id;
    }
    public void run() {
        //System.out.println("Hola desde " + id + " en hilo " + Thread.currentThread().getName());


        while (vivo) {
            int tunel = 0;
            try {
                tunel = zonaComun();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                zonaExterior(tunel);
                if(!vivo){
                    break;
                }
            } catch (InterruptedException e) {
                //System.out.println("El humano " + id + " ahora es un zombie .");
                break;
            }
            try {
                zonaComedor();

                    mundo.comedor.sacar(this);

            } catch (InterruptedException e) {
                //System.out.println("El humano " + id + " ahora es un zombie .");
                break;
            }
            if (atacado) {
                //System.out.println("El humano " + id +
                 //      " ha sido marcado por un zombi y va a descansar");
                try {
                    Thread.sleep((int) (Math.random() * 3000) + 5000);
                } catch (InterruptedException e) {
                    //System.out.println("El humano " + id + " ahora es un zombie .");
                    break;
                }

            }

        }


    }


    public int zonaComun() throws InterruptedException {
        /*

            Modela el comportamiento del humano en la zona común

             */

       //System.out.println("El humano "+id+" ha accedido a la zona común");

            mundo.zonaComun.meterH(this);

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
       // System.out.println("El humano "+id+" esta esperando en el tunel "
          //      +eleccion_tunel+" a que lleguen el resto de compañeros");
        try {
            barrera.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
    }

        mundo.zonaComun.sacar(this);
        mundo.zonasEntradaTunel.get(eleccion_tunel).meterH(this);

    Semaphore cerrojo=mundo.tuneles.get(eleccion_tunel);

    //El grupo accede al tunel de 1 en 1
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cerrojo.acquire();
        try {

            mundo.zonasEntradaTunel.get(eleccion_tunel).sacar(this);


            System.out.println("El humano " + id + " ha accedido al tunel");

            mundo.zonasTunel.get(eleccion_tunel).meterH(this);

            sleep(1000);
            System.out.println("El humano " + id + " ha cruzado el tunel");

            mundo.zonasTunel.get(eleccion_tunel).sacar(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            cerrojo.release();
        }


    //El humano ha salido del tunel y accede al exterior
    return eleccion_tunel;
}


    public void zonaExterior(int zona) throws InterruptedException {

    /*
    Modela el comportamiento del humano en la zona exterior
     */
            mundo.zonasInseguras.get(zona).meterH(this);

        Thread.sleep((int) (Math.random() * 3000) + 5000);
        while(peleando) {
            Thread.sleep(0);
        }

        mundo.zonasInseguras.get(zona).sacar(this);

        //El humano no ha sido atacado por un zombi
        System.out.println("El humano " + id + " ha recogido 2 piezas de comida y vuelve");

        mundo.zonasSalidaTunel.get(zona).meterH(this);

        Semaphore sem = mundo.tuneles.get(zona);
        sem.acquire();
        try {
            mundo.zonasSalidaTunel.get(zona).sacar(this);

            System.out.println("El humano " + id + " ha accedido al tunel");

            mundo.zonasTunel.get(zona).meterH(this);

            Thread.sleep(1000);

            mundo.zonasTunel.get(zona).sacar(this);

            System.out.println("El humano " + id + " ha regresado al refugio");
            mundo.getComida().setComida(2);
            System.out.println("El humano " + id + " ha dejado la comida y va a descansar");

            mundo.Descanso.meterH(this);

            Thread.sleep((int) (Math.random() * 2000) + 4000);

            mundo.Descanso.sacar(this);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            sem.release();
        }

    }



    public void zonaComedor() throws InterruptedException {
    /*Modela el comportamiento del humano en la zona comedor
     */
        mundo.comedor.meterH(this);
        System.out.println("El humano " + id + " ha accedido a la zona comedor" );
        mundo.getComida().comer(this);

    }




}

