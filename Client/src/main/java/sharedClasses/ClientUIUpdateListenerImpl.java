package sharedClasses;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

import eoz.client.lobbyToTable.LobbyRoomController;
import javafx.application.Platform;

public class ClientUIUpdateListenerImpl extends UnicastRemoteObject implements ClientUIUpdateListener {


    public void setLobbyRoomController(LobbyRoomController lobbyRoomController) {
        this.lobbyRoomController = lobbyRoomController;
    }

    private LobbyRoomController lobbyRoomController;

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
    }


    @Override
    public void updateUI(String command) throws RemoteException {
        Platform.runLater(() -> {
            System.out.println("switching to table begin");
            if ("switchToTable".equals(command)) {
                lobbyRoomController.setUiUpdateFromServer(true);
                lobbyRoomController.setNumOfPlayers(numOfPlayers);
                lobbyRoomController.switchSceneToTable();
                System.out.println("switching to table successful");
            }
            // Handle other commands as necessary
        });
    }
}

