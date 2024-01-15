package eoz.client.lobbyToTable;

import sharedClasses.Card;

import javax.swing.*;
import java.util.Collections;
import java.util.Stack;

public class Ablagestapel {

    private Stack<Card> ablagestapel;


    public Ablagestapel() {
        ablagestapel = new Stack<>();

    }
    public void pushCardAblagestapel(Card card){
        ablagestapel.push(card);
    }
    public Card readFirstElement() {
        return  ablagestapel.peek();
    }

}
