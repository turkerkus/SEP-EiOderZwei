package eoz.client.lobbyToTable;

import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {

    public int size(){
        return handCards.size();
    }
    private ArrayList<HandCard> handCards = new ArrayList<HandCard>();

    public void setHandCards(ArrayList<HandCard> handCards){ //Setter for the Card per drag and Drop
        this.handCards = handCards;
    }

    public ArrayList<HandCard> getHandCards(){ //gives all the current cards in the hand
        return handCards;
    }

    public HandCard getCard(int pos){ //returns the card at a particular position "pos" on the hand.
        return handCards.get(pos);
    }

    public void addCard(Card1 card){
        HandCard hc = new HandCard(card.getType(), card.getImage(), card.getValue(), false);
        handCards.add(hc);
    }

    public void removeCard (HandCard card){
        for(HandCard c: handCards){
            if(card.getValue() == c.getValue()){
                handCards.remove(c);
                break;
            }
        }
    }
}
