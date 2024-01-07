package eoz.client.lobbyToTable;

import java.util.List;

public class Spieler {
    private int  id;
    private  String name;
    private int punkte;

    private int kornzahl;
    private List<Card> hand;
    private  boolean  hahnkarte;
    public Spieler(int id, String name, int punkte, int kornzahl, List<Card> hand, boolean hahnkarte) {
        this.id = id;
        this.name = name;
        this.punkte = punkte;
        this.kornzahl = kornzahl;
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

    public int getPunkte() {return punkte;}

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public int getKornzahl() {return kornzahl;}

    public void setKornzahl(int asdf) {this.kornzahl = kornzahl;}

    public List<Card> getHand() {
        return hand;
    }
    public void  setHand(List<Card> hand){this.hand=hand;}

    public void add( Card card) {
         hand.add(card);
    }

    public void remove(Card card) { hand.remove(card); }

    public boolean isHahnkarte() {
        return hahnkarte;
    }

    public void setHahnkarte(boolean hahnkarte) {
        this.hahnkarte = hahnkarte;
    }
}



