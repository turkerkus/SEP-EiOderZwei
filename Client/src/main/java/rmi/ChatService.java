package rmi;

import eoz.client.lobbyToTable.Spieler;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ChatService {
    boolean notifyAllClients(String username, String message) throws RemoteException;
    ArrayList<String> getAll() throws RemoteException, SQLException;
    Spieler get(String username) throws RemoteException, SQLException;
    boolean addChatObserver(ChatObserver chatObserver)throws RemoteException;
    boolean removeChatObserver(ChatObserver chatObserver)throws RemoteException;
    boolean isReserved(String username) throws RemoteException;
    boolean addNewUser(Spieler spieler) throws RemoteException, SQLException;
}
