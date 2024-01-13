package rmi;

import eoz.client.lobbyToTable.Spieler;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.sql.SQLException;

public interface ChatController extends Remote {
    boolean notifyAllClients(String username, String message) throws RemoteException;
    ArrayList<String> getAll() throws RemoteException,SQLException;
    Spieler get (String username) throws RemoteException, SQLException;
    boolean addChatObserver(ChatObserver chatObserver) throws RemoteException;
    boolean removeChatObserver(ChatObserver chatObserver) throws RemoteException;
    boolean isReserved(String username) throws RemoteException;
    boolean addNewUser(Spieler spieler) throws RemoteException, SQLException;

}
