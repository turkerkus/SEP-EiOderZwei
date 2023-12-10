package eoz.client.lobbyToTable;

public class Spieler {
    private int  id;
    private  String name;
    private int punkte;
    private  Card [] hand;
    private  boolean  turn;
    public Spieler(int id, String name, int punkte, Card[] hand, boolean turn) {
        this.id = id;
        this.name = name;
        this.punkte = punkte;
        this.hand = hand;
        this.turn = turn;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
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

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}



