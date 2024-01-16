package sharedClasses;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;

public class ServerTable implements Serializable {

    public List<ServerPlayer> spielerList;
    private int moveCount = 0;
    private Deck nachzieheDeck = new Deck(true); //This is the deck from which the players draw cards.
    private Deck ablageDeck = new Deck(false); //This is the deck where the players drop cards.


    public volatile int active;

    public ServerTable(List<ServerPlayer> spieler) {
        this.spielerList = spieler;
    }

    public List<ServerPlayer> getSpielerList() {
        return spielerList;
    }

    public void nextMove() { // This method counts the number of moves made.
        moveCount += 1;
    }

    public ServerCard karteZiehen() {
        return nachzieheDeck.ziehen();
    }
    public ServerPlayer getActiveSpieler(){
        return spielerList.get(active);
    }

    public void setActive(int activeSpieler){
        this.active = activeSpieler;
    }
    public void karteAblegen(ServerCard serverCard) { //This method drops the chosen serverCard on the AblageDeck.
        ablageDeck.ablegen(serverCard);
    }

    public int getAnzahlSpieler() { //diese Method gibt die Anzahl der aktuellen Spieler_old zurück.
        return spielerList.size();
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
        do {
            active = (active + 1) % getAnzahlSpieler();
        } while (!spielerList.get(active).inGame());

        if ((spielerList.get(active) != null))
            System.out.println(spielerList.get(active).getServerPlayerName() + " is next");
        return spielerList.get(active);
    }
    public int getActive() {
        return active;
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

}
