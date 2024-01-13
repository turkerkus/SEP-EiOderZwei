package rmi;

import eoz.client.lobbyToTable.Spieler;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatServiceImpl implements ChatService{
    private static final ChatObservable CHAT_OBSERVABLE = new ChatObservable();
    private Spieler spieler = new Spieler();

    @Override
    public boolean notifyAllClients(String username, String message) throws RemoteException {
        return CHAT_OBSERVABLE.notifyAllClients(username, message);
    }

    @Override
    public ArrayList<String> getAll() throws RemoteException, SQLException {

        return null; //TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO
    }

    @Override
    public Spieler get(String username) throws RemoteException, SQLException {
        return spieler; //TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO
    }

    @Override
    public boolean addChatObserver(ChatObserver chatObserver) throws RemoteException {
        return CHAT_OBSERVABLE.addChatObserver(chatObserver);
    }

    @Override
    public boolean removeChatObserver(ChatObserver chatObserver) throws RemoteException {
        return CHAT_OBSERVABLE.removeChatObserver(chatObserver);
    }

    @Override
    public boolean isReserved(String username) throws RemoteException {
        return CHAT_OBSERVABLE.isReserved(username);
    }



    @Override
    public boolean addNewUser(Spieler spieler) throws RemoteException, SQLException {
        return true; //TODO //TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO
    }
}
