package Parte2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServidorInterfaz extends Remote {
    int getNumeroHumanosRefugio() throws RemoteException;
    int[] getNumeroHumanosZonasInseguras() throws RemoteException;
    int[] getNumeroZombisZonasInseguras() throws RemoteException;
    int[] getNumeroHumanosTuneles() throws RemoteException;
    String[] getRankingZombisLetales() throws RemoteException;
    void pausar() throws RemoteException;
    void reanudar() throws RemoteException;
}
