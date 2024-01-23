package sharedClasses;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatObserver extends Remote {
    boolean update(String username, String message) throws RemoteException;
    String getUsername() throws RemoteException;
}
