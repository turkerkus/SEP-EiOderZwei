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
    public Label pt6;
    public Label pt5;
    public Label pt4;
    public Label pt3;
    public Label pt1;
    public Label pt2;
    public GridPane player6LabelGrid;
    public GridPane player5LabelGrid;
    public GridPane player4LabelGrid;
    public GridPane player3LabelGrid;
    public GridPane player1LabelGrid;
    public GridPane player2LabelGrid;

    private ImageView draggedImage;
    private CardGridPane sourceGridPane;


    // create a list of player grid panes
    List<CardGridPane> gridPanesList = new ArrayList<>();
    List<Label> labelList = new ArrayList<>();
    private Map<UUID, Spieler> players = new ConcurrentHashMap<>();
    private Map<UUID, ServerPlayer> serverPlayers;


    private Client client;

    private UUID currentPlayerID;
    @FXML
    private Label timerLabel; // This is the Label that displays the timer


    // Time left in seconds

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
        Label[] labels = new Label[]{ p2, p3, p4, p5, p6, pt2, pt3, pt4, pt5, pt6,};
        // Set all labels to empty initially
        for (Label label : labels) {
            label.setText("");
        }


        int numPlayers = players.size();
        // Arrays for labels and grid panes based on the number of players
        Label[][] pointLabels = {
                {},  // Case 0 (unused)
                {},  // Case 1 (unused)
                {pt5},  // Case 2
                {pt4, pt6},  // Case 3
                {pt4, pt5, pt6},  // Case 4
                {pt3, pt4, pt5, pt6},  // Case 5
                {pt2, pt3, pt4, pt5, pt6}  // Case 6
        };

        GridPane[][] playersLabelGrid = {
                {},  // Case 0 (unused)
                {},  // Case 1 (unused)
                {player5LabelGrid},  // Case 2
                {player4LabelGrid, player6LabelGrid},  // Case 3
                {player4LabelGrid, player5LabelGrid, player6LabelGrid},  // Case 4
                {player3LabelGrid, player4LabelGrid, player5LabelGrid, player6LabelGrid},  // Case 5
                {player2LabelGrid, player3LabelGrid, player4LabelGrid, player5LabelGrid, player6LabelGrid}  // Case 6
        };


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
                    player.setPlayerLabelGrid(playersLabelGrid[numPlayers][nonHostPlayerIndex]);
                    // set the player pt label
                    pointLabels[numPlayers][nonHostPlayerIndex].setText("Pt: 0");
                    
                    player.setPlayerPoint(pointLabels[numPlayers][nonHostPlayerIndex]);


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
            this.players.put(spielerID, spieler);
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
     */
    public void startGameUiUpdate() {
        Spieler currentPlayer = players.get(this.currentPlayerID);
        Label currentPlayerLabel = currentPlayer.getPlayerLabel();
        System.out.println("this is the current player" + currentPlayer);
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

    public void hahnKarteGeben(UUID playerID) {
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
            swapPlayerWithBot(disconnectedPlayerID, botName);
            //change the label of the disconnectPlayer to the Bot name
            Spieler disconnectPlayer = players.get(disconnectedPlayerID);
            disconnectPlayer.getPlayerLabel().setText(botName);
        });

    }

    public void swapPlayerWithBot(UUID playerId, String botName) {
        players.get(playerId).setBot(true);
        players.get(playerId).setServerPlayerName(botName);

    }


    public void drawCard() {

        try {

            System.out.println("Draw card : the id of the client drawing the card " + client.getClientId());
            System.out.println("Draw card : the id of the currentPlayerId  " + this.currentPlayerID);

            if (Objects.equals(client.getClientId(), this.currentPlayerID)) {
                client.drawCard();
            } else {
                // Create an alert to inform the player that it's not their turn
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not Your Turn");
                alert.setHeaderText(null);
                alert.setContentText("It's not your turn to play so you cannot draw a card.");

                // Show the alert
                alert.showAndWait();
            }


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }

    public ArrayList<ImageView> auswahl(){
        ArrayList<ImageView> select= new ArrayList<ImageView>();
        UUID clientId = client.getClientId();
        Spieler player = players.get(clientId);
        CardGridPane grid= player.getCardGridPane();
        for(int i=0;i<grid.getChildren().size();i++){
            ImageView node =(ImageView) grid.getCardInCell(i);
            node.setOnMouseClicked(e ->{
                select.add(node);
                String Source = node.getImage().getUrl();
            });





        }
        return select;
    }



    public void getEggs() {
        Platform.runLater(() -> {
            try {
                if (Objects.equals(client.getClientId(), this.currentPlayerID)) {
                    // TODO check if current player has enough corn to exchange for eggs
                    Spieler player = players.get(client.getClientId());
                    if (player.getKornzahl()>=5){
                        System.out.println("LAY EGGS BOOOCK BOOOOOOOOOOOCK!"); //TODO delete after
                        client.karteUmtauschen();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Illegal Move");
                        alert.setHeaderText("Not enough corns!");
                        alert.setContentText("5 or more corns are needed to lay eggs.");
                        alert.showAndWait();
                    };
                } else {
                    // Create an alert to inform the player that it's not their turn
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Not Your Turn");
                    alert.setHeaderText(null);
                    alert.setContentText("It's not your turn to play so you cannot generate eggs.");

                    // Show the alert
                    alert.showAndWait();
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void stealRooster() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        UUID clientId = client.getClientId();
        Spieler player = players.get(clientId);
        try {
            if (Objects.equals(client.getClientId(), this.currentPlayerID)) {
                ServerPlayer roosterPlayer = client.getRoosterPlayer();
                Integer roosterPlayerPoint = roosterPlayer.getPunkte();
                if (player.getPunkte() < roosterPlayerPoint && !player.hatHahnKarte()) {
                    client.hahnKlauen();
                } else {
                    if (player.hatHahnKarte()) {
                        alert.setTitle("Illegal Move");
                        alert.setHeaderText("Steal Rooster Card");
                        alert.setContentText("You already have the Rooster Card");

                        // Show the alert
                        alert.showAndWait();
                    } else {
                        alert.setTitle("Illegal Move");
                        alert.setHeaderText("Steal Rooster Card");
                        alert.setContentText("you don't have less points than the rooster owner to steal the rooster card!");

                        // Show the alert
                        alert.showAndWait();
                    }
                }

            } else {
                // Create an alert to inform the player that it's not their turn

                alert.setTitle("Not Your Turn");
                alert.setHeaderText(null);
                alert.setContentText("It's not your turn to play so you cannot steal someones rooster card.");

                // Show the alert
                alert.showAndWait();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }

    public void hasDrawnACard(UUID playerId, ServerCard serverCard) {
        Platform.runLater(() -> {
            // Convert server Card to card
            Card drawnCard = convertCard(serverCard);
            players.get(playerId).addCard(drawnCard, serverCard);
        });

    }

    public Card convertCard(ServerCard serverCard) {
        return new Card(
                serverCard.getType(),
                serverCard.getValue(),
                serverCard.isCovered()
        );
    }

    public void changeRoosterPlayer(UUID oldRoosterPlayerID, UUID newRoosterPlayerID) {
        Platform.runLater(() -> {
            Spieler newRoosterPlayer = players.get(newRoosterPlayerID);
            Spieler oldRoosterPlayer = players.get(oldRoosterPlayerID);

            oldRoosterPlayer.setHahnKarte(false);


            newRoosterPlayer.setHahnKarte(true);

        });
    }

    public void drawnKuckuck(UUID playerId) {
        if (Objects.equals(playerId, client.getClientId())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("A Kuckuck card!");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You have drawn a Kuckuck card. Your egg-Score has been increased by 1!");
            alert.showAndWait();
        }
    }


}