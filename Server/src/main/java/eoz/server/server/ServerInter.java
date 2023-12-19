package eoz.server.server;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// Das Interface dient als "Bibliothek" für die Registry, damit der Client aufgelistete Methoden anfragen kann
// hier wird nichts gestartet, dieser wird vom Server beim Start in die Registry geladen
public interface ServerInter extends Remote {
    String sayHello() throws RemoteException;

    boolean checkUsernameExists(String username) throws RemoteException;

    void addNewUser(String username) throws RemoteException;

    void registerClient(String username, ServerInter client) throws RemoteException;

    void unregisterClient(String username) throws RemoteException;

    void sendChatMessage(String sender, String message) throws RemoteException;

    List<String> getChatMessages() throws RemoteException;
}