package sharedClasses;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Hand implements Serializable {



    private Map<UUID, ServerCard> bioCornCards = new ConcurrentHashMap<>();
    private Map<UUID, ServerCard> foxCards = new ConcurrentHashMap<>();
    private Map<UUID, ServerCard> cornCards = new ConcurrentHashMap<>();
    private Map<UUID, ServerCard> kuckuckCards = new ConcurrentHashMap<>();

    public void setKuckuck(Map<UUID, ServerCard> kuckuckCards ) {
        this.kuckuckCards = kuckuckCards;
    }

    public Map<UUID, ServerCard> getKuckuckCards() {
        return kuckuckCards;
    }






    public int size(){
        return bioCornCards.size() + foxCards.size() + cornCards.size();
    }

    public Map<UUID, ServerCard> getBioCornCards() {
        return bioCornCards;
    }

    public void setBioCornCards(Map<UUID, ServerCard> bioCornCards) {
        this.bioCornCards = bioCornCards;
    }

    public Map<UUID, ServerCard> getFoxCards() {
        return foxCards;
    }

    public void setFoxCards(Map<UUID, ServerCard> foxCards) {
        this.foxCards = foxCards;
    }

    public Map<UUID, ServerCard> getCornCards() {
        return cornCards;
    }

    public void setCornCards(Map<UUID, ServerCard> cornCards) {
        this.cornCards = cornCards;
    }


    public ServerCard getCard(UUID cardID, String cardType){ //returns the card at a particular position "pos" on the hand.
        return switch (cardType) {
            case "Koerner" -> cornCards.get(cardID);
            case "BioKoerner" -> bioCornCards.get(cardID);
            case "Fuchs" -> foxCards.get(cardID);
            case "Kuckuck" -> kuckuckCards.get(cardID);
            default -> null;
        };
    }



    public void addCardToHand(ServerCard serverCard){
        switch (serverCard.getType()){
            case "Koerner":
                //this.numOfCornCard += serverCard.getValue();
                 cornCards.put(serverCard.getServeCardID(),serverCard);
                 break;
            case "BioKoerner":
                //this.numOfBioCornCard += serverCard.getValue();
                 bioCornCards.put(serverCard.getServeCardID(),serverCard);
                 break;
            case "Fuchs":
                 foxCards.put(serverCard.getServeCardID(),serverCard);
                 break;
            default:
                kuckuckCards.put(serverCard.getServeCardID(),serverCard);
                break;
        }
    }

    public void removeCard (UUID cardID, String cardType ){
        switch (cardType){
            case "Koerner":
                ServerCard corn = cornCards.get(cardID);
                //this.numOfCornCard -= corn.getValue();
                cornCards.remove(cardID);
                break;
            case "BioKoerner":
                ServerCard bio = bioCornCards.get(cardID);
               // this.numOfBioCornCard -= bio.getValue();
                bioCornCards.remove(cardID);
                break;
            case "Fuchs":
                foxCards.remove(cardID);
                break;
            default:
                kuckuckCards.remove(cardID);
                break;
        }
    }

    public boolean isEmpty() {
        return foxCards.isEmpty() && bioCornCards.isEmpty() && cornCards.isEmpty();
    }
}