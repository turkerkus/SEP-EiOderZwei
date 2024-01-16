package sharedClasses;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

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
    public void setPlayerTurn(boolean playerTurn) throws RemoteException {
        this.tableController.setPlayerTurn(true);
    }

    @Override
    public void setCurrentPlayerIndex(Integer index) throws RemoteException {
        this.tableController.setCurrentPlayerIndex(index);
    }

    @Override
    public void setTimeLeft(Integer timeLeft) throws RemoteException {
        this.tableController.setTimeLeft(timeLeft);
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
    public void updateUI(String command) throws RemoteException {
        Platform.runLater(() -> {

            if ("switchToTable".equals(command)) {
                System.out.println("switching to table begin");
                lobbyRoomController.setUiUpdateFromServer(true);
                lobbyRoomController.setNumOfPlayers(numOfPlayers);
                lobbyRoomController.switchSceneToTable();
                System.out.println("switching to table successful");
            } else if ("startGame".equals(command)) {
                System.out.println("updating the UI for player when the game start Begins");
                tableController.startGameUiUpdate();
                System.out.println("updating the UI for player when the game start successful");
            }
            else if ("updateTimerLabel".equals(command)) {
                System.out.println("updating the timer label for player  Begins");
                tableController.updateTimerLabel();
                System.out.println("updating the timer label for player  successful");
            }
            // Handle other commands as necessary
        });
    }
}

