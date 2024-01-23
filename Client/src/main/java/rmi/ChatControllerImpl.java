package rmi;

import eoz.client.lobbyToTable.Client;
import sharedClasses.ChatController;
import sharedClasses.ChatObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatControllerImpl extends UnicastRemoteObject implements ChatController {
    private ChatService service = new ChatServiceImpl();

    public ChatControllerImpl() throws RemoteException{}


    @Override
    public boolean notifyAllClients(String username, String message) throws RemoteException {
        return service.notifyAllClients(username, message);
    }

    @Override
    public boolean addChatObserver(ChatObserver chatObserver) throws RemoteException {
        return service.addChatObserver(chatObserver);
    }

    @Override
    public boolean removeChatObserver(ChatObserver chatObserver) throws RemoteException {
        return service.removeChatObserver(chatObserver);
    }

    @Override
    public Client get(String username) throws RemoteException {
        return service.get(username);
    }
}
