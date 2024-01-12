package eoz.client.lobbyToTable;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import rmi.serverPlayer;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class Spieler1 extends serverPlayer {

    private boolean aussteigen = false;
    private boolean leftserver = false;
    private boolean cardDrawn = false;
    private int newCard;
    UUID  id;
    String playerName;
    int punkte;
    Hand cardHand;
    private int kornzahl;
    private List<Card1> hand;
    private  boolean  hahnKarte;
    private CardGridPane cardGridPane;

    private Label playerLabel;

    public Label getPlayerLabel() {
        return playerLabel;
    }

    public void setPlayerLabel(Label playerLabel) {
        this.playerLabel = playerLabel;
        this.playerLabel.setText(playerName);
    }

    public Spieler1(UUID id, String playerName, boolean hahnKarte, int kornzahl, CardGridPane cardGridPane, Label playerLabel) {
        super(id,playerName,hahnKarte,kornzahl); //Create player with following parameters
        this.id = id;
        this.playerName = playerName;
        this.hahnKarte = hahnKarte;
        this.kornzahl = kornzahl;
        this.cardGridPane = cardGridPane;
        this.playerLabel = playerLabel;
        this.playerLabel.setText(playerName);
    }

    public void setCardGridPane(CardGridPane cardGridPane) {
        this.cardGridPane = cardGridPane;
    }

    public CardGridPane getCardGridPane() {
        return cardGridPane;
    }

    public UUID getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }


    public int getPunkte() {return punkte;}

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public int getKornzahl() {return kornzahl;}

    public void setKornzahl(int kornzahl) {this.kornzahl += kornzahl;}

    public Hand getCardHand() {
        return cardHand;
    }

    public int getCardCount(){
        return cardHand.getHandCards().size();
    }

    public void add( Card1 card) {
        hand.add(card);
    }

    public void remove(Card1 card) { hand.remove(card); }

    public boolean isHahnKarte() {
        return hahnKarte;
    }

    public void setHahnKarte(boolean hahnKarte) {
        this.hahnKarte = hahnKarte;
        if (hahnKarte){

            Card1 hahnCard = new Card1( "Hahn", new ImageIcon(getClass().getResource("/cards/hahn.png").toString()), 0, false);
            // Create a new card ImageView for distribution
            ImageView cardView = new ImageView(String.valueOf(hahnCard.getImage()));
            cardView.setFitHeight(50);
            cardView.setFitWidth(80);
            // add card to grid pane
            this.cardGridPane.addRoosterCard(cardView);

        }else {
            this.cardGridPane.removeRoosterCard();
        }

    }

    public void einsteigen(){
        aussteigen = false;
    }

    public void aussteigen(){
        System.out.println("Aussteigen: " + aussteigen );
        aussteigen = true;
        System.out.println("in Game: " );
    }

    public boolean inGame(){
        return !aussteigen;
    }

    public boolean getLeftServer() { //Checks whether the player has left the game, true when play has left.
        return leftserver;
    }

    public void setLeftServer(boolean bool) {
        leftserver = bool;
    }

    public int getNewCard(){
        return newCard;
    }
    public boolean getCardDrawn(){
        return cardDrawn;
    }

    public void setCardDrawn(boolean cardDrawn) {
        this.cardDrawn = cardDrawn;
    }

    public void setNewCard(int newCard) {
        this.newCard = newCard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(playerName, ((Spieler1) obj).getPlayerName())
                && ((Spieler1)obj).getCardHand().equals(cardHand);
    }

    @Override
    public String toString() {
        return "(" + getPlayerName()+ " "+getId()+ ")";
    }
}

