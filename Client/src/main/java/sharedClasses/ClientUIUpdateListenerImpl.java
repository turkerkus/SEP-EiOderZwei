package sharedClasses;


import eoz.client.lobbyToTable.TableController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;

public class ClientUIUpdateListenerImpl extends UnicastRemoteObject implements ClientUIUpdateListener, Serializable {

    private LobbyRoomControllerInterface lobbyRoomController;
    private TableControllerInterface tableController;
    private String clientName;
    private Integer numOfPlayers;
    private JoinGameControllerInterface joinGameController;

    public ClientUIUpdateListenerImpl() throws RemoteException {

    }

    public void setJoinGameController(JoinGameControllerInterface joinGameController) {
        this.joinGameController = joinGameController;
    }

    public void setLobbyRoomController(LobbyRoomControllerInterface lobbyRoomController) {
        this.lobbyRoomController = lobbyRoomController;
    }

    public void setTableController(TableController tableController) {
        this.tableController = (TableControllerInterface) tableController;
    }

    @Override
    public void setCurrentPlayerID(UUID playerID) throws RemoteException {
        this.tableController.setCurrentPlayerID(playerID);
    }

    @Override
    public void setTimeLeft(Integer timeLeft) throws RemoteException {
        this.tableController.updateTimerLabel(timeLeft);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getNumOfPlayers() {
        return numOfPlayers;
    }

    @Override
    public void setNumOfPlayers(int numOfPlayers) throws RemoteException {
        this.numOfPlayers = numOfPlayers;
        if (lobbyRoomController != null) {
            lobbyRoomController.setNumOfPlayers(numOfPlayers);
        }

    }

    @Override
    public void hahnKarteGeben(UUID playerId) throws RemoteException {
        tableController.hahnKarteGeben(playerId);
    }

    @Override
    public void playerLeftGameSession(UUID disconnectedPlayerID, String botName) throws RemoteException {
        if (tableController != null) {
            tableController.playerLeftGameSession(disconnectedPlayerID, botName);
        } else if (lobbyRoomController != null) {
            lobbyRoomController.removePLayer(botName);
        }

    }

    @Override
    public void hasDrawnACard(UUID playerId, ServerCard serverCard) throws RemoteException {
        tableController.hasDrawnACard(playerId, serverCard);
    }

    @Override
    public void changeRoosterPlayer(UUID oldRoosterPlayerID, UUID newRoosterPlayerID) throws RemoteException {
        tableController.changeRoosterPlayer(oldRoosterPlayerID, newRoosterPlayerID);
    }


    @Override
    public void updateUI(String command) throws RemoteException {

            if ("switchToTable".equals(command)) {
                lobbyRoomController.setUiUpdateFromServer(true);
                lobbyRoomController.setNumOfPlayers(numOfPlayers);
                lobbyRoomController.switchSceneToTable();
            } else if ("startPlayerTurn".equals(command)) {
                tableController.startGameUiUpdate();
            }
    }

    @Override
    public void drawnKuckuckCard(UUID playerID, ServerCard kuckuckCard) throws RemoteException {
        tableController.drawnKuckuckCard(playerID, kuckuckCard);
    }

    @Override
    public void drawnFoxCard(UUID playerID, ServerCard foxCard) throws RemoteException {
        tableController.drawnFoxCard(playerID, foxCard);
    }

    @Override
    public void cardDiscarded(UUID playerID, ServerCard discardedCard, Integer eggPoints, ArrayList<ServerCard> selectedCards) throws RemoteException {
        tableController.cardDiscarded(playerID, discardedCard, eggPoints, selectedCards);
    }

    @Override
    public void oneCardStolen(UUID target, ServerCard stolenCard, UUID playerId) throws RemoteException {
        tableController.oneCardStolen(target, stolenCard, playerId);
    }

    @Override
    public void allCardsStolen(UUID target, UUID playerId) throws RemoteException {
        tableController.allCardsStolen(target, playerId);
    }

    @Override
    public void updateChat(String message, UUID playerId) throws RemoteException {
        tableController.updateChat(message, playerId);
    }

    @Override
    public void switchToResultTable(ServerPlayer winner) throws RemoteException {
        tableController.switchToResults(winner);
    }

    @Override
    public void stealingCardCompleted(UUID target, UUID thief, ArrayList<ServerCard> stollenCards) throws RemoteException {
        tableController.stealingCardCompleted(target, thief, stollenCards);
    }

    @Override
    public void removeFoxCard(ServerCard foxCard) throws RemoteException {
        tableController.removeFoxCard(foxCard);
    }

    @Override
    public void addPlayerToLobby(String playerName, Integer numOfPlayersPresent) throws RemoteException {
        lobbyRoomController.addPlayerToLobby(playerName, numOfPlayersPresent);
    }

    @Override
    public void updateGameSessionList() throws RemoteException {
        joinGameController.updateGameSessionList();
    }
}

