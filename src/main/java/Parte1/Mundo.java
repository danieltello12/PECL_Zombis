package Parte1;

import com.example.pecl_zombis.HelloController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    Falta:
        1-Metodos de salida de las zonas (en la ventana)(clase hellocontroller)
        2-Faltan Synchronized

 */

public class Mundo {
    HelloController controlador;
    //ExecutorService exHumanos= Executors.newFixedThreadPool(100);
    private Refugio refugio = new Refugio();
    protected List<List<Humano>> zonasInseguras = new ArrayList<>();
    ArrayList<Lock> tuneles = new ArrayList<>();
    ArrayList<CyclicBarrier>  barrerasTuneles= new ArrayList<>();

    public Mundo(HelloController controlador) throws InterruptedException {
        this.controlador=controlador;
        Refugio refugio = new Refugio();
        this.refugio = refugio;

        for (int i=0 ;i<=4 ;i++){
            zonasInseguras.add(new ArrayList<>());
            tuneles.add(new ReentrantLock());
            barrerasTuneles.add(new CyclicBarrier(3));
        }
        for(int i=1;i<=10;i++){
            String id=String.format("H%04d", i);
            Thread.sleep((int) (Math.random() * 500) + 2000);
            Humano humano = new Humano(id, this);
            humano.start();
           /*
            exHumanos.submit(()-> {

                Humano humano = new Humano(id, this);
                humano.start();
            });

            */
        }
        Zombie paciente0= new Zombie("Z0000",this);
        paciente0.start();

    }
    public static void main(String[] args) throws InterruptedException {
        HelloController controlador= new HelloController();
        Mundo mundo = new Mundo(controlador);
    }
    public Refugio getRefugio() {
        return refugio;
    }
    public synchronized HelloController getControlador() {
        return controlador;
    }

    public List<Humano> getContador_humanos(int zona) {
        {
            return zonasInseguras.get(zona);
        }
    }

    public boolean atacar_Humano(Zombie zombie,Humano presa) {
        presa.setAtacado();
        try {
            Thread.sleep((int) (Math.random() * 1000));
            int ataqueExitoso=(int)(Math.random()*100);
            if (ataqueExitoso<66){
                System.out.println("El zombi "+zombie.getZombieId()+ " no ha podido matar al humano "+presa.getHumanoId());
                return false;
            } else{
                System.out.println("El zombi " +zombie.getZombieId()+" ha logrado matar al humano  " +presa.getHumanoId());

                String id= String.valueOf(presa.getHumanoId());
                String nuevoid= id.replace("H", "Z");
                Zombie nuevoZombi= new Zombie(nuevoid,this);
                presa.setMuerto();
                presa.interrupt();
                nuevoZombi.start();
                return true;
            }
        } catch(Exception e){
            System.out.println("Error sucedido al atacar al humano " + presa.getId());
            return false;
        }
    }
}
