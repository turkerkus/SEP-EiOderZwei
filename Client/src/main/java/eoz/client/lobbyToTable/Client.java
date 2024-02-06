package eoz.client.lobbyToTable;

import sharedClasses.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Client program for sending requests to the server.
 * This class demonstrates a method to receive a greeting message from the server.
 * To start, execute the main method. Ensure that the server is already running.
 * Note: This is not a JavaFX implementation; it is assumed to run in the background and be invisible to the user.
 */
public class Client implements ClientInter {


    // Attributes
    private final UUID clientId = UUID.randomUUID();
    boolean isConnectedToServer = false;
    private ServerInter serverStub;
    private String playerName;
    private String gameName;
    private UUID gameId;
    private final ClientUIUpdateListener updateListener;
    public Client() {
        try {
            updateListener = new ClientUIUpdateListenerImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getClientName() {
        return playerName;
    }

    // Constructor for initializing with player name only
    public void setClientName(String playerName) {
        this.playerName = playerName;
        try {
            updateListener.setClientName(playerName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setTableController(TableControllerInterface tableController) throws RemoteException {
        updateListener.setTableController(tableController);
    }

    @Override
    public void sendChatMessage(String content) throws RemoteException {
        serverStub.sendChatMessage(content, gameId, clientId);
    }

    // Getter for clientId
    public UUID getClientId() {
        return clientId;
    }


    // Getter and setter for number of players
    public Integer getNumOfPlayers() throws RemoteException {
        return updateListener.getNumOfPlayers();
    }

    public void setNumOfPlayers(Integer numOfPlayers) {
        try {
            updateListener.setNumOfPlayers(numOfPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void setLobbyRoomController(LobbyRoomControllerInterface lobbyRoomController) {
        try {
            updateListener.setLobbyRoomController(lobbyRoomController);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    // Getter for game name using gameId, handles RemoteException
    public String getGameName(UUID gameId) throws RemoteException {
        try {
            return serverStub.getGameName(gameId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Client unable to get Game Name";
        }
    }

    // Setter for game name
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    // Getter and setter for gameId
    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    // Method to add a player to a game
    public void addPlayer(String playerName, UUID gameId) throws RemoteException {
        try {
            serverStub.addPlayer(this.clientId, updateListener, playerName, gameId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to start a game
    public void startGame() throws RemoteException {
        try {
            serverStub.startGame(this.gameId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to create or join a game session
    public void createGameSession() {
        try {
            gameId = serverStub.createGameSession(clientId, updateListener, gameName, playerName, updateListener.getNumOfPlayers());
            System.out.println("Game session created/joined.");
        } catch (Exception e) {
            System.err.println("Error creating/joining game session: ");
            e.printStackTrace();
        }
    }

    // Method to start a game table session
    public boolean startGameTable() {
        try {
            boolean isStartTableGameAllowed = serverStub.checkStartTable(gameId, playerName);
            if (isStartTableGameAllowed) {
                serverStub.startGameTable(gameId);
                System.out.println("Running");
                int numOfPlayers = updateListener.getNumOfPlayers();
                updateListener.setNumOfPlayers(numOfPlayers);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error starting game table session: ");
            e.printStackTrace();
            return false;
        }
    }

    // Method to check if a game is ready
    public boolean isGameReady(UUID gameId) throws RemoteException {
        try {
            return serverStub.isGameReady(gameId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

// Method to retrieve the player list

    public Map<UUID, ServerPlayer> getPlayers() throws RemoteException {
        try {
            return serverStub.getServerPlayers(gameId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to check if a game exists
    public boolean doesGameExist(UUID gameId) throws RemoteException {
        try {
            return serverStub.doesGameExit(gameId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to connect to the server
    public void connectToServer() {
        try {
            // Connect to the server's registry
            Registry registry = LocateRegistry.getRegistry(15000);
            //Registry registry = LocateRegistry.getRegistry("192.168.2.104", 15000);

            System.out.println("Verbindung zum Server hergestellt");

            // Compare the client's interface with the server's registry
            serverStub = (ServerInter) registry.lookup("ServerInter");
            System.out.println("Registry-Bibliothek gefunden");

            // Store and print the response from the server
            String response = serverStub.sayHello();
            System.out.println("Server says: " + response);
            serverStub.registerClient(clientId, updateListener);
            this.isConnectedToServer = true;


        } catch (Exception e) {
            // Handle exceptions in server connection
            System.err.println("Client exception: " + e);
            e.printStackTrace();

        }
    }

    public void disconnectFromServer(boolean isLeavingGameSession) {
        try {
            if (serverStub != null) {
                serverStub.unregisterClient(this.clientId, gameId, isLeavingGameSession);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    // DrawCard

    @Override
    public void drawCard() throws RemoteException {
        serverStub.drawCard(clientId, gameId);
    }


    @Override
    public void hahnKlauen() throws RemoteException {
        serverStub.hahnKlauen(clientId, gameId);
    }

    @Override
    public void karteUmtauschen(Integer eggPoints, ArrayList<ServerCard> selectedCards) throws RemoteException {
        serverStub.karteUmtauschen(clientId, gameId, eggPoints, selectedCards);
    }

    @Override
    public ServerPlayer getRoosterPlayer() throws RemoteException {
        return serverStub.getRoosterPlayer(gameId);
    }

    @Override
    public void stealOneCard(UUID target, ArrayList<ServerCard> selectedCards) throws RemoteException {
        serverStub.stealOneCard(target, selectedCards, clientId, gameId);
    }

    @Override
    public void stealAllCards(UUID target) throws RemoteException {
        serverStub.stealAllCards(target, clientId, gameId);
    }


    @Override
    public void endPlayerTurn() throws RemoteException {
        serverStub.endPlayerTurn(gameId);
    }

    @Override
    public void stealingInProgress(UUID playerId, UUID targetId, ArrayList<ServerCard> selectedCards) throws RemoteException {
        serverStub.stealingInProgress(gameId, playerId, targetId, selectedCards);
    }

    @Override
    public void removeFoxCard(ServerCard foxCard) throws RemoteException {
        serverStub.removeFoxCard(gameId, foxCard);
    }

    @Override
    public List<UUID> getPlayerIDList() throws RemoteException {
        return serverStub.getPlayerIDList(gameId);
    }

    @Override
    public void leaveLobbyRoom() throws RemoteException {
        serverStub.leaveLobbyRoom(gameId, clientId);
    }

    @Override
    public Map<String, UUID> getGameSessionIds() throws RemoteException {
        return serverStub.getGameSessionIds();
    }

    public void setJoinGameController(JoinGameControllerInterface joinGameController) {
        try {
            updateListener.setJoinGameController(joinGameController);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isGameFull(UUID gameId) throws RemoteException {
        return serverStub.isGameFull(gameId);
    }
}
