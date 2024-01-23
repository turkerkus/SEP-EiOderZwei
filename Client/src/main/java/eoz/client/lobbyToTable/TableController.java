package eoz.client.lobbyToTable;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public CardGridPane player6LabelGrid;
    public CardGridPane player5LabelGrid;
    public CardGridPane player4LabelGrid;
    public CardGridPane player3LabelGrid;
    public CardGridPane player1LabelGrid;
    public CardGridPane player2LabelGrid;

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
        Label[] labels = new Label[]{p2, p3, p4, p5, p6, pt2, pt3, pt4, pt5, pt6,};
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

        boolean[][] setStartingRowIndex = {
                {},  // Case 0 (unused)
                {},  // Case 1 (unused)
                {true},  // Case 2
                {true, true},  // Case 3
                {true, true, true},  // Case 4
                {false, true, true, true},  // Case 5
                {false, false, true, true, true}  // Case 6
        };

        CardGridPane[][] playersLabelGrid = {
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
                player1GridPane.setStartFromZero(false);
                player1GridPane.setRow();
                player1GridPane.updateNextAvailableCell();
                player.setCardGridPane(player1GridPane);
                player.setPlayerLabelGrid(player1LabelGrid);
                player.setPlayerPointlabel(pt1);

            } else {
                // Handle non-host players
                if (numPlayers >= 2 && numPlayers <= 6) {
                    // configure the starting row index of the gridPane
                    boolean startingIndex = setStartingRowIndex[numPlayers][nonHostPlayerIndex];
                    player.setPlayerLabel(labelCases[numPlayers][nonHostPlayerIndex]);
                    gridPaneCases[numPlayers][nonHostPlayerIndex].setStartFromZero(startingIndex);
                    gridPaneCases[numPlayers][nonHostPlayerIndex].setRow();
                    gridPaneCases[numPlayers][nonHostPlayerIndex].updateNextAvailableCell();

                    // assign the gridPane to the player
                    player.setCardGridPane(gridPaneCases[numPlayers][nonHostPlayerIndex]);
                    player.setPlayerLabelGrid(playersLabelGrid[numPlayers][nonHostPlayerIndex]);

                    // set the player pt label
                    pointLabels[numPlayers][nonHostPlayerIndex].setText("Pt: 0");

                    player.setPlayerPointlabel(pointLabels[numPlayers][nonHostPlayerIndex]);


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
                //TODO CHECK THIS FOR THE DRAG FEATURE
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

    private void handleCardClick(ImageView imageView) {
        ServerCard card = (ServerCard) imageView.getUserData();
        // Toggle selection
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
            imageView.setStyle(""); // Reset style for unselected card
        } else {
            selectedCards.add(card);
            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(30, 30, 133, 1), 10, 0, 0, 0)"); // Style for selected card
        }


    }

    private CardGridPane cloneGridPaneWithImageViews(CardGridPane originalGridPane) {
        CardGridPane clonedGridPane = new CardGridPane();
        for (Node node : originalGridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView originalImageView = (ImageView) node;
                ImageView clonedImageView = new ImageView(originalImageView.getImage());

                // Set a maximum width and height for the clonedImageView
                clonedImageView.setFitWidth(80); // Adjust the values as needed
                clonedImageView.setFitHeight(50);
                ServerCard card = (ServerCard) originalImageView.getUserData();
                clonedImageView.setUserData(card); // Preserve the card data
                if (Objects.equals(card.getType(), "Koerner") || Objects.equals(card.getType(), "BioKoerner")) {
                    clonedImageView.setOnMouseClicked(event -> handleCardClick(clonedImageView));
                }
                // Add clonedImageView to the clonedGridPane at the same position as the original
                clonedGridPane.add(clonedImageView, GridPane.getColumnIndex(originalImageView), GridPane.getRowIndex(originalImageView));
            }
        }
        return clonedGridPane;
    }


    ArrayList<ServerCard> selectedCards;

    public void auswahl() {


        UUID clientId = client.getClientId();
        Spieler player = players.get(clientId);
        CardGridPane sourceGridPane = cloneGridPaneWithImageViews(player.getCardGridPane());

        // Create the dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Card Selection");
        dialog.setHeaderText("Select Cards");

        // Dialog content
        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().add(sourceGridPane);


        // Add confirm and cancel buttons
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Set content
        dialog.getDialogPane().setContent(dialogContent);

        // Handle the confirmation action
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                dialog.close();
            }
            return null;
        });

        // Show dialog
        dialog.showAndWait();

    }

    public ArrayList<Integer> calculateEggPoints(ArrayList<ServerCard> selectedCards) {
        int kornzahl = 0;
        int bkornzahl = 0;
        int kornzahlwert = 0;
        int bKornzahlwert = 0;
        int eggPoints = 0;

        for (ServerCard c : selectedCards) {
            if (c.getType().equals("Koerner")) {
                kornzahl += 1;
            } else {
                bkornzahl += 1;
            }
        }

        for (ServerCard c : selectedCards) {
            if (kornzahl == 0 && bkornzahl >= 1) {      //nur Biokörner
                bKornzahlwert += c.getValue();
            } else {                              //normale oder gemischte Körner
                kornzahlwert += c.getValue();

            }
        }


        // Calculate the total value for Koerner and BioKoerner cards


        // Each egg needs at least five seeds, organic grains count double
        // Every 5 grains make an egg, and every 5 organic grains make two eggs
        eggPoints += (int) Math.floor(kornzahlwert / 5);
        eggPoints += (int) Math.floor(bKornzahlwert / 5) * 2;




        return new ArrayList<>(Arrays.asList(eggPoints, kornzahlwert, bKornzahlwert));
    }


    public void getEggs() {
        Platform.runLater(() -> {
            try {
                if (Objects.equals(client.getClientId(), this.currentPlayerID)) {
                    selectedCards = new ArrayList<>();
                    auswahl();
                    ArrayList<Integer> points = calculateEggPoints(selectedCards);
                    Integer eggPoints = points.get(0);
                    Integer kornzahlwert = points.get(1);
                    Integer bKornzahlwert = points.get(2);

                    //TODO: ask if we will get any card from the ablageDeck

                    if (kornzahlwert >= 5 || bKornzahlwert >= 5) {
                        client.karteUmtauschen(eggPoints, selectedCards);

                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Illegal Move");
                        alert.setHeaderText("Not enough corns!");
                        alert.setContentText("5 or more corns are needed to lay eggs.");
                        alert.showAndWait();
                    }
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
                Spieler roosterPlayer = players.get(client.getRoosterPlayer().getServerPlayerId()) ;
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


    public void drawnKuckuckCard(UUID playerId) {
        Platform.runLater(() -> {
            Spieler player = players.get(playerId);
            if (Objects.equals(playerId, client.getClientId())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("A Kuckuck card!");
                alert.setHeaderText("Congratulations!");
                alert.setContentText("You have drawn a Kuckuck card. Your egg-Score has been increased by 1!");
                alert.showAndWait();

                player.raisePunkte();
                ServerCard kuckuckCard = player.getKuckuckCard();
                removeMultipleCards(playerId, new ArrayList<>(Collections.singletonList(kuckuckCard)));


            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("A Kuckuck card!");
                alert.setHeaderText(player.getServerPlayerName() + " has drawn a Kuckuck card!");
                alert.setContentText("His/her egg-Score has been increased by 1!");
                alert.showAndWait();
                player.raisePunkte();
                ServerCard kuckuckCard = player.getKuckuckCard();
                removeMultipleCards(playerId, new ArrayList<>(Collections.singletonList(kuckuckCard)));
            }

        });

    }

    public void drawnFoxCard(UUID playerID) {
        Platform.runLater(() -> {
            if (Objects.equals(playerID, client.getClientId())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("A Fox Card");
                alert.setHeaderText("You have drawn a Fox Card");
                alert.setContentText("Do you want to use your Fox Card?");

                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");

                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        System.out.println("Player chose to use the Fox Card.");
                        // Perform the action for using the Fox Card here
                    } else if (response == noButton) {
                        System.out.println("Player chose not to use the Fox Card.");
                        // Perform any other action here if needed
                    }
                });
            }
        });
    }

    public void cardDiscarded(UUID playerID, ServerCard discardedCard, Integer eggPoints, ArrayList<ServerCard> discardedCards) {
        Platform.runLater(() -> {
            Spieler player = players.get(playerID);

            if (Objects.equals(playerID, client.getClientId())) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Eggs has been laid");
                alert.setHeaderText("You has laid eggs successfully!");
                alert.setContentText(" Your egg-Score has been increased by " + eggPoints + "!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Eggs has been laid");
                alert.setHeaderText(player.getServerPlayerName() + " has laid eggs!");
                alert.setContentText(player.getServerPlayerName() + "'s egg-Score has been increased by " + eggPoints + "!");
                alert.showAndWait();

            }
            player.increasePointsBy(eggPoints);

            // remove the cards
            removeMultipleCards(playerID,discardedCards);


            //display the discarded card on the AblageDeck
            Card newDiscardcard = convertCard(discardedCard);
            // Create a new card ImageView for distribution
            ImageView cardView = new ImageView(newDiscardcard.getImage());
            cardView.setFitHeight(89);
            cardView.setFitWidth(97);
            mainCardsGridPane.add(cardView, 0, 0);



        });
    }

    public void removeMultipleCards(UUID playerID, ArrayList<ServerCard> discardedCards){
        // Executor to schedule card removal with delays
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // Schedule the card removal with a delay
        int delay = 0; // Initial delay
        for (ServerCard card : discardedCards) { // No need to create a new ArrayList here
            executorService.schedule(() -> Platform.runLater(() -> players.get(playerID).removeCard(card)), delay, TimeUnit.SECONDS);
            delay += 1; // Increment delay for the next card
        }

        // Ensure the executor service is properly shutdown after all tasks
        executorService.schedule(() -> {
            // Reorganize the GridPane after all removals are complete
            Platform.runLater(() -> players.get(playerID).reorganizeGridPane());
            executorService.shutdown();
        }, delay + 1, TimeUnit.SECONDS); // Schedule this after the last removal

    }

    public void sendAction(){

    }

    public void emojiAction(){

    }


}