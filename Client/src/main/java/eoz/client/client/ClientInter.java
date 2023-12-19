package eoz.client.client;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


// Das Interface dient als "Bibliothek" für die Registry, damit der Client aufgelistete Methoden anfragen kann
// Hier wird nichts gestartet, dieser wird vom Server beim Start in die Registry geladen
public interface ClientInter extends Remote {
    String sayHello() throws RemoteException;

    boolean checkUsernameExists(String username) throws RemoteException;

    void addNewUser(String username) throws RemoteException;


    // Methods for the chat functionality
    void registerClient(String username, ClientInter client) throws RemoteException;

    void unregisterClient(String username) throws RemoteException;

    void sendChatMessage(String sender, String message) throws RemoteException;

    List<String> getChatMessages() throws RemoteException;
}