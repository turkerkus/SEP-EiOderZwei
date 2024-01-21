package sharedClasses;

import eoz.client.lobbyToTable.LobbyRoomController;
import eoz.client.lobbyToTable.TableController;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ClientUIUpdateListener extends Remote {
    void updateUI(String command) throws RemoteException;
    String getClientName() throws RemoteException;
    void setClientName(String clientName) throws RemoteException;
    LobbyRoomController getLobbyRoomController() throws RemoteException;

    void setNumOfPlayers(int numOfPlayers)throws RemoteException;

    Integer getNumOfPlayers() throws RemoteException;
    void setLobbyRoomController(LobbyRoomController lobbyRoomController) throws RemoteException;


    void setTableController(TableController tableController) throws RemoteException;


    void setCurrentPlayerID(UUID playerID) throws RemoteException;

    void setTimeLeft(Integer timeLeft) throws RemoteException;

    void hahnKarteGeben(UUID playerId) throws RemoteException;


    void playerLeftGameSession(UUID disconnectedPlayerID, String botName) throws RemoteException;

    void hasDrawnACard(UUID playerId, ServerCard serverCard) throws RemoteException;

    void changeRoosterPlayer(UUID oldRoosterPlayerID, UUID newRoosterPlayerID) throws RemoteException;

    void drawnKuckuckCard() throws RemoteException;

    void drawnFoxCard () throws RemoteException;

}
