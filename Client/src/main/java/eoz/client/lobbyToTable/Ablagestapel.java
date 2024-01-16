package eoz.client.lobbyToTable;

import sharedClasses.ServerCard;

import java.util.Stack;

public class Ablagestapel {

    private Stack<ServerCard> ablagestapel;


    public Ablagestapel() {
        ablagestapel = new Stack<>();

    }
    public void pushCardAblagestapel(ServerCard serverCard){
        ablagestapel.push(serverCard);
    }
    public ServerCard readFirstElement() {
        return  ablagestapel.peek();
    }

}
