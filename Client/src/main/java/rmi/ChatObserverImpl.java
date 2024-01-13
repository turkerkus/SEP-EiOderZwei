package rmi;

import eoz.client.lobbyToTable.Spieler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChatObserverImpl extends UnicastRemoteObject implements ChatObserver {
    private ChatObserver observer;

    public ChatObserverImpl(ChatObserver observer) throws RemoteException{
        this.observer = observer;
    }

    @Override
    public boolean update(String username, String message) throws RemoteException {
        return observer.update(username, message);
    }

    @Override
    public ArrayList<Spieler> getOnlineUsers() throws RemoteException {
        return observer.getOnlineUsers();
    }

    @Override
    public String getUsername() throws RemoteException {
        return observer.getUsername();
    }
}
