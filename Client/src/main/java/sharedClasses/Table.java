package sharedClasses;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;

public class Table implements Serializable {

    public List<ServerPlayer> spielerList;
    private int moveCount = 0;
    private Deck nachzieheDeck = new Deck(true); //This is the deck from which the players draw cards.
    private Deck ablageDeck = new Deck(false); //This is the deck where the players drop cards.
    public volatile int active;

    public Table(List<ServerPlayer> spieler) {
        this.spielerList = spieler;
    }

    public List<ServerPlayer> getSpielerList() {
        return spielerList;
    }

    public void nextMove() { // This method counts the number of moves made.
        moveCount += 1;
    }

    public Card karteZiehen() {
        return nachzieheDeck.ziehen();
    }
    public ServerPlayer getActiveSpieler(){
        return spielerList.get(active);
    }

    public void setActive(int activeSpieler){
        this.active = activeSpieler;
    }
    public void karteAblegen(Card card) { //This method drops the chosen card on the AblageDeck.
        ablageDeck.ablegen(card);
    }

    public int getAnzahlSpieler() { //diese Method gibt die Anzahl der aktuellen Spieler_old zurück.
        return spielerList.size();
    }

    public Card getTopCardOfDeck() {
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

    public void intiNachZiehDeck() {
        int index = 0;
        nachzieheDeck = new Deck(true);
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 8; j++) { //This creates 8 sets of 7 cards and add it to the deck = 56cards
                switch (i) {
                    case 0:
                        nachzieheDeck.addCard(new Card("Koerner", new ImageIcon(getClass().getResource("/cards/zwei.png").toString()), 2, true));
                        break;
                    case 1:
                        nachzieheDeck.addCard(new Card("Koerner", new ImageIcon(getClass().getResource("/cards/drei.png").toString()), 3, true));
                        break;
                    case 2:
                        nachzieheDeck.addCard(new Card("Koerner", new ImageIcon(getClass().getResource("/cards/vier.png").toString()), 4, true));
                        break;
                    case 3:
                        nachzieheDeck.addCard(new Card("Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0, true));
                        break;
                    case 4:
                        nachzieheDeck.addCard(new Card("BioKoerner", new ImageIcon(getClass().getResource("/cards/dreib.png").toString()), 3, true));
                        break;
                    case 5:
                        nachzieheDeck.addCard(new Card("BioKoerner", new ImageIcon(getClass().getResource("/cards/zweib.png").toString()), 2, true));
                        break;
                    case 6:
                        nachzieheDeck.addCard(new Card("BioKoerner", new ImageIcon(getClass().getResource("/cards/zweib.png").toString()), 1, true));
                        break;
                }
            }
            for (int n = 0; n < 2; n++) { //This creates 2 sets of 7 cards and add it to the deck = 14 cards
                switch (i) {
                    case 7:
                        nachzieheDeck.addCard(new Card("BioKoerner", new ImageIcon(getClass().getResource("/cards/dreib.png").toString()), 3, true));
                        break;
                    case 8:
                        nachzieheDeck.addCard(new Card("BioKoerner", new ImageIcon(getClass().getResource("/cards/zweib.png").toString()), 2, true));
                        break;
                    case 9:
                        nachzieheDeck.addCard(new Card("Koerner", new ImageIcon(getClass().getResource("/cards/zwei.png").toString()), 2, true));
                        break;
                    case 10:
                        nachzieheDeck.addCard(new Card("Koerner", new ImageIcon(getClass().getResource("/cards/drei.png").toString()), 3, true));
                        break;
                    case 11:
                        nachzieheDeck.addCard(new Card("Koerner", new ImageIcon(getClass().getResource("/cards/vier.png").toString()), 4, true));
                        break;
                    case 12:
                        nachzieheDeck.addCard(new Card("Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0, true));
                        break;
                    case 13:
                        nachzieheDeck.addCard(new Card("Kuckuck", new ImageIcon(getClass().getResource("/cards/kuckuck.png").toString()), 0, true));
                        break;
                }
                for (int r = 0; r < 2; r++) { //This creates 2 sets of 2 cards and add it to the deck = 4 cards
                    switch (i) {
                        case 14:
                            nachzieheDeck.addCard(new Card("Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0, true));
                            break;
                        case 15:
                            nachzieheDeck.addCard(new Card("Kuckuck", new ImageIcon(getClass().getResource("/cards/kuckuck.png").toString()), 0, true));
                            break;
                    }
                }
                for (int m = 0; m < 1; m++) {
                    if (i == 16) { //This adds the last remaining card to the deck. making it all 75
                        nachzieheDeck.addCard(new Card("Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0, true));
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
