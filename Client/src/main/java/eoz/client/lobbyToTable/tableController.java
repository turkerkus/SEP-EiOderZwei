package eoz.client.lobbyToTable;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import rmi.Client;
import rmi.serverPlayer;

import java.rmi.RemoteException;
import java.util.*;

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
    List<CardGridPane> gridPanes = new ArrayList<>();
    private Table table;
    private List<Spieler1> spielerList = new ArrayList<>(6);

    private Client client;


    private final Spiellogik gameLogic = new Spiellogik();
    private int currentPlayerIndex ;
    @FXML
    private Label timerLabel; // This is the Label that displays the timer

    //TODO this attributes (timerTimeline)has to be on the server
    private Timeline timerTimeline;
    private Integer timeLeft; // Time left in seconds



    private Boolean isGameStarted = false;
    private String mainPlayerName = "MainPlayer";

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void initialize() {

        // Setup drag and drop for each player grid pane
        setupGridPaneForDrop(player1GridPane);
        setupGridPaneForDrop(player2GridPane);
        setupGridPaneForDrop(player3GridPane);
        setupGridPaneForDrop(player5GridPane);
        setupGridPaneForDrop(player6GridPane);

        //TODO  THIS IS A DUMMY IT HAS TO REMOVED LATTER
        gridPanes.add(player1GridPane);
        gridPanes.add(player2GridPane);
        gridPanes.add(player3GridPane);
        gridPanes.add(player4GridPane);
        gridPanes.add(player5GridPane);
        gridPanes.add(player6GridPane);

    }
    public void assignCardGridPane() throws RemoteException {
        if (this.client != null) {

            try {
                // get player list
                List<serverPlayer> players = this.client.getPlayerList();
                convertAndSetPlayers(players);
            } catch (RemoteException e) {
                e.getMessage();

            }

        }


        //Assign the CardGridPane and Player label
        Label[] labels = new Label[]{p2,p3,p4,p5,p6};
        CardGridPane[] cardGridPanes = new CardGridPane[]{player2GridPane, player3GridPane, player4GridPane, player5GridPane, player6GridPane};

        int labelIndex = 0; // To keep track of which label and cardGridPane to assign next

        for (Spieler1 player : this.spielerList){
            if (player.getPlayerName().equals(this.mainPlayerName)){
                // Assign the main player to p1 and player1GridPane
                player.setPlayerLabel(p1);
                player.setCardGridPane(player1GridPane);
            } else {
                // Assign the rest of the players to the next label and cardGridPane
                if (labelIndex < labels.length) {
                    player.setPlayerLabel(labels[labelIndex]);
                    player.setCardGridPane(cardGridPanes[labelIndex]);
                    labelIndex++; // Move to the next label and cardGridPane
                }
            }
        }
    }




    public void convertAndSetPlayers(List<serverPlayer> players) {
        // Assuming you have a way to get JavaFX components for each player
        for (serverPlayer player : players) {
            CardGridPane dummyPane = new CardGridPane();
            Label dummyLabel = new Label();

            Spieler1 spieler = new Spieler1(
                    player.getId(),
                    player.getName(),
                    player.isHasHahnKarte(),
                    player.getKornzahl(),
                    dummyPane,
                    dummyLabel
            );

            // Add to your spielerList or handle as needed
            spielerList.add(spieler);
        }
    }

    public String getMainPlayerName() {
        return mainPlayerName;
    }

    public void setMainPlayerName(String mainPlayerName) {
        this.mainPlayerName = mainPlayerName;
    }

    // TODO for setStartButton():
    //  1.SEND AN ACTION TO THE SERVER THAT THE GAME AS START by setting setGameStarted(true).
    //  2.SEND THE TABLE CREATE TO SERVER. FOR THE SERVE TO USE clientStartGame() method to set  IT FOR OTHER CLIENTS
    //  3.Give the currentPlayerIndex  to server. FOR THE SERVE TO USE clientStartGame() method to set  IT FOR OTHER CLIENTS
    //  4.

    /**
     * Sets up the start button action to begin the game if it hasn't started already.
     * Randomly selects the first player, assigns the 'hahn' card to them, initializes the table,
     * sets the first player as the active player, initializes and shuffles the deck, and starts the first player's turn.
     */
    public void setStartButton() {

        // Check if the game has already started
        if (!isGameStarted) {
            // Randomly choose the first player
            Random random = new Random();
            int numOfPlayers = 6;
            int firstPlayerIndex = random.nextInt(numOfPlayers);
            Spieler1 firstPlayer = spielerList.get(firstPlayerIndex);

            // Give the firstPlayer the 'hahn' card
            firstPlayer.setHahnKarte(true);

            // Set the firstPlayer as the current player
            this.currentPlayerIndex = firstPlayerIndex;

            // Create a table
            this.table = new Table(spielerList);

            // Set the firstPlayer as the active player in the table
            table.setActive(firstPlayerIndex);

            // Initialize the deck and shuffle it
            table.intiNachZiehDeck();
            //table.shuffleDeck();

            //TODO GIVE THE TABLE TO THE SEVER ON THE NEXT LINE
            // eg: sever.settable(this.table)

            // TODO CHANGE THIS TO SERVER
            //  eg: sever.startPlayerTurn();
            // Start the first player's turn
            startPlayerTurn();

            // Mark the game as started
            isGameStarted = true;
        }
    }

    public void clientStartGame(Table table, Integer currentPlayerIndex){
        this.isGameStarted = true;
        this.table = table;
        this.currentPlayerIndex = currentPlayerIndex;

    }



    //TODO startPlayerTurn() has to be implemented in the server
    /**
     * Starts the turn for the current player, sets up a timer, and updates the UI accordingly.
     * Checks for end-game conditions and handles game over if necessary.
     */
    private void startPlayerTurn() {
        // Check for end-game condition
        if (gameLogic.SpielzugManager(this.spielerList, this.table)) {
            // Handle game over (declare winner, etc.)
            // TODO: SWITCH TO SCORE BOARD OR SHOW A DIALOG BOX
            // This is just a placeholder
            return;
        }

        // Get the current player
        Spieler1 currentPlayer = this.spielerList.get(currentPlayerIndex);

        //TODO: this is implement on the client side
        // and the sever have to update the current player ui for all clients
        // Update UI elements for the current player's turn
        updateUIForCurrentPlayer(currentPlayer);


        // TODO THIS SHOULD BE CALLED FROM THE SERVER SIDE
        //  Eg: sever.startTurnTimer
        // Set up a 2-minute timer for the player's turn
        startTurnTimer(120);
    }


    //TODO :  this startTurnTimer() has to be on the server
    /**
     * Starts the turn timer with the specified duration in seconds.
     * @param durationInSeconds The duration of the turn timer in seconds.
     */
    public void startTurnTimer(int durationInSeconds) {
        timeLeft = durationInSeconds;

        // Stop any existing timer
        if (timerTimeline != null) {
            timerTimeline.stop();
        }

        // Create a new timeline for the timer
        timerTimeline = new Timeline();
        timerTimeline.setCycleCount(Timeline.INDEFINITE);

        // Define a key frame to update the timer
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(1),
                event -> {
                    timeLeft--;
                    //TODO this has to be can to player1.updateTimerLabel(). you do this for all player
                    updateTimerLabel();
                    if (timeLeft <= 0) {
                        timerTimeline.stop();
                        // Call method to handle end of timer
                        onTimerEnd();
                    }
                });

        // Add the key frame to the timeline and start the timer
        timerTimeline.getKeyFrames().add(keyFrame);
        timerTimeline.playFromStart();
    }

    /**
     * Updates the timer label with the remaining time.
     */
    private void updateTimerLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("Timer: %02d:%02d", minutes, seconds));
    }

    /**
     * Handles what should happen when the timer ends, such as ending the current player's turn.
     */
    private void onTimerEnd() {
        // Handle what should happen when the timer ends
        // For example, end the current player's turn
        endPlayerTurn();
    }


    /**
     * Ends the turn for the specified player and performs end-of-turn actions.
     * Moves to the next player's turn.
     *
     */
    private void endPlayerTurn() {
        // Perform end-of-turn actions for the player

        // Move to the next player's turn
        currentPlayerIndex = (currentPlayerIndex + 1) % this.spielerList.size();
        startPlayerTurn(); // Start the next player's turn
    }



    /**
     * Updates the UI to highlight the current player's label with a glowing effect.
     * Also, stops the glowing effect for all other player labels.
     *
     * @param currentPlayer The current player whose label should be highlighted.
     */
    public void updateUIForCurrentPlayer(Spieler1 currentPlayer) {
        Label currentPlayerLabel = currentPlayer.getPlayerLabel();
        if (currentPlayerLabel != null) {
            // Create a glow effect
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
            Deck1 deck = table.getNachzieheDeck();
            Card1 card = deck.ziehen();

            if(card != null){
                String cardName = card.getType() + card.value;
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
                int rowIndex = nextCell[0];
                int colIndex = nextCell[1];
                gridPane.addCard(imageViewMap.get(cardName), rowIndex, colIndex);
                gridPanesIdx++;

                // Check if the card has been distributed to all panes at the current cell
                if (gridPanesIdx >= gridPanes.size()) {
                    // Reset the distribution counter
                    gridPanesIdx = 0;

                    // Increment the index to cycle through the GridPanes
                    gridPanesIdx = (gridPanesIdx) % gridPanes.size();
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
        cardView.setOnMousePressed(event -> {
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

    //TODO FRANK HAS TO FIX THIS DEPENDING ON THE NUMBER OF BOT OR PLAYERS
    public void displayName( double numOfPlayers) {

        switch ((int) numOfPlayers) {
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