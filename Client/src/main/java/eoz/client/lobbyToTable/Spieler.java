package eoz.client.lobbyToTable;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;

import java.util.UUID;

public class Spieler extends ServerPlayer {

    private CardGridPane cardGridPane;

    private Label playerLabel;

    public Label getPlayerPoint() {
        return playerPoint;
    }

    public void setPlayerPoint(Label playerPoint) {
        this.playerPoint = playerPoint;
    }

    public GridPane getPlayerLabelGrid() {
        return playerLabelGrid;
    }

    public void setPlayerLabelGrid(GridPane playerLabelGrid) {
        this.playerLabelGrid = playerLabelGrid;
    }

    private Label playerPoint;

    private GridPane playerLabelGrid;

    public Label getPlayerLabel() {
        return playerLabel;
    }

    public void setPlayerLabel(Label playerLabel) {
        this.playerLabel = playerLabel;
        this.playerLabel.setText(getServerPlayerName());
    }

    public Spieler(UUID id, String playerName, boolean hahnKarte, int kornzahl, CardGridPane cardGridPane, Label playerLabel) {
        super(id,playerName,hahnKarte,kornzahl); //Create player with following parameters
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
                ImageView cardView = new ImageView(String.valueOf(hahnServerCard.getImage()));
                cardView.setFitHeight(50);
                cardView.setFitWidth(80);
                // add card to grid pane
                this.playerLabelGrid.add(cardView,1,0);

            }else {
                this.playerLabelGrid.getChildren().remove(1,0);
            }
        });

    }
    @Override
    public void setPunkte(int punkte) {
        super.setPunkte(punkte);
    }


    public void addCard(Card card, ServerCard servercard){
        super.add(servercard);
        // Create a new card ImageView for distribution
        ImageView cardView = new ImageView(String.valueOf(card.getImage()));
        //cardView.setPreserveRatio(true);
        cardView.setFitHeight(50);
        cardView.setFitWidth(80);
        // add card to grid pane
        int[] nextCell = this.cardGridPane.getNextAvailableCell();
        int row = nextCell[0];
        int col = nextCell[1];
        this.cardGridPane.addCard(cardView,row,col);
    }
}
