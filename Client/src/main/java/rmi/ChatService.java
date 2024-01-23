package rmi;

import eoz.client.lobbyToTable.Client;
import sharedClasses.ChatObserver;

import java.rmi.RemoteException;

public interface ChatService {
    boolean notifyAllClients(String username, String message) throws RemoteException;
    Client get(String username) throws RemoteException;
    boolean addChatObserver(ChatObserver chatObserver) throws  RemoteException;
    boolean removeChatObserver(ChatObserver chatObserver) throws RemoteException;

}
