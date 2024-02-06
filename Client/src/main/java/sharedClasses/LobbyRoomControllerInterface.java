package sharedClasses;

public interface LobbyRoomControllerInterface {

    void setNumOfPlayers(Integer numOfPlayers);

    void setUiUpdateFromServer(boolean uiUpdateFromServer);

    void switchSceneToTable();

    void addPlayerToLobby(String playerName, Integer numOfPlayersPresent);

    void removePLayer(String isHostPlayer);
}
