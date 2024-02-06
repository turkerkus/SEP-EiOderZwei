package sharedClasses;

import eoz.client.lobbyToTable.TableController;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

public interface ClientUIUpdateListener extends Remote {
    void updateUI(String command) throws RemoteException;

    String getClientName() throws RemoteException;

    void setClientName(String clientName) throws RemoteException;

    Integer getNumOfPlayers() throws RemoteException;

    void setNumOfPlayers(int numOfPlayers) throws RemoteException;

    void setLobbyRoomController(LobbyRoomControllerInterface lobbyRoomController) throws RemoteException;


    void setTableController(TableController tableController) throws RemoteException;


    void setCurrentPlayerID(UUID playerID) throws RemoteException;

    void setTimeLeft(Integer timeLeft) throws RemoteException;

    void hahnKarteGeben(UUID playerId) throws RemoteException;


    void playerLeftGameSession(UUID disconnectedPlayerID, String botName) throws RemoteException;

    void hasDrawnACard(UUID playerId, ServerCard serverCard) throws RemoteException;

    void changeRoosterPlayer(UUID oldRoosterPlayerID, UUID newRoosterPlayerID) throws RemoteException;

    void drawnKuckuckCard(UUID playerID, ServerCard kuckuckCard) throws RemoteException;

    void drawnFoxCard(UUID playerID, ServerCard foxCard) throws RemoteException;

    void cardDiscarded(UUID playerID, ServerCard discardedCard, Integer eggPoints, ArrayList<ServerCard> selectedCards) throws RemoteException;

    void oneCardStolen(UUID target, ServerCard stolenCard, UUID activeSpielerID) throws RemoteException;

    void allCardsStolen(UUID target, UUID playerId) throws RemoteException;

    void updateChat(String message, UUID playerId) throws RemoteException;

    void switchToResultTable(ServerPlayer winner) throws RemoteException;

    void stealingCardCompleted(UUID target, UUID thief, ArrayList<ServerCard> stollenCards) throws RemoteException;

    void removeFoxCard(ServerCard drawnCard) throws RemoteException;

    void addPlayerToLobby(String playerName, Integer numOfPlayersPresent) throws RemoteException;


    void updateGameSessionList() throws RemoteException;

    void setJoinGameController(JoinGameControllerInterface joinGameController) throws RemoteException;
}
