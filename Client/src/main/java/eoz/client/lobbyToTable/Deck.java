package eoz.client.lobbyToTable;

import javafx.scene.image.Image;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;


public class Deck {
    private Stack<Card> deck;


    public Deck( ) {
        deck = new Stack<>();
        int index = 1;

        for (int i = 1; i < 11; i++) {
            deck.push(new Card(index++, "Koerner", new ImageIcon(getClass().getResource("/cards/zwei.png").toString()), 2));
            deck.push(new Card(index++, "Koerner", new ImageIcon(getClass().getResource("/cards/dreik.png").toString()), 3));
            deck.push(new Card(index++, "Koerner", new ImageIcon(getClass().getResource("/cards/vier.png").toString()), 4));
            deck.push(new Card(index++, "Biokörner", new ImageIcon(getClass().getResource("/cards/zweib.png").toString()), 2));
            deck.push(new Card(index++, "Biokörner", new ImageIcon(getClass().getResource("/cards/drei.png").toString()), 3));
        }

        for (int j = 1; j < 9; j++) {
            deck.push(new Card(index++, "Biokörner", new ImageIcon(getClass().getResource("/cards/drei.png").toString()), 1));
        }

        for (int k = 1; k < 13; k++) {
            deck.push(new Card(index++, "Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0));
        }

        for (int l = 1; l < 5; l++) {
            deck.push(new Card(index++, "Kuckuck", new ImageIcon(getClass().getResource("/cards/kuckuck.png").toString()), 0));
        }

        Collections.shuffle(deck);
    }
    public Card pop() {
       return  deck.pop();
    }

}
