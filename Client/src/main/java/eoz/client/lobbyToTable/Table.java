package eoz.client.lobbyToTable;

import javax.swing.*;
import java.io.Serializable;

public class Table implements Serializable {

    public Spieler1[] spielerList;
    private int moveCount = 0;
    private Deck1 nachzieheDeck = new Deck1(true); //This is the deck from which the players draw cards.
    private Deck1 ablageDeck = new Deck1(false); //This is the deck where the players drop cards.
    public volatile int active;

    public Table(Spieler1[] spieler) {
        this.spielerList = spieler;
    }

    public Spieler1[] getSpielerList() {
        return spielerList;
    }

    public void nextMove() { // This method counts the number of moves made.
        moveCount += 1;
    }

    public Card1 karteZiehen() {
        return nachzieheDeck.ziehen();
    }
    public Spieler1 getActiveSpieler(){
        return spielerList[active];
    }

    public void setActive(int activeSpieler){
        this.active = activeSpieler;
    }
    public void karteAblegen(Card1 card) { //This method drops the chosen card on the AblageDeck.
        ablageDeck.ablegen(card);
    }

    public int getAnzahlSpieler() { //diese Method gibt die Anzahl der aktuellen Spieler zurück.
        return spielerList.length;
    }

    public Card1 getTopCardOfDeck() {
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

    public Spieler1 nextSpieler() {
        do {
            active = (active + 1) % getAnzahlSpieler();
        } while (!spielerList[active].inGame());

        if ((spielerList[active] != null))
            System.out.println(spielerList[active].getPlayerName() + " is next");
        return spielerList[active];
    }

    public void intiNachZiehDeck() {
        int index = 0;
        nachzieheDeck = new Deck1(true);
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 8; j++) { //This creates 8 sets of 7 cards and add it to the deck = 56cards
                switch (i) {
                    case 0:
                        nachzieheDeck.addCard(new Card1("Koerner", new ImageIcon(getClass().getResource("/cards/zwei.png").toString()), 2, true));
                        break;
                    case 1:
                        nachzieheDeck.addCard(new Card1("Koerner", new ImageIcon(getClass().getResource("/cards/drei.png").toString()), 3, true));
                        break;
                    case 2:
                        nachzieheDeck.addCard(new Card1("Koerner", new ImageIcon(getClass().getResource("/cards/vier.png").toString()), 4, true));
                        break;
                    case 3:
                        nachzieheDeck.addCard(new Card1("Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0, true));
                        break;
                    case 4:
                        nachzieheDeck.addCard(new Card1("BioKoerner", new ImageIcon(getClass().getResource("/cards/dreib.png").toString()), 3, true));
                        break;
                    case 5:
                        nachzieheDeck.addCard(new Card1("BioKoerner", new ImageIcon(getClass().getResource("/cards/zweib.png").toString()), 2, true));
                        break;
                    case 6:
                        nachzieheDeck.addCard(new Card1("BioKoerner", new ImageIcon(getClass().getResource("/cards/zweib.png").toString()), 1, true));
                        break;
                }
            }
            for (int n = 0; n < 2; n++) { //This creates 2 sets of 7 cards and add it to the deck = 14 cards
                switch (i) {
                    case 7:
                        nachzieheDeck.addCard(new Card1("BioKoerner", new ImageIcon(getClass().getResource("/cards/dreib.png").toString()), 3, true));
                        break;
                    case 8:
                        nachzieheDeck.addCard(new Card1("BioKoerner", new ImageIcon(getClass().getResource("/cards/zweib.png").toString()), 2, true));
                        break;
                    case 9:
                        nachzieheDeck.addCard(new Card1("Koerner", new ImageIcon(getClass().getResource("/cards/zwei.png").toString()), 2, true));
                        break;
                    case 10:
                        nachzieheDeck.addCard(new Card1("Koerner", new ImageIcon(getClass().getResource("/cards/drei.png").toString()), 3, true));
                        break;
                    case 11:
                        nachzieheDeck.addCard(new Card1("Koerner", new ImageIcon(getClass().getResource("/cards/vier.png").toString()), 4, true));
                        break;
                    case 12:
                        nachzieheDeck.addCard(new Card1("Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0, true));
                        break;
                    case 13:
                        nachzieheDeck.addCard(new Card1("Kuckuck", new ImageIcon(getClass().getResource("/cards/kuckuck.png").toString()), 0, true));
                        break;
                }
                for (int r = 0; r < 2; r++) { //This creates 2 sets of 2 cards and add it to the deck = 4 cards
                    switch (i) {
                        case 14:
                            nachzieheDeck.addCard(new Card1("Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0, true));
                            break;
                        case 15:
                            nachzieheDeck.addCard(new Card1("Kuckuck", new ImageIcon(getClass().getResource("/cards/kuckuck.png").toString()), 0, true));
                            break;
                    }
                }
                for (int m = 0; m < 1; m++) {
                    if (i == 16) { //This adds the last remaining card to the deck. making it all 75
                        nachzieheDeck.addCard(new Card1("Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0, true));
                    }
                }
            }
        }
    }

    public void shuffleDeck() {
        nachzieheDeck.mischen();
    }

}
