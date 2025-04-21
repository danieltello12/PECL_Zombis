package Parte1;

import java.util.List;

public class Zombie extends Thread{
    private Mundo mundo;
    private String id;
    private int contadorMuertes=0;

    public Zombie(String id, Mundo mundo){
        this.id=id;
        this.mundo=mundo;
    }
    public String getZombieId(){
        return id;
    }

    public void run(){
        while(true){
            int eleccion_zona=(int)(Math.random()*4);
            //System.out.println("El zombi "+id+" ha elegido la zona "+eleccion_zona+ " insegura");
            mundo.getZonasInsegurasZ(eleccion_zona).meterH(this);
            List<Humano> lista_humanos=mundo.getContador_humanos(eleccion_zona);
            if (!lista_humanos.isEmpty()){
                Humano presa=lista_humanos.get((int)(Math.random()*lista_humanos.size()));
                if(mundo.atacar_Humano(this, presa,eleccion_zona)){
                    contadorMuertes++;
                    //System.out.println("El zombi "+id+" ha matado a "+contadorMuertes+" humano(s)");

                }
                try {
                    sleep((int)(Math.random()*1000)+2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                //System.out.println("No hay humanos en la zona "+ eleccion_zona+ " insegura");
                try {
                    sleep((int)(Math.random()*1000)+2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            mundo.getZonasInsegurasZ(eleccion_zona).sacar(this);

        }

    }

}
