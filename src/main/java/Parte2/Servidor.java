package Parte2;

import Parte1.Mundo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Servidor extends UnicastRemoteObject implements ServidorInterfaz {
    private final Mundo mundo;

    public Servidor(Mundo mundo) throws RemoteException {
        super();
        this.mundo = mundo;
    }

    @Override
    public int getNumeroHumanosRefugio() throws RemoteException {
        return mundo.getNumeroHumanosRefugio();
    }

    @Override
    public int[] getNumeroHumanosZonasInseguras() throws RemoteException {
        int[] humanos = new int[4];
        for (int i = 0; i < 4; i++) {
            humanos[i] = mundo.getContador_humanos(i).size();
        }
        return humanos;
    }

    @Override
    public int[] getNumeroZombisZonasInseguras() throws RemoteException {
        int[] zombis = new int[4];
        for (int i = 0; i < 4; i++) {
            zombis[i] = mundo.getZonasInsegurasZ(i).getLista().size();
        }
        return zombis;
    }

    @Override
    public int[] getNumeroHumanosTuneles() throws RemoteException {
        int[] tuneles = new int[4];
        for (int i = 0; i < 4; i++) {
            tuneles[i] = mundo.getNumeroHumanosTuneles(i);
        }
        return tuneles;
    }

    @Override
    public String[] getRankingZombisLetales() throws RemoteException {
        return mundo.getRankingZombis().stream().map(zombie -> zombie.getZombieId() + " -> Contador de muertes: "+zombie.getContadorMuertes()).toArray(String[]::new);
    }

    @Override
    public void pausar() throws RemoteException {
        mundo.setPausado(true);

    }

    @Override
    public void reanudar() throws RemoteException {
        mundo.setPausado(false);
    }


}