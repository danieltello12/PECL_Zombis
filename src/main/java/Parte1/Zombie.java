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
            Logs.getInstancia().logInfo("El zombi " + id + " ha entrado en la zona insegura " + eleccion_zona);
            mundo.getZonasInsegurasZ(eleccion_zona).meterH(this);
            List<Humano> lista_humanos=mundo.getContador_humanos(eleccion_zona);
            if (!lista_humanos.isEmpty()){
                Humano presa=lista_humanos.get((int)(Math.random()*lista_humanos.size()));
                if(mundo.atacar_Humano(this, presa,eleccion_zona)){
                    contadorMuertes++;
                    Logs.getInstancia().logInfo("El zombi " + id + " ha atacado al humano " + presa.getHumanoId() + " en la zona " + eleccion_zona+ "número de muertes: "+contadorMuertes);

                }
            }else{
                //System.out.println("No hay humanos en la zona "+ eleccion_zona+ " insegura");
                try {
                    sleep((int)(Math.random()*1000)+2000);
                } catch (InterruptedException e) {
                    Logs.getInstancia().logWarning("El zombi " + id + " ha sido interrumpido en la zona insegura " + eleccion_zona);
                    throw new RuntimeException(e);
                }
            }
            mundo.getZonasInsegurasZ(eleccion_zona).sacar(this);

        }

    }

    public int getContadorMuertes() {
        return contadorMuertes;
    }

}
