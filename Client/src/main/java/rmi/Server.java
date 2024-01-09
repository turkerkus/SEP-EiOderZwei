package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


// Programm für den Server, dieser erhält vom Client über die Registry Anfragen, verarbeitet diese und schickt dann eine Antwort zurück an den Client.
// Der Server muss vor dem Client gestartet werden, dazu einfach die Main-Methode aufrufen. Die Registry wird automatisch im Port 15000 gestartet.
// Hinweis: Dies ist keine JavaFX-Implementierung, da wir annehmen, dass der Server keine grafische Oberfläche benötigt, um den Nutzern ein optimales Spielerlebnis liefern zu können.
public class Server implements Remote, ServerInter {
    //Variables

    //Die Registry wird erstellt
    public static Registry registry = null;

    //Constructor
    // Der Server muss konstruiert werden
    public Server() throws RemoteException {
        super();

    }

    //Methods
    // Implementierungen der Server-Methoden
    public String sayHello() throws RemoteException {
        // Diese Antwort wird an den Client geschickt, wenn dieser sich mit sayHello() verbindet
        return "Connection to Server successful. Hello Client!";
    }

    public static void main(String[] args) {
        // Versuche, den Server zu starten
        try {
            //Der Server wird erstellt und die Registry wird gestartet. Das Interface "Serverinter" wird geladen und mit der Registry verbunden.
            Server obj = new Server();
            ServerInter stub = (ServerInter) UnicastRemoteObject.exportObject(obj, 0);
            registry = LocateRegistry.createRegistry(15000);
            registry.bind("ServerInter", stub);

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