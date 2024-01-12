package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

// Das Interface dient als "Bibliothek" fÃ¼r die Registry, damit der Client aufgelistete Methoden anfragen kann
// Hier wird nichts gestartet, dieser wird vom Server beim Start in die Registry geladen
public interface ServerInter extends Remote {
    String sayHello() throws RemoteException;
    List<serverPlayer> getPlayerList(UUID gameId) throws RemoteException;

    UUID createGameSession(String playerName, Integer numOfPlayers) throws RemoteException;

    boolean isGameReady(UUID gameId) throws RemoteException ;

    long getRemainingGameSessionTimeMillis(UUID gameId)throws RemoteException ;

    void waitingForPlayers(UUID gameId)throws RemoteException ;
    void startGame(UUID gameId) throws RemoteException;

}