package sharedClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck {
    //deck steht für das Deck_old dargestellt als Stack
    private final Stack<ServerCard> deck = new Stack<>();

    //Steht für die Anzahl der Karten im Deck_old
    private int cardCount = 0;

    //coverCard steht für die Art von Deck_old. False für Ablage und True für ZiehDeck
    private final boolean coverCard;

    public Deck(boolean coverCard){
        this.coverCard = coverCard;
    }

    public ServerCard ziehen(){
        if(deck.isEmpty()){
            return  null;
        }
        if(coverCard){ //Das ist die ZiehDeck
            ServerCard serverCard = deck.pop();
            cardCount = cardCount - 1;
            return serverCard;
        }
        return null;
    }
    public boolean ablegen(ServerCard serverCard){
        if (!coverCard){    //Ablagestapel
            deck.add(serverCard);
            cardCount = cardCount + 1;
            return true;
        } else
            return false;
    }

    public void mischen(){
        ArrayList<ServerCard> serverCardDeck = new ArrayList<>();

        while(!deck.isEmpty()) { // deck leeren und in liste packen
            ServerCard serverCard = deck.pop();
            serverCardDeck.add(serverCard);
        }
        Collections.shuffle(serverCardDeck); //shuffle liste

        int len = serverCardDeck.size();
        for(int i = 0; i <= len-1; i++){
            deck.push(serverCardDeck.get(i)); //wieder auf stack packen
        }
    }

    public int getCardCount(){
        return  cardCount;
    }

    public ServerCard getTopCard(){
        return deck.peek();
    }

    public void addCard(ServerCard serverCard){
        if(coverCard){
            deck.push(serverCard);
            cardCount = cardCount + 1;
        }
    }
}
