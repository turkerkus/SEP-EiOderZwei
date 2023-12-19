package eoz.server.server;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

// Programm für den Server, dieser erhält vom Client über die Registry Anfragen, verarbeitet diese und schickt dann eine Antwort zurück an den Client.
// Der Server muss vor dem Client gestartet werden, dazu einfach die Main-Methode aufrufen. Die Registry wird automatisch im Port 15000 gestartet.
// Hinweis: Dies ist keine JavaFX-Implementierung, da wir annehmen, dass der Server keine grafische Oberfläche benötigt, um den Nutzern ein optimales Spielerlebnis liefern zu können.
public class Server extends UnicastRemoteObject implements Remote, ServerInter {
    //Variables

    private Set<String> usernames;

    //Die Registry wird erstellt
    public static Registry registry = null;

    //Constructor
    // Der Server muss konstruiert werden
    public Server() throws RemoteException {
        super();
        usernames = new HashSet<>();
    }

    //Methods
    // Implementierungen der Server-Methoden
    public String sayHello() throws RemoteException {
        // Diese Antwort wird an den Client geschickt, wenn dieser sich mit sayHello() verbindet
        return "Connection to Server successful. Hello Client!";
    }

    @Override
    public boolean checkUsernameExists(String username) throws RemoteException {
        return usernames.contains(username);
    }

    @Override
    public void addNewUser(String username) throws RemoteException {
        usernames.add(username);
        System.out.println("New user added: " + username);
    }

    @Override
    public void registerClient(String username, ServerInter client) throws RemoteException {
        //TODO Frank and Jerry
    }

    @Override
    public void unregisterClient(String username) throws RemoteException {
        //TODO Frank and Jerry
    }

    @Override
    public void sendChatMessage(String sender, String message) throws RemoteException {
        //TODO Frank and Jerry
    }

    @Override
    public List<String> getChatMessages() throws RemoteException {
        return null;  //TODO Frank and Jerry
    }

    public static void main(String[] args) {
        // Versuche, den Server zu starten
        try {
            //Der Server wird erstellt und die Registry wird gestartet. Das Interface "Serverinter" wird geladen und mit der Registry verbunden.
            Server obj = new Server();
            ServerInter stub = (ServerInter) UnicastRemoteObject.exportObject(obj, 0);
            registry = LocateRegistry.createRegistry(15000);
            registry.bind("ServerInter", stub);
            Naming.rebind("Server", obj);
            // Der Server ist ab sofort betriebsbereit und wartet auf Anfragen. Zu Debug-Zwecken wird zusätzlich in der Konsole darauf hingewiesen.
            System.err.println("Server ready");
        }
        // Wenn der Server nicht gestartet werden kann, das Interface nicht geladen werden kann oder die Registry wegen einem bereits belegtem Port nicht starten kann.
        // Es wird eine Exception e geworfen.
        catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
