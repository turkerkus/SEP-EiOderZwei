package sharedClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck {
    //deck steht für das Deck_old dargestellt als Stack
    private final Stack<Card> deck = new Stack<>();

    //Steht für die Anzahl der Karten im Deck_old
    private int cardCount = 0;

    //coverCard steht für die Art von Deck_old. False für Ablage und True für ZiehDeck
    private final boolean coverCard;

    public Deck(boolean coverCard){
        this.coverCard = coverCard;
    }

    public Card ziehen(){
        if(deck.isEmpty()){
            return  null;
        }
        if(coverCard){ //Das ist die ZiehDeck
            Card card = deck.pop();
            cardCount = cardCount - 1;
            return card;
        }
        return null;
    }
    public boolean ablegen(Card card){
        if (!coverCard){    //Ablagestapel
            deck.add(card);
            cardCount = cardCount + 1;
            return true;
        } else
            return false;
    }

    public void mischen(){
        ArrayList<Card> cardDeck = new ArrayList<>();

        while(!deck.isEmpty()) { // deck leeren und in liste packen
            Card card = deck.pop();
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

    public Card getTopCard(){
        return deck.peek();
    }

    public void addCard(Card card){
        if(coverCard){
            deck.push(card);
            cardCount = cardCount + 1;
        }
    }
}
