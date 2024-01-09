package eoz.client.lobbyToTable;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tableController {

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
    public CardGridPane player4GridPane;
    public CardGridPane player6GridPane;
    public CardGridPane player3GridPane;
    public CardGridPane player5GridPane;
    public CardGridPane player1GridPane;
    public CardGridPane player2GridPane;

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

    Map<String, ImageView> imageViewMap = new HashMap<>();
    private ImageView draggedImage;
    private CardGridPane sourceGridPane;

    // create a list of player grid panes
    List<CardGridPane> gridPanes = new ArrayList<>();

    public void initialize(){
        // Setup drag and drop for each player grid pane
        setupGridPaneForDrop(player1GridPane);
        setupGridPaneForDrop(player2GridPane);
        setupGridPaneForDrop(player3GridPane);
        setupGridPaneForDrop(player4GridPane);
        setupGridPaneForDrop(player5GridPane);
        setupGridPaneForDrop(player6GridPane);

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

        Deck deck = new Deck();
        Card card = deck.pop();


        String cardName = card.getType() + card.getID();
        // Create a new card ImageView for distribution
        ImageView cardView = new ImageView(String.valueOf(card.getImage()));
        cardView.setId(cardName);
        imageViewMap.put(cardName, cardView);
        // Set a unique ID for the ImageView
        cardView.setFitHeight(50);
        cardView.setFitWidth(80);
        setupCardDragEvents(cardView);


        // Add the card to the grid, then update the position for the next card
        CardGridPane gridPane = gridPanes.get(gridPanesIdx);
        int[] nextCell = gridPane.getNextAvailableCell();

        // Find the cell in the grid where the drop occurred
        Integer rowIndex = nextCell[0];
        Integer colIndex = nextCell[1];
        gridPane.addCard(imageViewMap.get(cardName),rowIndex , colIndex);
        gridPanesIdx++;

        // Check if the card has been distributed to all panes at the current cell
        if (gridPanesIdx >= gridPanes.size()) {
            // Reset the distribution counter
            gridPanesIdx = 0;

            // Increment the index to cycle through the GridPanes
            gridPanesIdx = (gridPanesIdx ) % gridPanes.size();
        }
    }


    private void setupCardDragEvents(ImageView cardView) {
        cardView.setOnMousePressed(event -> {
            draggedImage = cardView;
            sourceGridPane = (CardGridPane) cardView.getParent();
        });

        cardView.setOnDragDetected(event -> {
            Dragboard db = cardView.startDragAndDrop(TransferMode.MOVE);

            // Set the drag view
            db.setDragView(cardView.snapshot(null, null));

            ClipboardContent content = new ClipboardContent();
            content.putString(cardView.getId());
            db.setContent(content);

            draggedImage = cardView;
            sourceGridPane = (CardGridPane) cardView.getParent();

            event.consume();
        });
    }


    private void setupGridPaneForDrop(CardGridPane gridPane) {
        gridPane.setOnDragOver(event -> {
            // Accept it only if it is not being dragged from the same node
            // and if it has a string data
            if (event.getGestureSource() != gridPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        gridPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                // Remove the image from the source grid pane
                sourceGridPane.removeCard(draggedImage);
                int[] nextCell = gridPane.getNextAvailableCell();

                // Find the cell in the grid where the drop occurred
                Integer rowIndex = nextCell[0];
                Integer colIndex = nextCell[1];

                // Add image to the target grid pane at the same cell
                // If dropping on an empty grid cell, you might need to calculate the cell indices based on drop location
                gridPane.addCard(draggedImage, rowIndex, colIndex);


                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
    }






    public void displayName(String username, double numOfPlayers){
        p1.setText(username);

        switch ((int) numOfPlayers){
            case 1:
                p2.setText("Bot 1");
                p3.setText("Bot 2");
                p4.setText("Bot 3");
                p5.setText("Bot 4");
                p6.setText("Bot 5");
                break;
            case 2:
                p3.setText("Bot 1");
                p4.setText("Bot 2");
                p5.setText("Bot 3");
                p6.setText("Bot 4");
                break;
            case 3:
                p4.setText("Bot 1");
                p5.setText("Bot 2");
                p6.setText("Bot 3");
                break;
            case 4:
                p5.setText("Bot 1");
                p6.setText("Bot 2");
                break;
            case 5:
                p6.setText("Bot 1");
                break;
        }
    }







}