package eoz.client.lobbyToTable;

public class Spieler {
    private int  id;
    private  String name;
    private int punkte;
    private  Card [] hand;
    private  boolean  hahnkarte;
    public Spieler(int id, String name, int punkte, Card[] hand, boolean hahnkarte) {
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

    public Card[] getHand() {
        return hand;
    }

    public void setHand(Card[] hand) {
        this.hand = hand;
    }


    public boolean isHahnkarte() {
        return hahnkarte;
    }

    public void setHahnkarte(boolean hahnkarte) {
        this.hahnkarte = hahnkarte;
    }
}



