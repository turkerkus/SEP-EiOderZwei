package rmi;

import eoz.client.lobbyToTable.Client;
import sharedClasses.ChatObserver;

import java.rmi.RemoteException;

public class ChatServiceImpl implements ChatService {
    private static final ChatObservable CHAT_OBSERVABLE = new ChatObservable();
    @Override
    public boolean notifyAllClients(String username, String message) throws RemoteException {
        return CHAT_OBSERVABLE.notifyAllClients(username, message);
    }

    @Override
    public Client get(String username) throws RemoteException {
        return null;
    }

    @Override
    public boolean addChatObserver(ChatObserver chatObserver) throws RemoteException {
        return CHAT_OBSERVABLE.addChatObserver(chatObserver);
    }

    @Override
    public boolean removeChatObserver(ChatObserver chatObserver) throws RemoteException {
        return CHAT_OBSERVABLE.removeChatObserver(chatObserver);
    }

}
