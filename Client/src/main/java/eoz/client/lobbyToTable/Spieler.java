package eoz.client.lobbyToTable;

import java.util.Stack;

public class Spieler {
    private int  id;
    private  String name;
    private int punkte;
    private Stack<Card> hand;
    private  boolean  hahnkarte;
    public Spieler(int id, String name, int punkte, Stack<Card> hand, boolean hahnkarte) {
        this.id = id;
        this.name = name;
        this.punkte = punkte;
        this.hand = hand;
        this.hahnkarte = hahnkarte;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public Stack<Card> getHand() {
        return hand;
    }

    public void push( Card card) {
         hand.push(card);
    }


    public boolean isHahnkarte() {
        return hahnkarte;
    }

    public void setHahnkarte(boolean hahnkarte) {
        this.hahnkarte = hahnkarte;
    }
}



