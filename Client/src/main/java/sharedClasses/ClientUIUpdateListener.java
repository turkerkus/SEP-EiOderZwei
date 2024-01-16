package sharedClasses;

import eoz.client.lobbyToTable.LobbyRoomController;
import eoz.client.lobbyToTable.tableController;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientUIUpdateListener extends Remote {
    void updateUI(String command) throws RemoteException;
    String getClientName() throws RemoteException;
    void setClientName(String clientName) throws RemoteException;
    LobbyRoomController getLobbyRoomController() throws RemoteException;

    void setNumOfPlayers(int numOfPlayers)throws RemoteException;

    Integer getNumOfPlayers() throws RemoteException;
    void setLobbyRoomController(LobbyRoomController lobbyRoomController) throws RemoteException;


    void setTableController(tableController tableController) throws RemoteException;

    void setPlayerTurn(boolean playerTurn) throws  RemoteException;

    void setCurrentPlayerIndex(Integer index) throws RemoteException;

    void setTimeLeft(Integer timeLeft) throws RemoteException;
}
