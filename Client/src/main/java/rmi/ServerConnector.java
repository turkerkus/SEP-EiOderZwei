package rmi;

import java.io.IOException;
import java.rmi.NotBoundException;


public class ServerConnector {
    private static ServerConnector serverConnector;
    ChatController controller;


    public static ServerConnector getServerConnector() throws IOException, NotBoundException {
        if(serverConnector == null){
            serverConnector = new ServerConnector();
        }
        return serverConnector;
    }

    public ChatController getController(){
        return controller;
    }
}
