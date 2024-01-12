package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.UUID;


// Programm für den Client, dieser schickt Anfragen an den Server (zur Demonstration eine Methode, die eine Begrüßungsnachricht vom Server erwartet)
// Zum Starten die Main-Methode ausführen. Der Server sollte zuvor gestartet werden und laufen.
// Hinweis: Dies ist keine JavaFX-Implementierung, da wir annehmen, dass dieser Prozess im Hintergrund läuft und für den Nutzer nicht sichtbar sein sollte.
public class Client{
    //Attributes

    private ServerInter serverStub;
    private final String playerName;
    private final Integer numOfPlayers;

    private UUID gameId;




    //Constructor
    public Client(String playerName, Integer numOfPlayers) {
        this.playerName = playerName;
        this.numOfPlayers = numOfPlayers;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }
    public void startgame(){
        try {
            serverStub.startGame(this.gameId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGameSession() {
        try {
            // Assuming the server has a method to create/join a game session
            gameId = serverStub.createGameSession(playerName, numOfPlayers);
            System.out.println("Game session created/joined.");
        } catch (Exception e) {
            System.err.println("Error creating/joining game session: ");
            System.err.println("Error creating/joining game session.");
            e.printStackTrace(); // This will print the full stack trace
        }
    }

    public long fetchRemainingTime() {
        try {
            // Assuming you have a method in serverStub to fetch the remaining time
            return serverStub.getRemainingGameSessionTimeMillis(gameId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0; // Return 0 or an appropriate error value
        }
    }

    public boolean isGameReady() throws RemoteException {

        try {
            // Assuming you have a method in serverStub to fetch the remaining time
            return serverStub.isGameReady(gameId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<serverPlayer> getPlayerList() throws RemoteException {
        try {
            // get player list
            return serverStub.getPlayerList(gameId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }

    }



    //Methods
    public Boolean connectToServer() {
        // Verbindungsversuch
        try {
            // Der Server erhält Anfragen über eine Registry, die als Schnittstelle dient
            // ServerInter ist das Serverinterface und gibt der Registry die Methoden, die an den Server weitergegeben werden dürfen.
            // sayHello ist eine Methode, die in der Registry vorhanden ist, wir erwarten also eine Begrüßung.
            // IP-Adresse des Servers ist "131.246.169.160" und der Port lautet 15000 zum Erreichen der Registry. Mit localhost ersetzen zum lokalen Testen

            // Mit Registry des Servers verbinden
            Registry registry = LocateRegistry.getRegistry( 15000);
            System.out.println("Verbindung zum Server hergestellt");

            // Vergleiche Interface der Registry mit der des Clients (Muss 1:1 eindeutig sein)
            serverStub = (ServerInter) registry.lookup("ServerInter");
            System.out.println("Registry-Bibliothek gefunden");

            // Speichere die Antwort des Servers als response
            String response = serverStub.sayHello();

            // Die Antwort wird anschließend in der Konsole ausgegeben
            System.out.println("Server says: " + response);
            return true;



        } catch (Exception e) {
            // Falls der Server nicht erreichbar ist oder die Anfrage nicht akzeptiert, wird eine Exception ausgegeben.
            System.err.println("Client exception: " + e);
            e.printStackTrace();
            return false;
        }
    }


}