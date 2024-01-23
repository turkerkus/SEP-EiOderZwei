package rmi;

import sharedClasses.ChatObserver;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatObservable {
    private final ArrayList<ChatObserver> chatObserverList = new ArrayList<>();

    public boolean addChatObserver(ChatObserver chatObserver) throws RemoteException{
        return chatObserverList.add(chatObserver);
    }

    public boolean removeChatObserver(ChatObserver chatObserver) throws RemoteException{
        return chatObserverList.remove(chatObserver);
    }

    public boolean notifyAllClients(String username, String message) throws RemoteException{
        for(ChatObserver observer : chatObserverList){
            observer.update(username, message);
        }
        return true;
    }
}
