package rmi;

import sharedClasses.ClientUIUpdateListener;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public class ServerCallback implements Serializable {

    public void addClientListeners(UUID clientId, ClientUIUpdateListener clientListeners) {
        this.clientListeners.put(clientId, clientListeners);
    }

    private Map<UUID, ClientUIUpdateListener> clientListeners;
    public ServerCallback(Map<UUID, ClientUIUpdateListener> clientListeners) {
        this.clientListeners = clientListeners;
    }

    public void updateGameSessionList(ClientUIUpdateListener hostListener) throws RemoteException {
        clientListeners.values().stream().forEach(listner ->{
            try {
                if(!hostListener.equals(listner)) listner.updateGameSessionList();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeClientListener(UUID clientId) {
        clientListeners.remove(clientId);
    }
}
