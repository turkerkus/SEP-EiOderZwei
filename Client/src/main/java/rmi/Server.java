package rmi;

import sharedClasses.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Programm für den Server, dieser erhält vom Client über die Registry Anfragen, verarbeitet diese und schickt dann eine Antwort zurück an den Client.
// Der Server muss vor dem Client gestartet werden, dazu einfach die Main-Methode aufrufen. Die Registry wird automatisch im Port 15000 gestartet.
// Hinweis: Dies ist keine JavaFX-Implementierung, da wir annehmen, dass der Server keine grafische Oberfläche benötigt, um den Nutzern ein optimales Spielerlebnis liefern zu können.
public class Server implements Remote, ServerInter {
    // Variables

    private GameSessionManager gameSessionManager;

    private Map<UUID, ClientUIUpdateListener> clientListeners = new ConcurrentHashMap<>();


    // Die Registry wird erstellt
    public static Registry registry = null;


    // Constructor
    // Der Server muss konstruiert werden
    public Server() throws RemoteException {
        super();
        gameSessionManager = new GameSessionManager();
    }

    // Methods
    // Implementierungen der Server-Methoden
    public String sayHello() throws RemoteException {
        // Diese Antwort wird an den Client geschickt, wenn dieser sich mit sayHello()
        // verbindet
        return "Connection to Server successful. Hello Client!";
    }



    // get the player list
    @Override
    public Map<UUID, ServerPlayer> getServerPlayers(UUID gameId) throws RemoteException {
        return gameSessionManager.getGameSession(gameId).getServerPlayers();
    }

    @Override
    public UUID createGameSession(UUID clientID, ClientUIUpdateListener listener, String gameName, String playerName, Integer numOfPlayers) throws RemoteException {
        // Generate or retrieve a unique Game ID
        UUID gameId = gameSessionManager.createGameSession(clientID, listener, gameName, playerName, numOfPlayers);
        return gameId;
    }

    @Override
    public boolean isGameReady(UUID gameId) throws RemoteException {
        return gameSessionManager.getGameSession(gameId).isGameSessionReady();
    }


    @Override
    public void startGameTable(UUID gameId) throws RemoteException {
        // Create a new game session with the Game ID and player information
        GameSession gameSession = gameSessionManager.getGameSession(gameId);
        gameSession.startGameTable();

    }

    @Override
    public void startGame(UUID gameId) throws RemoteException {
        gameSessionManager.getGameSession(gameId).startGame();
    }

    @Override
    public boolean doesGameExit(UUID gameId) {
        return gameSessionManager.doesGameExit(gameId);
    }

    @Override
    public String getGameName(UUID gameId) {
        return gameSessionManager.getGameSession(gameId).getGameName();
    }

    public void addPlayer(UUID clientID, ClientUIUpdateListener listener, String playerName, UUID gameId) {
        gameSessionManager.getGameSession(gameId).addPlayer(clientID,listener, playerName);
    }

    public boolean checkStartTable(UUID gameID, String playerName) throws RemoteException {
        return gameSessionManager.getGameSession(gameID).checkStartTable(playerName);
    }



    public void registerClient(UUID clientId, ClientUIUpdateListener listener) {
        clientListeners.put(clientId, listener);
    }

    // Draw card
    @Override
    public void drawCard(UUID clientId, UUID gameId) throws RemoteException {
        gameSessionManager.getGameSession(gameId).drawCard(clientId);
    }



    @Override
    public void hahnKlauen(UUID clientId, UUID gameId) throws RemoteException {
        gameSessionManager.getGameSession(gameId).hahnKlauen(clientId);
    }

    @Override
    public void karteUmtauschen(UUID clientId, UUID gameId, Integer eggPoints,ArrayList<ServerCard> selectedCards ) throws RemoteException {
        gameSessionManager.getGameSession(gameId).karteUmtauschen(clientId,eggPoints,selectedCards);
    }

    @Override
    public ServerPlayer getRoosterPlayer(UUID gameId) throws RemoteException {
        return gameSessionManager.getGameSession(gameId).getRoosterPlayer();
    }

    @Override
    public void stealOneCard(UUID target, ArrayList<ServerCard> selectedCards, UUID clientId, UUID gameId){
        gameSessionManager.getGameSession(gameId).stealOneCard(target,selectedCards,clientId);
    }

    @Override
    public void stealAllCards(UUID target, UUID clientId, UUID gameId) throws RemoteException{
        gameSessionManager.getGameSession(gameId).stealAllCards(target,clientId);
    }

    @Override
    public void sendChatMessage(String content, UUID gameId, UUID clientId) throws RemoteException {
        gameSessionManager.getGameSession(gameId).sendChatMessage(clientId, content);
    }


    @Override
    public void endPlayerTurn(UUID gameId) throws RemoteException {
        //TODO REMOVE THIS PRINT LINE
        System.out.println("end player turn from CLIENT");
        gameSessionManager.getGameSession(gameId).endPlayerTurn();
    }

    @Override
    public void stealingInProgress(UUID gameId, UUID playerId, UUID targetId, ArrayList<ServerCard> selectedCards) throws RemoteException {
        gameSessionManager.getGameSession(gameId).stealingInProgress(playerId, targetId,selectedCards);
    }

    @Override
    public void removeFoxCard(UUID gameId, ServerCard foxCard) throws RemoteException {
        gameSessionManager.getGameSession(gameId).removeFoxCard(foxCard);
    }

    @Override
    public void unregisterClient(UUID clientId, UUID gameID, boolean isLeavingGameSession) throws RemoteException {

        if (gameID != null && gameSessionManager.doesGameExit(gameID)){

            gameSessionManager.getGameSession(gameID).handleDisconnectedClient(clientId);
        }

        ClientUIUpdateListener listener = clientListeners.get(clientId);
        if (listener != null && !isLeavingGameSession) {
            clientListeners.remove(clientId);
        }

    }


    public static void main(String[] args) {
        // Versuche, den Server zu starten
        try {
            // Der Server wird erstellt und die Registry wird gestartet. Das Interface
            // "Serverinter" wird geladen und mit der Registry verbunden.
            Server obj = new Server();
            ServerInter stub = (ServerInter) UnicastRemoteObject.exportObject(obj, 0);
            registry = LocateRegistry.createRegistry(15000);
            registry.bind("ServerInter", stub);

            // Der Server ist ab sofort betriebsbereit und wartet auf Anfragen. Zu
            // Debug-Zwecken wird zusätzlich in der Konsole darauf hingewiesen.
            System.err.println("Server ready");
        }
        // Wenn der Server nicht gestartet werden kann, das Interface nicht geladen
        // werden kann oder die Registry wegen einem bereits belegtem Port nicht starten
        // kann.
        // Es wird eine Exception e geworfen.
        catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}