package sharedClasses;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

// Das Interface dient als "Bibliothek" fÃ¼r die Registry, damit der Client aufgelistete Methoden anfragen kann
// Hier wird nichts gestartet, dieser wird vom Server beim Start in die Registry geladen
public interface ServerInter extends Remote {

    String sayHello() throws RemoteException;
    List<ServerPlayer> getPlayerList(UUID gameId) throws RemoteException;

    UUID createGameSession(UUID clientID,String gameName, String playerName, Integer numOfPlayers) throws RemoteException;


    boolean isGameReady(UUID gameId) throws RemoteException ;

    //long getRemainingGameSessionTimeMillis(UUID gameId)throws RemoteException ;

    void startGameTable(UUID gameId)throws RemoteException ;
    void startGame(UUID gameId) throws RemoteException;
    boolean doesGameExit(UUID gameId)throws RemoteException;

    String getGameName(UUID gameID)throws RemoteException;

    void addPlayer(UUID ClientID, String playerName, UUID gameID)throws RemoteException;

    boolean checkStartTable(UUID gameID, String playerName) throws RemoteException;


    String getHosPlayerName(UUID gameID)throws RemoteException;


    void registerClient(UUID clientId, ClientUIUpdateListener listener)throws RemoteException;






}