package eoz.client.lobbyToTable;

import javafx.scene.image.Image;

import javax.swing.*;


public class Deck {
    private Card[] deck;


    public Deck( ) {
        Card [] deck =new  Card[76];
        int index=1;

        for (int i=1; i<11; i++){
            deck[index++] = new Card(index, "Koerner", new ImageIcon(getClass().getResource("/cards/zwei.png").toString()), 2);
            deck[index++] = new Card(index, "Koerner", new ImageIcon(getClass().getResource("/cards/dreik.png").toString()), 3);
            deck[index++] = new Card(index, "Koerner", new ImageIcon(getClass().getResource("/cards/vier.png").toString()), 4);
            deck[index++] = new Card(index, "Biokörner", new ImageIcon(getClass().getResource("/cards/zweib.png").toString()), 2);
            deck[index++] = new Card(index, "Biokörner", new ImageIcon(getClass().getResource("/cards/drei.png").toString()), 3);
        }
        for (int j=1 ;j< 9;j++){
            deck [index++] = new Card(index, "Biokörner", new ImageIcon(getClass().getResource("/cards/drei.png").toString()), 1);
        }
        for (int k=1 ;k< 13;k++){
            deck [index++] =new Card( index, "Fuchs", new ImageIcon(getClass().getResource("/cards/fuchs.png").toString()), 0);
        }
        for (int l=1 ;l< 5;l++){
            deck [index++] =new Card(index, "Kuckuck", new ImageIcon(getClass().getResource("/cards/kuckuck.png").toString()), 0);
        }
    }
    public Card getCardAtIndex(int index) {
        if (index >= 0 && index < deck.length) {
            return deck[index];
        } else {
            throw new IndexOutOfBoundsException("Ungültiger Index");
        }
    }



}
