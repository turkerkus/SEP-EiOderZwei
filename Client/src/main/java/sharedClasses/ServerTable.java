package sharedClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerTable implements Serializable {

    private Map<UUID, ServerPlayer> players;

    public UUID getPlayerId(Integer index) {
        return playerIdList.get(index);
    }
    public ServerPlayer getPlayer(UUID playerID){
        return players.get(playerID);
    }

    private List<UUID> playerIdList;
    private int moveCount = 0;
    private Deck nachzieheDeck = new Deck(true); //This is the deck from which the players draw cards.
    private Deck ablageDeck = new Deck(false); //This is the deck where the players drop cards.

    public ServerCard getDrawnCard() {
        return drawnCard;
    }


    ServerCard drawnCard ;

    private ServerCard stolenCard;

    private UUID target;

    public UUID getAlteSpielerMitHahnKarte() {
        return alteSpielerMitHahnKarte;
    }

    UUID alteSpielerMitHahnKarte;
    private volatile int active;

    public UUID getSpielerMitHahnKarte() {
        return spielerMitHahnKarte;
    }

    public void setSpielerMitHahnKarte(UUID neuSpielerMitHahnKarteID) {
        // set the old player with rooster card value to false
        alteSpielerMitHahnKarte = this.spielerMitHahnKarte;
        if(alteSpielerMitHahnKarte != null){
            players.get(alteSpielerMitHahnKarte).setHahnKarte(false);
        }


        // set the new player with the rooster card
        this.spielerMitHahnKarte = neuSpielerMitHahnKarteID;
        players.get(neuSpielerMitHahnKarteID).setHahnKarte(true);
    }

    public void swapPlayerWithBot(UUID playerId, String botName){
        players.get(playerId).setBot(true);
        players.get(playerId).setServerPlayerName(botName);

    }

    private UUID spielerMitHahnKarte;

    public ServerTable() {
        players = new ConcurrentHashMap<>();
        playerIdList = new ArrayList<>();
    }

    public Map<UUID, ServerPlayer> getPlayers() {
        return players;
    }
    public void addplayer(UUID spielerId, ServerPlayer spieler){
        players.put(spielerId, spieler);
        playerIdList.add(spielerId);
    }

    public void nextMove() { // This method counts the number of moves made.
        moveCount += 1;
    }

    public void karteZiehen(UUID clientId) {
        drawnCard =  nachzieheDeck.ziehen();
        players.get(clientId).addCard(drawnCard);
    }
    public ServerPlayer getActiveSpieler(){
        UUID playerId = playerIdList.get(active);
        return players.get(playerId);
    }
    public UUID getActiveSpielerID(){
        return playerIdList.get(active);
    }

    public void setActive(int activeSpieler){
        this.active = activeSpieler;
    }
    public void karteAblegen(UUID clientId, ServerCard serverCard) { //This method drops the chosen serverCard on the AblageDeck.
        players.get(clientId).remove(serverCard.getServeCardID(), serverCard.getType());
        ablageDeck.ablegen(serverCard);
    }

    public int getAnzahlSpieler() { //diese Method gibt die Anzahl der aktuellen Spieler_old zurück.
        return players.size();
    }

    public ServerCard getTopCardOfDeck() {
        return ablageDeck.getTopCard();
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int getNachzieheDeckSize() {
        return nachzieheDeck.getCardCount();
    }

    public int getAblageDeckSize() {
        return ablageDeck.getCardCount();
    }

    public ServerPlayer nextSpieler() {
        active = (active + 1) % getAnzahlSpieler();
        UUID playerId = playerIdList.get(active);
        if ((players.get(playerId) != null))
            System.out.println(players.get(playerId).getServerPlayerName() + " is next");
        return players.get(playerId);
    }
    public int getActive() {
        return active;
    }

    public void setStolenCard(ServerCard stolenCard) {
        this.stolenCard = stolenCard;
    }

    public ServerCard getStolenCard() {
        return stolenCard;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public void intiNachZiehDeck() {
        int index = 0;
        nachzieheDeck = new Deck(true);
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 8; j++) { //This creates 8 sets of 7 cards and add it to the deck = 56cards
                switch (i) {
                    case 0:
                        nachzieheDeck.addCard(new ServerCard("Koerner",  2, true));
                        break;
                    case 1:
                        nachzieheDeck.addCard(new ServerCard("Koerner",  3, true));
                        break;
                    case 2:
                        nachzieheDeck.addCard(new ServerCard("Koerner",  4, true));
                        break;
                    case 3:
                        nachzieheDeck.addCard(new ServerCard("Fuchs",  0, true));
                        break;
                    case 4:
                        nachzieheDeck.addCard(new ServerCard("BioKoerner",  3, true));
                        break;
                    case 5:
                        nachzieheDeck.addCard(new ServerCard("BioKoerner", 2, true));
                        break;
                    case 6:
                        nachzieheDeck.addCard(new ServerCard("BioKoerner",  1, true));
                        break;
                }
            }
            for (int n = 0; n < 2; n++) { //This creates 2 sets of 7 cards and add it to the deck = 14 cards
                switch (i) {
                    case 7:
                        nachzieheDeck.addCard(new ServerCard("BioKoerner", 3, true));
                        break;
                    case 8:
                        nachzieheDeck.addCard(new ServerCard("BioKoerner",  2, true));
                        break;
                    case 9:
                        nachzieheDeck.addCard(new ServerCard("Koerner",  2, true));
                        break;
                    case 10:
                        nachzieheDeck.addCard(new ServerCard("Koerner",  3, true));
                        break;
                    case 11:
                        nachzieheDeck.addCard(new ServerCard("Koerner",  4, true));
                        break;
                    case 12:
                        nachzieheDeck.addCard(new ServerCard("Fuchs",  0, true));
                        break;
                    case 13:
                        nachzieheDeck.addCard(new ServerCard("Kuckuck",  0, true));
                        break;
                }
                for (int r = 0; r < 2; r++) { //This creates 2 sets of 2 cards and add it to the deck = 4 cards
                    switch (i) {
                        case 14:
                            nachzieheDeck.addCard(new ServerCard("Fuchs",  0, true));
                            break;
                        case 15:
                            nachzieheDeck.addCard(new ServerCard("Kuckuck",  0, true));
                            break;
                    }
                }
                for (int m = 0; m < 1; m++) {
                    if (i == 16) { //This adds the last remaining card to the deck. making it all 75
                        nachzieheDeck.addCard(new ServerCard("Fuchs",  0, true));
                        break;
                    }
                }
            }
        }
        nachzieheDeck.mischen();
    }

    public Deck getNachzieheDeck() {
        return nachzieheDeck;
    }

    public ArrayList<ServerCard> getDiscardedSelectedCards() {
        return discardedSelectedCards;
    }

    public void setDiscardedSelectedCards(ArrayList<ServerCard> discardedSelectedCards) {
        this.discardedSelectedCards = discardedSelectedCards;
    }

    public Integer getEggPoints() {
        return eggPoints;
    }

    public void setEggPoints(Integer eggPoints) {
        this.eggPoints = eggPoints;
    }

    private ArrayList<ServerCard> discardedSelectedCards ;
    private Integer eggPoints;
    public ServerCard getDiscarded() {
        return ablageDeck.getTopCard();
    }

}
