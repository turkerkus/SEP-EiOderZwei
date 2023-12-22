package eoz.client.client;


import java.rmi.Remote;
import java.rmi.RemoteException;



// Das Interface dient als "Bibliothek" für die Registry, damit der Client aufgelistete Methoden anfragen kann
// hier wird nichts gestartet, dieser wird vom Server beim Start in die Registry geladen
public interface ServerInter extends Remote {
     String sayHello() throws RemoteException;

    // Methods for the chat functionality
    void receiveMessage(String senderUsername, String message) throws RemoteException; //Change client to Spieler

}