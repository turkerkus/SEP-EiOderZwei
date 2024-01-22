package sharedClasses;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.UUID;

import eoz.client.lobbyToTable.LobbyRoomController;
import eoz.client.lobbyToTable.TableController;
import javafx.application.Platform;

public class ClientUIUpdateListenerImpl extends UnicastRemoteObject implements ClientUIUpdateListener, Serializable {


    public void setLobbyRoomController(LobbyRoomController lobbyRoomController) {
        this.lobbyRoomController = lobbyRoomController;
    }

    private LobbyRoomController lobbyRoomController;

    public TableController getTableController() {
        return tableController;
    }

    public void setTableController(TableController tableController) {
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

    private TableController tableController;

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
    public void playerLeftGameSession(UUID disconnectedPlayerID, String botName) throws RemoteException{
        tableController.playerLeftGameSession(disconnectedPlayerID,botName);
    }

    @Override
    public void hasDrawnACard(UUID playerId, ServerCard serverCard) throws RemoteException{
        tableController.hasDrawnACard(playerId,serverCard);
    }

    @Override
    public void changeRoosterPlayer(UUID oldRoosterPlayerID, UUID newRoosterPlayerID) throws RemoteException {
        tableController.changeRoosterPlayer(oldRoosterPlayerID,newRoosterPlayerID);
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

    @Override
    public void drawnKuckuckCard(UUID playerID) throws RemoteException {
        tableController.drawnKuckuckCard(playerID);
    }

    @Override
    public void drawnFoxCard (UUID playerID) throws RemoteException {
        tableController.drawnFoxCard(playerID);
    }
}

