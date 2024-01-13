package rmi;

import eoz.client.lobbyToTable.Spieler;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ChatObserver extends Remote {
    boolean update(String username, String message) throws RemoteException;
    ArrayList<Spieler> getOnlineUsers() throws RemoteException;
    String getUsername() throws RemoteException;
    //boolean updateUI(ArrayList<String> playerList) throws RemoteException;
}
