package sharedClasses;

import eoz.client.lobbyToTable.Card;
import rmi.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The ServerInter interface serves as a library for the RMI Registry, allowing the client to request listed methods.
 * Nothing is started here; it is loaded into the Registry by the server during startup.
 */
public interface ServerInter extends Remote {

    /**
     * Returns a greeting message.
     *
     * @return A greeting message as a string.
     * @throws RemoteException if a remote communication error occurs.
     */
    String sayHello() throws RemoteException;

    /**
     * Gets the list of players in a game session identified by its game ID.
     *
     * @param gameId The UUID of the game session.
     * @return The list of ServerPlayer objects representing players in the game session.
     * @throws RemoteException if a remote communication error occurs.
     */
    Map<UUID, ServerPlayer> getServerPlayers(UUID gameId) throws RemoteException;

    /**
     * Creates a new game session and returns its UUID.
     *
     * @param clientID     The UUID of the client creating the game session.
     * @param listener     The listener that is used to update the client UI
     * @param gameName     The name of the game.
     * @param playerName   The name of the player creating the game session.
     * @param numOfPlayers The number of players in the game session.
     * @return The UUID of the created game session.
     * @throws RemoteException if a remote communication error occurs.
     */
    UUID createGameSession(UUID clientID, ClientUIUpdateListener listener, String gameName, String playerName, Integer numOfPlayers) throws RemoteException;

    /**
     * Checks if a game session identified by its game ID is ready to start.
     *
     * @param gameId The UUID of the game session.
     * @return True if the game session is ready to start, false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    boolean isGameReady(UUID gameId) throws RemoteException;

    /**
     * Initiates the start of a game session's table.
     *
     * @param gameId The UUID of the game session.
     * @throws RemoteException if a remote communication error occurs.
     */
    void startGameTable(UUID gameId) throws RemoteException;

    /**
     * Starts the game identified by its game ID.
     *
     * @param gameId The UUID of the game session.
     * @throws RemoteException if a remote communication error occurs.
     */
    void startGame(UUID gameId) throws RemoteException;

    /**
     * Checks if a game session identified by its game ID exists.
     *
     * @param gameId The UUID of the game session.
     * @return True if the game session exists, false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    boolean doesGameExit(UUID gameId) throws RemoteException;

    /**
     * Gets the name of the game identified by its game ID.
     *
     * @param gameID The UUID of the game.
     * @return The name of the game.
     * @throws RemoteException if a remote communication error occurs.
     */
    String getGameName(UUID gameID) throws RemoteException;

    /**
     * Adds a player to a game session identified by its game ID.
     *
     * @param clientID   The UUID of the client adding the player.
     * @param listener     The listener that is used to update the client UI
     * @param playerName The name of the player.
     * @param gameID     The UUID of the game session.
     * @throws RemoteException if a remote communication error occurs.
     */
    void addPlayer(UUID clientID, ClientUIUpdateListener listener, String playerName, UUID gameID) throws RemoteException;

    /**
     * Checks if a player with the given name is allowed to start the game table in a game session.
     *
     * @param gameID     The UUID of the game session.
     * @param playerName The name of the player.
     * @return True if the player is allowed to start the game table, false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    boolean checkStartTable(UUID gameID, String playerName) throws RemoteException;

    /**
     * Registers a client with a unique client ID and a ClientUIUpdateListener for updates.
     *
     * @param clientId The UUID of the client.
     * @param listener The ClientUIUpdateListener to register.
     * @throws RemoteException if a remote communication error occurs.
     */
    void registerClient(UUID clientId, ClientUIUpdateListener listener) throws RemoteException;

    /**
     * Lets a player draw a card as a round action.
     *
     * @param clientId The UUID of the current Client drawing the card.
     * @param gameId the UUID of the game session the client is in
     * @throws RemoteException if a remote communication error occurs.
     */
    void drawCard(UUID clientId, UUID gameId) throws RemoteException;

    /**
     *
     * @param card The card to discard.
     * @param clientId The Client who wants to discard.
     * @param gameId The UUID of the game where this is happening.
     * @throws RemoteException if a remote communication error occurs.
     */
    void discardCard(ServerCard card, UUID clientId, UUID gameId) throws RemoteException;

    /**
     * Lets a player steal a rooter card .
     *
     * @param clientId The UUID of the current Client drawing the card.
     * @param gameId the UUID of the game session the client is in
     * @throws RemoteException if a remote communication error occurs.
     */
    void hahnKlauen(UUID clientId, UUID gameId) throws RemoteException;

    /**
     * Lets a player change card .
     *
     * @param clientId The UUID of the current Client drawing the card.
     * @param gameId the UUID of the game session the client is in
     * @throws RemoteException if a remote communication error occurs.
     */
    void karteUmtauschen(UUID clientId, UUID gameId) throws RemoteException;

    ServerPlayer getRoosterPlayer(UUID gameId) throws RemoteException;

    void unregisterClient(UUID clientId, UUID gameID) throws RemoteException;
}
