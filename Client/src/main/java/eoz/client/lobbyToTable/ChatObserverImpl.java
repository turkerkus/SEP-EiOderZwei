package eoz.client.lobbyToTable;

import sharedClasses.ChatObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
    public String getUsername() throws RemoteException {
        return observer.getUsername();
    }
}
