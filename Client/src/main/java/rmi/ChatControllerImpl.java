package rmi;

import eoz.client.lobbyToTable.Spieler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatControllerImpl extends UnicastRemoteObject implements  ChatController {

    private ChatService service = new ChatServiceImpl();

    public ChatControllerImpl() throws RemoteException {
    }
    @Override
    public boolean notifyAllClients(String username, String message) throws RemoteException {
        return service.notifyAllClients(username, message);
    }

    @Override
    public ArrayList<String> getAll() throws RemoteException, SQLException {
        return service.getAll();
    }

    @Override
    public Spieler get(String username) throws RemoteException, SQLException {
        return service.get(username);
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
    public boolean isReserved(String username) throws RemoteException {
        return service.isReserved(username);
    }



    @Override
    public boolean addNewUser(Spieler spieler) throws RemoteException, SQLException {
        return service.addNewUser(spieler);
    }
}
