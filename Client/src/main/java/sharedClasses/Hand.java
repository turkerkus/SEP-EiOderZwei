package sharedClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {

    public int size(){
        return handCards.size();
    }
    private ArrayList<HandServerCard> handCards = new ArrayList<HandServerCard>();

    public void setHandCards(ArrayList<HandServerCard> handCards){ //Setter for the ServerCard per drag and Drop
        this.handCards = handCards;
    }

    public ArrayList<HandServerCard> getHandCards(){ //gives all the current cards in the hand
        return handCards;
    }

    public HandServerCard getCard(int pos){ //returns the card at a particular position "pos" on the hand.
        return handCards.get(pos);
    }

    public void addCard(ServerCard serverCard){
        HandServerCard hc = new HandServerCard(serverCard.getType(), serverCard.getValue(), false);
        handCards.add(hc);
    }

    public void removeCard (HandServerCard card){
        for(HandServerCard c: handCards){
            if(card.getValue() == c.getValue()){
                handCards.remove(c);
                break;
            }
        }
    }

    public boolean isEmpty() {
        return handCards.isEmpty();
    }
}