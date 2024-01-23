package eoz.client.lobbyToTable;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;

import java.util.UUID;

public class Spieler extends ServerPlayer {

    private CardGridPane cardGridPane;

    private Label playerLabel;

    public Label getPlayerPointlabel() {
        return playerPointlabel;
    }

    public void setPlayerPointlabel(Label playerPointlabel) {
        this.playerPointlabel = playerPointlabel;
    }

    public GridPane getPlayerLabelGrid() {
        return playerLabelGrid;
    }

    public void setPlayerLabelGrid(CardGridPane playerLabelGrid) {
        this.playerLabelGrid = playerLabelGrid;
    }

    private Label playerPointlabel;

    private CardGridPane playerLabelGrid;

    public Label getPlayerLabel() {
        return playerLabel;
    }

    public void setPlayerLabel(Label playerLabel) {
        this.playerLabel = playerLabel;
        this.playerLabel.setText(getServerPlayerName());
    }

    public Spieler(UUID id, String playerName, boolean hahnKarte, CardGridPane cardGridPane, Label playerLabel) {
        super(id,playerName,hahnKarte); //Create player with following parameters
        this.cardGridPane = cardGridPane;
        setPlayerLabel(playerLabel);
    }

    public void setCardGridPane(CardGridPane cardGridPane) {
        this.cardGridPane = cardGridPane;
    }



    public CardGridPane getCardGridPane() {
        return cardGridPane;
    }


    @Override
    public void setHahnKarte(boolean hahnKarte) {
        super.setHahnKarte(hahnKarte);
        Platform.runLater(()->{
            if (hahnKarte){

                Card hahnServerCard = new Card( "Hahn",  0, false);
                // Create a new card ImageView for distribution
                ImageView cardView = new ImageView(hahnServerCard.getImage());
                cardView.setFitHeight(50);
                cardView.setFitWidth(80);
                // add card to grid pane
                this.playerLabelGrid.addRoosterCard(cardView);

            }else {
                this.playerLabelGrid.removeRoosterCard();
            }
        });

    }
    @Override
    public void setPunkte(int punkte) {
        super.setPunkte(punkte);
        this.playerPointlabel.setText("Pt: "+ punkte);
    }
    @Override
    public void increasePointsBy(int punkte){
        super.increasePointsBy(punkte);
        this.playerPointlabel.setText("Pt: "+getPunkte());
    }
    @Override
    public void decreasePointsBy(int punkte){
        super.decreasePointsBy(punkte);
        this.playerPointlabel.setText("Pt: "+getPunkte());
    }

    @Override
    public void raisePunkte(){
        super.raisePunkte();
        this.playerPointlabel.setText("Pt: "+getPunkte());
    }


    public void addCard(Card card, ServerCard servercard){
        //set the cell of the card
        int[] nextCell = this.cardGridPane.getNextAvailableCell();

        servercard.setCardCell(nextCell);
        int row = nextCell[0];
        int col = nextCell[1];

        // add the card to the hand
        super.addCard(servercard);

        // Create a new card ImageView for distribution
        ImageView cardView =  new ImageView(card.getImage());
        cardView.setUserData(servercard);
        cardView.setFitHeight(50);
        cardView.setFitWidth(80);

        // add card to grid pane
        this.cardGridPane.addCard(cardView,row,col);
        reorganizeGridPane();
    }

    public void removeCard (int[] cell ,UUID cardID, String cardType){
        // get the cell  key from the cardGridPane
        Integer key = cardGridPane.getCellKey(cell[0], cell[1]);
        // get the card
        Node card = cardGridPane.getCardInCell(key);

        if ( cardType == "Kuckuck") {
            //set the kuckuck card to null
            getCardHand().setKuckuck(null);
        } else {
            remove(cardID,cardType);
        }

        // remove the card
        cardGridPane.removeCard(card);
        reorganizeGridPane();

    }

    public  void reorganizeGridPane() {
        int StartRows = cardGridPane.getStartRow();
        int endRow = cardGridPane.getEndRow();
        int index = 0;

        for (int row = StartRows; row <= endRow; row++) {
            for (int col = 0; col <= 5; col++) {
                if (index >= cardGridPane.getChildren().size()) {
                    return; // Exit if all nodes have been reorganized
                }

                Node child = cardGridPane.getChildren().get(index);

                // Reorganize the child node to the new row and column
                GridPane.setRowIndex(child, row);
                GridPane.setColumnIndex(child, col);

                index++;
            }
        }
        cardGridPane.updateNextAvailableCell();
    }
}
