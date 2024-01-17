package sharedClasses;

import eoz.client.lobbyToTable.LobbyRoomController;
import eoz.client.lobbyToTable.tableController;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public interface ClientInter extends Remote, Serializable {
    UUID getClientId() throws RemoteException;

    UUID getGameId() throws RemoteException;

    String getGameName(UUID gameId) throws RemoteException;

    void setGameName(String gameName) throws RemoteException;

    void startGame() throws RemoteException;

    void createGameSession() throws RemoteException;

    boolean startGameTable() throws RemoteException;

    boolean isGameReady() throws RemoteException;

    Map<UUID, ServerPlayer> getPlayers() throws RemoteException;

    boolean doesGameExist(UUID gameId) throws RemoteException;

    void addPlayer(String playerName, UUID gameId) throws RemoteException;

    Integer getNumOfPlayers() throws RemoteException;

    void setNumOfPlayers(Integer numOfPlayers) throws RemoteException;


    void setLobbyRoomController(LobbyRoomController lobbyRoomController);

    String getClientName()throws RemoteException;

    void setTableController(tableController tableController) throws RemoteException;



}