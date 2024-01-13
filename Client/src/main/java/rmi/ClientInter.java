package rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInter extends Remote, Serializable {
    void retrieveMessage(String message) throws RemoteException;

}
