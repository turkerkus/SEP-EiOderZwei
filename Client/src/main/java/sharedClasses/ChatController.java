package sharedClasses;

import eoz.client.lobbyToTable.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatController extends Remote {
    boolean notifyAllClients(String username, String message) throws RemoteException;
    boolean addChatObserver(ChatObserver chatObserver) throws RemoteException;
    boolean removeChatObserver(ChatObserver chatObserver) throws RemoteException;

    Client get(String username) throws RemoteException;

}
