package eoz.client.lobbyToTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck1 {
    //deck steht für das Deck dargestellt als Stack
    private final Stack<Card1> deck = new Stack<>();

    //Steht für die Anzahl der Karten im Deck
    private int cardCount = 0;

    //coverCard steht für die Art von Deck. False für Ablage und True für ZiehDeck
    private final boolean coverCard;

    public Deck1(boolean coverCard){
        this.coverCard = coverCard;
    }

    public Card1 ziehen(){
        if(deck.isEmpty()){
            return  null;
        }
        if(coverCard){ //Das ist die ZiehDeck
            Card1 card = deck.pop();
            cardCount = cardCount - 1;
            return card;
        }
        return null;
    }
    public boolean ablegen(Card1 card){
        if (!coverCard){    //Ablagestapel
            deck.add(card);
            cardCount = cardCount + 1;
            return true;
        } else
            return false;
    }

    public void mischen(){
        ArrayList<Card1> cardDeck = new ArrayList<>();

        while(!deck.isEmpty()) { // deck leeren und in liste packen
            Card1 card = deck.pop();
            cardDeck.add(card);
        }
        Collections.shuffle(cardDeck); //shuffle liste

        int len = cardDeck.size();
        for(int i = 0; i <= len-1; i++){
            deck.push(cardDeck.get(i)); //wieder auf stack packen
        }
    }

    public int getCardCount(){
        return  cardCount;
    }

    public Card1 getTopCard(){
        return deck.peek();
    }

    public void addCard(Card1 card){
        if(coverCard){
            deck.push(card);
            cardCount = cardCount + 1;
        }
    }
}
