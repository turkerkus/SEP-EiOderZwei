package eoz.client.lobbyToTable;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.*;

public class newTableController {

    @FXML
    public Label p1;
    @FXML
    public Label p4;
    @FXML
    public Label p2;
    @FXML
    public Label p5;
    @FXML
    public Label p6;
    @FXML
    public Label p3;

    @FXML
    public AnchorPane anchorPane1;
    @FXML
    public AnchorPane anchorPane2;
    @FXML
    public SplitPane splitPane;
    @FXML
    public SplitPane splitPane2;
    @FXML
    public AnchorPane anchorPane3;
    @FXML
    public AnchorPane anchorPane4;
    @FXML
    public ImageView backgroundView;
    @FXML
    public GridPane tableGridPane1;
    @FXML
    public ImageView recTable;
    @FXML
    public GridPane buttonGridPane;
    public GridPane recTableGridPane;
    public GridPane mainCardsGridPane;
    public ImageView mainCard;
    public GridPane player4GridPane;
    public GridPane player6GridPane;
    public GridPane player3GridPane;
    public GridPane player5GridPane;
    public GridPane player1GridPane;
    public GridPane player2GridPane;

    @FXML
    public Button drawCardButton;
    @FXML
    public Button getEggsButton;
    @FXML
    public Button takeRoosterButton;
    @FXML
    public Button startButton;
    @FXML
    public Button leaveLobbyButton;

    private int currentRow = 1; // Start from the first row
    private int currentCol = 0; // Start from the first column
    Map<String, ImageView> imageViewMap = new HashMap<>();
    private int nameIncrement = 1;

    // create a list of player grid panes
    List<GridPane> gridPanes = new ArrayList<>();

    public void initialize(){
        gridPanes.add(player1GridPane);
        gridPanes.add(player2GridPane);
        gridPanes.add(player3GridPane);
        gridPanes.add(player4GridPane);
        gridPanes.add(player5GridPane);
        gridPanes.add(player6GridPane);
    }

    Integer gridPanesIdx = 0;






    // Assume this is called when the main card is clicked to start distribution
    public void distributeCards() {
        String cardName = "card " + nameIncrement;
        // Create a new card ImageView for distribution
        imageViewMap.put(cardName, new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/cards/kuckuck.png")).toString())));
        imageViewMap.get(cardName).setFitHeight(50);
        imageViewMap.get(cardName).setFitWidth(80);

        // Add the card to the grid, then update the position for the next card
        gridPanes.get(gridPanesIdx).add(imageViewMap.get(cardName), currentCol, currentRow);
        gridPanesIdx++;

        // Check if the card has been distributed to all panes at the current cell
        if (gridPanesIdx >= gridPanes.size()) {
            // Reset the distribution counter
            gridPanesIdx = 0;

            // Update the position for the next card using your existing logic
            incrementPosition();
            nameIncrement++;

            // Increment the index to cycle through the GridPanes
            gridPanesIdx = (gridPanesIdx ) % gridPanes.size();
        }
    }

    private void incrementPosition() {
        // Move to the next column, wrap to the next row if at the end
        currentCol++;
        if (currentCol > 4) { // Assuming 5 columns indexed from 0 to 4
            currentCol = 0;
            currentRow++;
        }

        // Wrap back to the first row if at the end
        if (currentRow > 4) { // Assuming 5 rows indexed from 0 to 4
            currentRow = 1; // Reset to the start position for rows
        }
    }










}