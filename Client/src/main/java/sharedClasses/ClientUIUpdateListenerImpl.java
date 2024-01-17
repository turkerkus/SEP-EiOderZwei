package sharedClasses;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.UUID;

import eoz.client.lobbyToTable.LobbyRoomController;
import eoz.client.lobbyToTable.tableController;
import javafx.application.Platform;

public class ClientUIUpdateListenerImpl extends UnicastRemoteObject implements ClientUIUpdateListener, Serializable {


    public void setLobbyRoomController(LobbyRoomController lobbyRoomController) {
        this.lobbyRoomController = lobbyRoomController;
    }

    private LobbyRoomController lobbyRoomController;

    public tableController getTableController() {
        return tableController;
    }

    public void setTableController(tableController tableController) {
        this.tableController = tableController;
        System.out.println("the table controller is set");
    }



    @Override
    public void setCurrentPlayerID(UUID playerID) throws RemoteException {
        this.tableController.setCurrentPlayerID(playerID);
    }

    @Override
    public void setTimeLeft(Integer timeLeft) throws RemoteException {
        this.tableController.updateTimerLabel(timeLeft);
    }

    private tableController tableController;

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }



    private String clientName;

    public String getClientName() {
        return clientName;
    }

    public Integer getNumOfPlayers() {
        return numOfPlayers;
    }



    private Integer numOfPlayers;
    public ClientUIUpdateListenerImpl() throws RemoteException {

    }

    public ClientUIUpdateListenerImpl(int numOfPlayers) throws RemoteException {
        this.numOfPlayers = numOfPlayers;
    }
    @Override
    public LobbyRoomController getLobbyRoomController()throws RemoteException{
        return lobbyRoomController;
    }

    @Override
    public void setNumOfPlayers(int numOfPlayers) throws RemoteException {
        this.numOfPlayers = numOfPlayers;
        if(lobbyRoomController != null){
            lobbyRoomController.setNumOfPlayers(numOfPlayers);
        }

    }

    @Override
    public void hahnKarteGeben(UUID playerId) throws RemoteException{
        tableController.hahnKarteGeben(playerId);
    }


    @Override
    public void updateUI(String command) throws RemoteException {
        Platform.runLater(() -> {

            if ("switchToTable".equals(command)) {
                lobbyRoomController.setUiUpdateFromServer(true);
                lobbyRoomController.setNumOfPlayers(numOfPlayers);
                lobbyRoomController.switchSceneToTable();
            } else if ("startPlayerTurn".equals(command)) {
                tableController.startGameUiUpdate();
            }

        });
    }
}

