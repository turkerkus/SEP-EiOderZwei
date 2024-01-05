package eoz.rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

// Das Interface dient als "Bibliothek" fÃ¼r die Registry, damit der Client aufgelistete Methoden anfragen kann
// Hier wird nichts gestartet, dieser wird vom Server beim Start in die Registry geladen
public interface ServerInter extends Remote {
    String sayHello() throws RemoteException;

    /* Speicher die Chatnachricht auf dem Server*/
    void sendMessage(String message, String playerId)throws RemoteException;

    /* @return Liste der Chatnachrichten und deren Absender
     */
    List<List<String>> getChat(boolean mes) throws RemoteException;

    Map<String, String> getClients() throws RemoteException;

}