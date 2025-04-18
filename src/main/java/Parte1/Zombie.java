package Parte1;

import java.util.List;

public class Zombie extends Thread {
    private Mundo mundo;
    private String id;
    private int contadorMuertes=0;

    public Zombie(String id, Mundo mundo){
        this.mundo=mundo;
        this.id=id;
    }

    public void run(){
        while(true){
            int eleccion_zona=(int)(Math.random()*4);
            System.out.println("El zombi "+id+" ha elegido la zona "+eleccion_zona+ " insegura");
            List<Humano> lista_humanos=mundo.getContador_humanos(eleccion_zona);
            if (!lista_humanos.isEmpty()){
                Humano presa=lista_humanos.get((int)(Math.random()*lista_humanos.size()));
                if(mundo.atacar_Humano(presa)){
                    contadorMuertes++;
                    System.out.println("El zombi "+id+" tiene "+contadorMuertes+" muertes");

                }
                try {
                    sleep((int)(Math.random()*1000)+2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

}
