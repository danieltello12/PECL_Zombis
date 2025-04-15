package Parte1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mundo {
    private List<List<Humano>> zonasInseguras = new ArrayList<>();
    ArrayList<Lock> tuneles = new ArrayList<>();

    public Mundo() {
        for (int i=0 ;i<=4 ;i++){
            zonasInseguras.add(new ArrayList<>());
            tuneles.add(new ReentrantLock());
        }
    }

    public List<Humano> getContador_humanos(int zona) {
        {
            return zonasInseguras.get(zona);
        }
    }
    //a

    public boolean atacar_Humano(Humano presa) {
        try {
            Thread.sleep((int) (Math.random() * 1000));
            int ataqueExitoso=(int)(Math.random()*100);
            if (ataqueExitoso<50){
                System.out.println("El zombi no ha podido matar al humano "+presa.getId());
                return true;
            }
            else{
                System.out.println("El zombi ha logrado matar al humano" +presa.getId());
                return false;
            }
        } catch(Exception e){
            System.out.println("Error sucedido al atacar al humano " + presa.getId());
            return false;
        }
    }
}
