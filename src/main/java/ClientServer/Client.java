package ClientServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// Programm für den Client, dieser schickt Anfragen an den Server (zur Demonstration eine Methode, die eine Begrüßungsnachricht vom Server erwartet)
// Zum Starten die Main-Methode ausführen. Der Server sollte zuvor gestartet werden und laufen.
// Hinweis: Dies ist keine JavaFX-Implementierung, da wir annehmen, dass dieser Prozess im Hintergrund läuft und für den Nutzer nicht sichtbar sein sollte.
public class Client {
    //Constructor
    private Client() {}

    //Methods
    public static void main(String[] args) {
        // Verbindungsversuch
        try {
            // Der Server erhält Anfragen über eine Registry, die als Schnittstelle dient
            // ServerInter ist das Serverinterface und gibt der Registry die Methoden, die an den Server weitergegeben werden dürfen.
            // sayHello ist eine Methode, die in der Registry vorhanden ist, wir erwarten also eine Begrüßung.
            // IP-Adresse des Servers ist "131.246.169.160" und der Port lautet 15000 zum Erreichen der Registry. Mit localhost ersetzen zum lokalen Testen

            // Mit Registry des Servers verbinden
            Registry registry = LocateRegistry.getRegistry("localhost", 15000);
            System.out.println("Verbindung zum Server hergestellt");

            // Vergleiche Interface der Registry mit der des Clients (Muss 1:1 eindeutig sein)
            ServerInter stub = (ServerInter) registry.lookup("ServerInter");
            System.out.println("Registry-Bibliothek gefunden");

            // Speichere die Antwort des Servers als response
            String response = stub.sayHello();

            // Die Antwort wird anschließend in der Konsole ausgegeben
            System.out.println("Server says: " + response);
        } catch (Exception e) {
            // Falls der Server nicht erreichbar ist oder die Anfrage nicht akzeptiert, wird eine Exception ausgegeben.
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}