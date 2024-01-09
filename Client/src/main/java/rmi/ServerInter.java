package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;

// Das Interface dient als "Bibliothek" fÃ¼r die Registry, damit der Client aufgelistete Methoden anfragen kann
// Hier wird nichts gestartet, dieser wird vom Server beim Start in die Registry geladen
public interface ServerInter extends Remote {
    String sayHello() throws RemoteException;
}