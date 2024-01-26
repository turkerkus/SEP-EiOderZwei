package sharedClasses;

import eoz.client.lobbyToTable.LobbyRoomController;
import eoz.client.lobbyToTable.TableController;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * This interface defines the methods that can be invoked remotely by clients.
 */
public interface ClientInter extends Remote, Serializable {

    /**
     * Get the client's unique identifier.
     *
     * @return The UUID of the client.
     * @throws RemoteException if a remote communication error occurs.
     */
    UUID getClientId() throws RemoteException;

    /**
     * Get the unique identifier of the game associated with the client.
     *
     * @return The UUID of the game.
     * @throws RemoteException if a remote communication error occurs.
     */
    UUID getGameId() throws RemoteException;

    /**
     * Get the name of the game associated with a specific game ID.
     *
     * @param gameId The UUID of the game.
     * @return The name of the game.
     * @throws RemoteException if a remote communication error occurs.
     */
    String getGameName(UUID gameId) throws RemoteException;

    /**
     * Set the name of the game associated with the client.
     *
     * @param gameName The name of the game.
     * @throws RemoteException if a remote communication error occurs.
     */
    void setGameName(String gameName) throws RemoteException;

    /**
     * Start the game.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    void startGame() throws RemoteException;

    /**
     * Create a game session.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    void createGameSession() throws RemoteException;

    /**
     * Start the game table.
     *
     * @return True if the game table is started successfully, false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    boolean startGameTable() throws RemoteException;

    /**
     * Check if the game is ready to start.
     *
     * @return True if the game is ready, false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    boolean isGameReady() throws RemoteException;

    /**
     * Get the players in the game.
     *
     * @return A map of player UUIDs to ServerPlayer objects.
     * @throws RemoteException if a remote communication error occurs.
     */
    Map<UUID, ServerPlayer> getPlayers() throws RemoteException;

    /**
     * Check if a game with a specific game ID exists.
     *
     * @param gameId The UUID of the game to check.
     * @return True if the game exists, false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    boolean doesGameExist(UUID gameId) throws RemoteException;

    /**
     * Add a player to the game.
     *
     * @param playerName The name of the player to add.
     * @param gameId     The UUID of the game.
     * @throws RemoteException if a remote communication error occurs.
     */
    void addPlayer(String playerName, UUID gameId) throws RemoteException;

    /**
     * Get the number of players in the game.
     *
     * @return The number of players in the game.
     * @throws RemoteException if a remote communication error occurs.
     */
    Integer getNumOfPlayers() throws RemoteException;

    /**
     * Set the LobbyRoomController.
     *
     * @param lobbyRoomController The LobbyRoomController to set.
     */
    void setLobbyRoomController(LobbyRoomController lobbyRoomController);

    /**
     * Get the client's name.
     *
     * @return The name of the client.
     * @throws RemoteException if a remote communication error occurs.
     */
    String getClientName() throws RemoteException;

    /**
     * Set the TableController.
     *
     * @param tableController The TableController to set.
     * @throws RemoteException if a remote communication error occurs.
     */
    void setTableController(TableController tableController) throws RemoteException;

    /**
     * Let a player draw a card as a round action.
     *
     * @  The UUID of the current Client drawing the card.
     * @throws RemoteException if a remote communication error occurs.
     */
    void drawCard() throws RemoteException;


    /**
     * Let a player draw a card as a round action.
     *
     * @  The UUID of the current Client drawing the card.
     * @throws RemoteException if a remote communication error occurs.
     */
    void hahnKlauen() throws RemoteException;

    /**
     * Lets a player change card .
     *
     * @param eggPoints  contains the eggPoint, rest
     * @throws RemoteException if a remote communication error occurs.
     */
    void karteUmtauschen( Integer eggPoints,ArrayList<ServerCard> selectedCards ) throws RemoteException;

    ServerPlayer getRoosterPlayer() throws  RemoteException;

    void disconnectFromServer() throws RemoteException;

    void sendChatMessage(String content) throws RemoteException;

    void stealOneCard(UUID target, ArrayList<ServerCard> selectedCards) throws RemoteException;
    
    void stealAllCards(UUID target) throws RemoteException;

    void endPlayerTurn() throws RemoteException;

    void stealingInProgress(UUID playerId, UUID targetId, ArrayList<ServerCard> selectedCards) throws RemoteException;

    void removeFoxCard(ServerCard foxCard) throws  RemoteException;
}
