package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;


// Programm für den Server, dieser erhält vom Client über die Registry Anfragen, verarbeitet diese und schickt dann eine Antwort zurück an den Client.
// Der Server muss vor dem Client gestartet werden, dazu einfach die Main-Methode aufrufen. Die Registry wird automatisch im Port 15000 gestartet.
// Hinweis: Dies ist keine JavaFX-Implementierung, da wir annehmen, dass der Server keine grafische Oberfläche benötigt, um den Nutzern ein optimales Spielerlebnis liefern zu können.
public class Server implements Remote, ServerInter {
    //Variables

    private GameSessionManager gameSessionManager;



    //Die Registry wird erstellt
    public static Registry registry = null;

    //Constructor
    // Der Server muss konstruiert werden
    public Server() throws RemoteException {
        super();
        gameSessionManager = new GameSessionManager();
    }

    //Methods
    // Implementierungen der Server-Methoden
    public String sayHello() throws RemoteException {
        // Diese Antwort wird an den Client geschickt, wenn dieser sich mit sayHello() verbindet
        return "Connection to Server successful. Hello Client!";
    }

    //get the player list
    @Override
    public List<serverPlayer> getPlayerList(UUID gameId) throws RemoteException {
        return gameSessionManager.getGameSession(gameId).getPlayers();
    }

    @Override
    public UUID createGameSession(String playerName, Integer numOfPlayers) throws RemoteException  {
        // Generate or retrieve a unique Game ID
        UUID gameId =  gameSessionManager.createGameSession( playerName, numOfPlayers);
        waitingForPlayers(gameId);
        return gameId;
    }

    @Override
    public boolean isGameReady(UUID gameId) throws RemoteException {
        return gameSessionManager.getGameSession(gameId).isGameReady();
    }

    @Override
    public long getRemainingGameSessionTimeMillis(UUID gameId) throws RemoteException {
        return gameSessionManager.getGameSession(gameId).getRemainingTimeMillis();
    }


    @Override
    public void waitingForPlayers(UUID gameId) throws RemoteException {
        // Create a new game session with the Game ID and player information
        GameSession gameSession = gameSessionManager.getGameSession(gameId);

        // Start a thread or a task to wait for other players to join
        new Thread(() -> {
            while (gameSession.getNumberOfHumanPlayersPresent() < gameSession.getNumOfHumanPlayersRequired()) {
                // Wait for other players to join (this could be a polling mechanism or event-driven)
                // ...

                // Check if the waiting period is over or the game session is full
                if (gameSession.isWaitingPeriodOver() || gameSession.isFull()) {
                    break;
                }

                // Sleep for a short duration before checking again
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    // Handle interruption
                }
            }



            // Notify that the game session is ready to start
            gameSession.startGame();

        }).start();
    }

    @Override
    public void startGame(UUID gameId) throws RemoteException {
        gameSessionManager.getGameSession(gameId).startGame();
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
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}