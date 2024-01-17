package eoz.client.lobbyToTable;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sharedClasses.ServerPlayer;

import java.util.UUID;

public class Spieler extends ServerPlayer {

    private CardGridPane cardGridPane;

    private Label playerLabel;

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
                this.cardGridPane.addRoosterCard(cardView);

            }else {
                this.cardGridPane.removeRoosterCard();
            }
        });

    }
}
