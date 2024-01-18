package eoz.client.lobbyToTable;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sharedClasses.ServerCard;
import sharedClasses.Deck;
import sharedClasses.ServerTable;
import sharedClasses.ServerPlayer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TableController implements Serializable {

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
    public ImageView nachzieheDeck;
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
    public ImageView ablageDeck;

    Map<String, ImageView> imageViewMap = new HashMap<>();
    private ImageView draggedImage;
    private CardGridPane sourceGridPane;


    // create a list of player grid panes
    List<CardGridPane> gridPanesList = new ArrayList<>();
    List<Label> labelList = new ArrayList<>();
    private ServerTable serverTable;
    private Map<UUID, Spieler> players = new ConcurrentHashMap<>();
    private Map<UUID, ServerPlayer> serverPlayers;


    private Client client;

    private UUID currentPlayerID;
    @FXML
    private Label timerLabel; // This is the Label that displays the timer



    private Integer timeLeft; // Time left in seconds

    private Boolean isGameStarted = false;





    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCurrentPlayerID(UUID playerID) {
        this.currentPlayerID = playerID;
    }


    public void assignCardGridPane() throws RemoteException {
        if (this.client == null) return;

        try {
            // Get player list from the client
            serverPlayers = this.client.getPlayers();
            convertAndSetPlayers(serverPlayers);
        } catch (RemoteException e) {
            e.printStackTrace();  // Better to print stack trace for debugging
        }

        //set the labels to ""
        Label[] labels = new Label[]{p1, p2, p3, p4, p5, p6};
        // Set all labels to empty initially
        for (Label label : labels) {
            label.setText("");
        }


        int numPlayers = players.size();

        // Arrays for labels and grid panes based on the number of players
        Label[][] labelCases = {
                {},  // Case 0 (unused)
                {},  // Case 1 (unused)
                {p5},  // Case 2
                {p4, p6},  // Case 3
                {p4, p5, p6},  // Case 4
                {p3, p4, p5, p6},  // Case 5
                {p2, p3, p4, p5, p6}  // Case 6
        };

        CardGridPane[][] gridPaneCases = {
                {},  // Case 0 (unused)
                {},  // Case 1 (unused)
                {player5GridPane},  // Case 2
                {player4GridPane, player6GridPane},  // Case 3
                {player4GridPane, player5GridPane, player6GridPane},  // Case 4
                {player3GridPane, player4GridPane, player5GridPane, player6GridPane},  // Case 5
                {player2GridPane, player3GridPane, player4GridPane, player5GridPane, player6GridPane}  // Case 6
        };

        //add the p1 label and player1GridPane to the list
        labelList.add(p1);
        gridPanesList.add(player1GridPane);
        // Setup drag and drop for each player grid pane
        setupGridPaneForDrop(player1GridPane);

        // Iterate over players and assign labels and grid panes
        int nonHostPlayerIndex = 0;
        for (Map.Entry<UUID, Spieler> entry : players.entrySet()) {
            Spieler player = entry.getValue();
            if (player.getServerPlayerName().equals(this.client.getClientName())) {

                // Assign the host player to p1 and player1GridPane
                player.setPlayerLabel(p1);
                player.setCardGridPane(player1GridPane);
            } else {
                // Handle non-host players
                if (numPlayers >= 2 && numPlayers <= 6) {
                    player.setPlayerLabel(labelCases[numPlayers][nonHostPlayerIndex]);
                    player.setCardGridPane(gridPaneCases[numPlayers][nonHostPlayerIndex]);

                    //add the Labels and GridPane to their respective list to be able to distribute the cards respectively
                    labelList.add(labelCases[numPlayers][nonHostPlayerIndex]);
                    gridPanesList.add(gridPaneCases[numPlayers][nonHostPlayerIndex]);
                    // Setup drag and drop for each player grid pane
                    setupGridPaneForDrop(gridPaneCases[numPlayers][nonHostPlayerIndex]);
                    nonHostPlayerIndex++;
                }
            }
        }
    }





    public void convertAndSetPlayers(Map<UUID, ServerPlayer> players) {
        // Assuming you have a way to get JavaFX components for each player
        for (Map.Entry<UUID, ServerPlayer> entry : players.entrySet()) {
            UUID spielerID = entry.getKey();
            ServerPlayer player = entry.getValue();
            CardGridPane dummyPane = new CardGridPane();
            Label dummyLabel = new Label();

            Spieler spieler = new Spieler(
                    player.getServerPlayerId(),
                    player.getServerPlayerName(),
                    player.hatHahnKarte(),
                    player.getKornzahl(),
                    dummyPane,
                    dummyLabel
            );

            // Add to your spielerList or handle as needed
            this.players.put(spielerID,spieler);
        }
    }


    public void startGame() {

        try {
            //this.client.setTableController(this);
            this.client.startGame();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Updates the timer label with the remaining time.
     */
    public void updateTimerLabel(Integer timeLeft) {
        Platform.runLater(() -> {
            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;
            timerLabel.setText(String.format("Timer: %02d:%02d", minutes, seconds));
        });
    }



    /**
     * Updates the UI to highlight the current player's label with a glowing effect.
     * Also, stops the glowing effect for all other player labels.
     *
     */
    public void startGameUiUpdate() {
        Spieler currentPlayer = players.get(this.currentPlayerID);
        Label currentPlayerLabel = currentPlayer.getPlayerLabel();
        System.out.println("this is the current player"+currentPlayer.toString());
        if (currentPlayerLabel != null) {
            // Create a glow effect
            System.out.println("this is the current player is not null");
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.GOLD);
            dropShadow.setRadius(20);
            dropShadow.setSpread(0.5);

            // Create an animation to pulse the drop shadow effect
            Timeline shadowAnimation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(dropShadow.radiusProperty(), 10)),
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(dropShadow.radiusProperty(), 20)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(dropShadow.radiusProperty(), 10))
            );
            shadowAnimation.setCycleCount(Timeline.INDEFINITE);
            shadowAnimation.setAutoReverse(true);
            shadowAnimation.play();

            // Apply the effect and animation to the label
            currentPlayerLabel.setEffect(dropShadow);
            currentPlayerLabel.setUserData(shadowAnimation);

            // Change background color in animation
            ColorAnimation(currentPlayerLabel, Duration.seconds(1));
        }

        // Ensure other player labels are not glowing
        for (Label label : getAllPlayerLabels()) {
            if (label != currentPlayerLabel) {
                stopLabelGlow(label);
            }
        }
    }

    private void ColorAnimation(Label label, Duration duration) {
        FillTransition fillTransition = new FillTransition(duration, label.getShape());
        fillTransition.setFromValue(Color.YELLOWGREEN);
        fillTransition.setToValue(Color.YELLOW);
        fillTransition.setAutoReverse(true);
        fillTransition.setCycleCount(Animation.INDEFINITE);
        fillTransition.play();
    }

    /**
     * Stops the glowing effect for a given label.
     *
     * @param label The label for which the glowing effect should be stopped.
     */
    private void stopLabelGlow(Label label) {
        if (label.getUserData() instanceof Animation) {
            ((Animation) label.getUserData()).stop();
        }
        label.setEffect(null);
        label.setStyle("""
                -fx-background-color: transparent;-fx-background-color: rgba(0, 0, 0, 0.6);
                    -fx-padding: 5;
                    -fx-background-radius: 30;"""); // Set the style back to default
    }

    /**
     * Returns a list of all player labels.
     *
     * @return A list containing all player labels.
     */
    private List<Label> getAllPlayerLabels() {
        // Return all player labels
        return Arrays.asList(p1, p2, p3, p4, p5, p6);
    }


    Integer gridPanesIdx = 0;

    // Assume this is called when the main card is clicked to start distribution
    // This is just a dummy
    public void distributeCards() {
        if (isGameStarted){
            Deck deck = serverTable.getNachzieheDeck();
            ServerCard serverCard = deck.ziehen();
            Card card = new Card(serverCard.getType(), serverCard.getValue(), serverCard.isCovered());

            if(card != null){
                String cardName = card.getType() + card.getValue();
                // Create a new serverCard ImageView for distribution
                ImageView cardView = new ImageView(String.valueOf(card.getImage()));
                cardView.setId(cardName);
                imageViewMap.put(cardName, cardView);
                // Set a unique ID for the ImageView
                cardView.setFitHeight(50);
                cardView.setFitWidth(80);
                setupCardDragEvents(cardView);

                // Add the serverCard to the grid, then update the position for the next serverCard
                CardGridPane gridPane = gridPanesList.get(gridPanesIdx);
                int[] nextCell = gridPane.getNextAvailableCell();

                // Find the cell in the grid where the drop occurred
                int rowIndex = nextCell[0];
                int colIndex = nextCell[1];
                gridPane.addCard(imageViewMap.get(cardName), rowIndex, colIndex);
                gridPanesIdx++;

                // Check if the serverCard has been distributed to all panes at the current cell
                if (gridPanesIdx >= gridPanesList.size()) {
                    // Reset the distribution counter
                    gridPanesIdx = 0;

                    // Increment the index to cycle through the GridPanes
                    gridPanesIdx = (gridPanesIdx) % gridPanesList.size();
                }
            }
        }



    }

    /**
     * Sets up drag events for a given ImageView representing a card.
     *
     * @param cardView The ImageView representing the card to which drag events are applied.
     */
    private void setupCardDragEvents(ImageView cardView) {
        // Mouse Pressed Event: Triggered when the user presses the mouse button on the card.
        cardView.setOnMousePressed(event ->{
            // Store the card being dragged and its source GridPane.
            draggedImage = cardView;
            sourceGridPane = (CardGridPane) cardView.getParent();
        });

        // Drag Detected Event: Triggered when the system detects a drag operation on the card.
        cardView.setOnDragDetected(event -> {
            // Create a Drag board to manage the drag-and-drop operation.
            Dragboard db = cardView.startDragAndDrop(TransferMode.MOVE);

            // Set the drag view to a snapshot of the card.
            db.setDragView(cardView.snapshot(null, null));

            // Prepare the clipboard content with the card's ID.
            ClipboardContent content = new ClipboardContent();
            content.putString(cardView.getId());
            db.setContent(content);

            // Store the card being dragged and its source GridPane.
            draggedImage = cardView;
            sourceGridPane = (CardGridPane) cardView.getParent();

            // Consume the event to indicate it has been handled.
            event.consume();
        });
    }

    // TO SET UP CARD GRID PANE OF DROP EFFECT
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
                int rowIndex = nextCell[0];
                int colIndex = nextCell[1];

                // Add image to the target grid pane at the same cell
                // If dropping on an empty grid cell, you might need to calculate the cell
                // indices based on drop location
                gridPane.addCard(draggedImage, rowIndex, colIndex);

                success = true;
            }
            /*
             * let the source know whether the string was successfully
             * transferred and used
             */
            event.setDropCompleted(success);

            event.consume();
        });
    }

    public void hahnKarteGeben(UUID playerID){
        players.get(playerID).setHahnKarte(true);
    }

    public void playerLeftGameSession(UUID disconnectedPlayerID, String botName) {
        Platform.runLater(() -> {
            // Create an alert dialog to inform players about the player who left
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Player Left");
            alert.setHeaderText("Player has left the game session");
            alert.setContentText("Player with ID " + disconnectedPlayerID + " has left the game session.");

            // Add a button to acknowledge the message
            alert.getButtonTypes().setAll(ButtonType.OK);

            // Show the dialog
            alert.showAndWait();

            //Swap player with bot
            swapPlayerWithBot(disconnectedPlayerID,botName);
            //change the label of the disconnectPlayer to the Bot name
            Spieler disconnectPlayer = players.get(disconnectedPlayerID);
            disconnectPlayer.getPlayerLabel().setText(botName);
        });

    }
    public void swapPlayerWithBot(UUID playerId, String botName){
        players.get(playerId).setBot(true);
        players.get(playerId).setServerPlayerName(botName);

    }

    /*
    public void drawCard{
        if (client.getClientId()== currentPlayerID){

        };
    };
    */

}