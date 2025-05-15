package Parte1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Humano extends Thread{
    private Mundo mundo;
    private String id;
    private Boolean atacado;
    private Boolean vivo;
    private Boolean peleando=false;

    public void pausar_si_pausado(){
        while(Mundo.pausado){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }





    public Humano(String id, Mundo mundo){
        this.id=id;
        this.atacado=false;
        this.vivo=true;
        this.mundo=mundo;

    }
    public synchronized void setAtacado(){
        this.atacado=true;

    }
    public synchronized void setNoAtacado(){
        this.atacado=false;
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
    }

    public String getHumanoId(){
        return id;
    }
    public void run() {
        Logs.getInstancia().logInfo("El humano " + id + " ha comenzado a ejecutarse");


        while (vivo) {
            int tunel = 0;
            try {
                Logs.getInstancia().logInfo("El humano " + id + " ha entrado en la zona común");
                tunel = zonaComun();
            } catch (InterruptedException e) {
                Logs.getInstancia().logWarning("El humano " + id + " ha sido interrumpido en la zona común");
                throw new RuntimeException(e);
            }
            try {
                Logs.getInstancia().logInfo("El humano " + id + " ha dejado la zona común y va a la zona de entrada del túnel " + tunel);
                zonaExterior(tunel);
                if(!vivo){
                    break;
                }
            } catch (InterruptedException e) {
                Logs.getInstancia().logWarning("El humano " + id + " ha sido interrumpido en la zona de entrada del túnel");
                break;
            }
            try {
                Logs.getInstancia().logInfo("El humano " + id + "va a la zona comedor");
                zonaComedor();
                mundo.getComedor().sacar(this);

            } catch (InterruptedException e) {
                Logs.getInstancia().logWarning("El humano " + id + " ha sido interrumpido en la zona comedor");
                break;
            }
            if (atacado) {
                Logs.getInstancia().logWarning("El humano " + id + " ha sido marcado por un zombi y va a descansar");
                try {
                    Thread.sleep((int) (Math.random() * 3000) + 5000);
                } catch (InterruptedException e) {
                    Logs.getInstancia().logWarning("El humano " + id + " ha sido interrumpido mientras descansaba");
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
        pausar_si_pausado();
            mundo.getZonaComun().meterH(this);

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
        CyclicBarrier barrera=mundo.getBarrerasTuneles().get(eleccion_tunel);
       // System.out.println("El humano "+id+" esta esperando en el tunel "
          //      +eleccion_tunel+" a que lleguen el resto de compañeros");
        try {
            barrera.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
    }
        pausar_si_pausado();
        mundo.getZonaComun().sacar(this);
        pausar_si_pausado();
        mundo.getZonasEntradaTunel().get(eleccion_tunel).meterH(this);

    Semaphore cerrojo=mundo.getTuneles().get(eleccion_tunel);

    while(mundo.getEsperandoEntrada(eleccion_tunel)>0){
        try {
            sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

        cerrojo.acquire();
        try {
            pausar_si_pausado();
            mundo.getZonasEntradaTunel().get(eleccion_tunel).sacar(this);

            pausar_si_pausado();
            mundo.getZonasTunel().get(eleccion_tunel).meterH(this);

            sleep(1000);
            pausar_si_pausado();
            Logs.getInstancia().logInfo("El humano " + id + " ha cruzado el tunel " + eleccion_tunel + " y sale a la zona exterior");
            mundo.getZonasTunel().get(eleccion_tunel).sacar(this);
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

        pausar_si_pausado();
        mundo.getZonasInseguras().get(zona).meterH(this);

        Thread.sleep((int) (Math.random() * 2000) + 3000);
        while (peleando) {
            Thread.sleep(0);
        }
        pausar_si_pausado();
        mundo.getZonasInseguras().get(zona).sacar(this);

        if (atacado) {
            Logs.getInstancia().logInfo("El humano " + id + " ha sobrevivido al ataque de un zombi y vuelve al refugio sin recolectar comida");
        } else {

            Logs.getInstancia().logInfo("El humano " + id + " ha recogido 2 piezas de comida y vuelve al refugio");
            pausar_si_pausado();
            mundo.getComida().setComida(2);
        }
        pausar_si_pausado();
        mundo.getZonasSalidaTunel().get(zona).meterH(this);

        int esperando=mundo.getEsperandoEntrada(zona);
        esperando++;
        mundo.setEsperandoEntrada(esperando,zona);

        Semaphore sem = mundo.getTuneles().get(zona);
        sem.acquire();
        try {
            pausar_si_pausado();
            mundo.getZonasSalidaTunel().get(zona).sacar(this);


            pausar_si_pausado();
            System.out.println("El humano " + id + " ha accedido al tunel");
            mundo.getZonasTunel().get(zona).meterH(this);

            Thread.sleep(1000);
            pausar_si_pausado();
            esperando=mundo.getEsperandoEntrada(zona);
            esperando--;
            mundo.setEsperandoEntrada(esperando,zona);
            mundo.getZonasTunel().get(zona).sacar(this);



            System.out.println("El humano " + id + " ha regresado al refugio");

            setNoAtacado(); //Para cuando vuelva a salir al exterior

            pausar_si_pausado();
            System.out.println("El humano " + id + " ha dejado la comida y va a descansar");
            mundo.getDescanso().meterH(this);

            Thread.sleep((int) (Math.random() * 2000) + 4000);
            pausar_si_pausado();
            mundo.getDescanso().sacar(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sem.release();
        }

    }

    public void zonaComedor() throws InterruptedException {
    /*Modela el comportamiento del humano en la zona comedor
     */
        pausar_si_pausado();
        mundo.getComedor().meterH(this);
        System.out.println("El humano " + id + " ha accedido a la zona comedor" );
        pausar_si_pausado();
        mundo.getComida().comer(this);

    }




}

