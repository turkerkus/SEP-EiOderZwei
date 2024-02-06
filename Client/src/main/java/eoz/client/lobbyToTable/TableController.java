package eoz.client.lobbyToTable;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import sharedClasses.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TableController implements Serializable, Initializable, TableControllerInterface {
    //chat Vars start

    @FXML
    public AnchorPane chatPane;
    @FXML
    public TextArea input;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public VBox chatBox;
    @FXML
    public Button btnSend;
    @FXML
    public TextFlow emojiList;
    @FXML
    public Button btnEmoji;
    //Chat Vars end
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

    public void updateChat(String message, UUID playerId) {
        Platform.runLater(() -> {
            String playerName = players.get(playerId).getServerPlayerName();
            Text chatText = new Text("   " + playerName+ ": " + message);
            chatText.wrappingWidthProperty().bind(chatBox.widthProperty());
            chatText.setFill(players.get(playerId).getPlayerColor());
            chatText.setFont(Font.font("Verdana", FontPosture.REGULAR, 18));
            chatBox.getChildren().add(chatText);
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Node text : emojiList.getChildren()){
            text.setOnMouseClicked(event -> {
                input.setText(input.getText()+ " " +((Text)text).getText());
                emojiList.setVisible(false);
            });
        }
        scrollPane.vvalueProperty().bind(chatBox.heightProperty());
        this.input.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue){
                input.setScrollTop(Double.MAX_VALUE);
            }
        });
    }

    @FXML
    public void sendAction() throws RemoteException{
        String msg = input.getText().trim();
        if(!msg.isEmpty() && !msg.isBlank()){
            client.sendChatMessage(msg);
            input.setText("");
        }

    }
    @FXML
    public void enterChatHandle(KeyEvent event) throws RemoteException{
        if(event.getCode().equals(KeyCode.ENTER)){
            String msg = this.input.getText();
            if(!msg.isEmpty() && !msg.isBlank()){
                client.sendChatMessage(msg);
                input.setText("");
            }
        }
    }

    @FXML
    public void emojiAction(ActionEvent actionEvent) {
        emojiList.setVisible(!emojiList.isVisible());
    }



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
        Color[][]  chatColor = {
                {},  // Case 0 (unused)
                {},  // Case 1 (unused)
                {Color.YELLOWGREEN},  // Case 2
                {Color.PURPLE, Color.ORANGE},  // Case 3
                {Color.PURPLE, Color.YELLOWGREEN, Color.ORANGE},  // Case 4
                {Color.GREEN, Color.PURPLE, Color.YELLOWGREEN, Color.ORANGE},  // Case 5
                {Color.WHITE, Color.GREEN, Color.PURPLE, Color.YELLOWGREEN, Color.ORANGE}  // Case 6
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
        for (Entry<UUID, Spieler> entry : players.entrySet()) {
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
                player.setPlayerColor(Color.RED);

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
                    player.setPlayerColor(chatColor[numPlayers][nonHostPlayerIndex]); //This is to set the color of the player/chat

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
        for (Entry<UUID, ServerPlayer> entry : players.entrySet()) {
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
        Platform.runLater(() -> {

            Spieler currentPlayer = players.get(this.currentPlayerID);
            Label currentPlayerLabel = currentPlayer.getPlayerLabel();
            if (currentPlayerLabel != null) {
                // Create a glow effect
                DropShadow dropShadow = new DropShadow();
                dropShadow.setColor(Color.CYAN);
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
        });

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
            alert.setContentText(players.get(disconnectedPlayerID).getServerPlayerName()+ " has left the game session.");

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

            // check for client id and available cells
            if (Objects.equals(client.getClientId(), this.currentPlayerID)) {
                if(checkForAvailableCell(null)) client.drawCard();
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

    /**
     * this method helps you check if the client has enough space before allowing it to draw or steal a card
     * if there is no enough space it make the client to exchange the card in his hand for eggs
     * @param targetedPlayer
     * @return boolean
     */
    private boolean checkForAvailableCell(Spieler targetedPlayer) {
        Spieler player = players.get(client.getClientId());

        // the total number of cards that can be displayed id 24
        int playersAvailableCells = 24 - player.getCardHand().size();

        System.out.println(player.getServerPlayerName() + " has "+ playersAvailableCells +" cells available");

        // Check available cells before drawing a card
        if (targetedPlayer == null) {
            int requiredCells = player.hatHahnKarte() ? 2 : 1;
            if ( playersAvailableCells< requiredCells) {
                showSpaceAlert();
                getEggs();
                drawCard();
                return false; // Indicates not enough space to draw a card
            }
        }
        // Check available cells before stealing cards
        else {
            int cellsNeeded = selectedCards.size() == 1 ? 1 : targetedPlayer.getCardHand().size() - 1;
            if (playersAvailableCells < cellsNeeded) {
                showSpaceAlert();
                getEggs();
                drawnFoxCard(client.getClientId(), null);
                return false; // Indicates not enough space to steal cards
            }
        }
        return true; // Indicates enough space available
    }

    private void showSpaceAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Less space to store card");
        alert.setHeaderText("You don't have enough space! \n Exchange some of your cards for egg.");
        alert.showAndWait();
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

    private Map<ServerCard, ImageView> cardImageViewMap = new HashMap<>();


    private void handleSingleCardSelection(ImageView imageView) {

        ServerCard card = (ServerCard) imageView.getUserData();

        if (selectedCards.size() == 1) {
            // If one card is already selected, deselect it
            ServerCard selectedCard = selectedCards.get(0);
            selectedCards.clear(); // Clear the selected cards list
            // Reset style for the previously selected card
            ImageView selectedImageView = cardImageViewMap.get(selectedCard); // Implement this method
            if (selectedImageView != null) {
                selectedImageView.setStyle("");
            }
        }
        // Now select the clicked card
        selectedCards.add(card);
        cardImageViewMap.put(card, imageView);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(30, 30, 133, 1), 10, 0, 0, 0)");
    }


    private CardGridPane cloneGridPaneWithImageViews(CardGridPane originalGridPane, Boolean onlySingleSelection) {
        CardGridPane clonedGridPane = new CardGridPane();
        cardImageViewMap = new HashMap<>();
        for (Node node : originalGridPane.getChildren()) {
            if (node instanceof ImageView originalImageView) {
                ImageView clonedImageView = new ImageView(originalImageView.getImage());

                // Set a maximum width and height for the clonedImageView
                clonedImageView.setFitWidth(80); // Adjust the values as needed
                clonedImageView.setFitHeight(50);
                ServerCard card = (ServerCard) originalImageView.getUserData();
                clonedImageView.setUserData(card); // Preserve the card data
                if (Objects.equals(card.getType(), "Koerner") || Objects.equals(card.getType(), "BioKoerner")) {
                    if (onlySingleSelection) {
                        clonedImageView.setOnMouseClicked(event -> handleSingleCardSelection(clonedImageView));
                    } else {
                        clonedImageView.setOnMouseClicked(event -> handleCardClick(clonedImageView));
                    }

                }
                // Add clonedImageView to the clonedGridPane at the same position as the original
                clonedGridPane.add(clonedImageView, GridPane.getColumnIndex(originalImageView), GridPane.getRowIndex(originalImageView));
            }
        }
        return clonedGridPane;
    }


    ArrayList<ServerCard> selectedCards;

    public void auswahl(UUID playerID,Boolean onlySingleSelection, String dialogTitle, String dialogHeader) {



        Spieler player = players.get(playerID);
        CardGridPane sourceGridPane = cloneGridPaneWithImageViews(player.getCardGridPane(), onlySingleSelection);

        // Create the dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);

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
        int bKornzahl = 0;
        int kornzahlwert = 0;
        int bKornzahlwert = 0;
        int eggPoints = 0;

        for (ServerCard c : selectedCards) {
            if (c.getType().equals("Koerner")) {
                kornzahl += 1;
            } else {
                bKornzahl += 1;
            }
        }

        for (ServerCard c : selectedCards) {
            if (kornzahl == 0 && bKornzahl >= 1) {      //nur Biokörner
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

    // this is used to prevent the player from getting egg card if he has already drawn
    // a fox card due to the delay in time to end player turn


    public void  getEggs() {
        Platform.runLater(() -> {
            try {
                if (Objects.equals(client.getClientId(), this.currentPlayerID) ) {
                    String dialogTitle = "Card Selection";
                    String dialogHeader = "Select Cards";
                    selectedCards = new ArrayList<>();
                    UUID clientId = client.getClientId();
                    auswahl(clientId,false, dialogTitle, dialogHeader);
                    ArrayList<Integer> points = calculateEggPoints(selectedCards);
                    Integer eggPoints = points.get(0);
                    Integer kornzahlwert = points.get(1);
                    Integer bKornzahlwert = points.get(2);



                    if (kornzahlwert >= 5 || bKornzahlwert >= 5) {
                        System.out.println(players.get(client.getClientId()).getServerPlayerName() + " has is changing the following cards for egg Points!");
                        for (ServerCard card : selectedCards){
                            System.out.println(card.toString());
                        }
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

      Platform.runLater(()->{
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          UUID clientId = client.getClientId();
          Spieler player = players.get(clientId);
          try {
              if (Objects.equals(client.getClientId(), this.currentPlayerID)) {
                  Spieler roosterPlayer = players.get(client.getRoosterPlayer().getServerPlayerId());
                  int roosterPlayerPoint = roosterPlayer.getPunkte();
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
      });


    }

    public void hasDrawnACard(UUID playerId, ServerCard serverCard) {
        Platform.runLater(() -> {
            // Convert server Card to card
            addMultipleCards(playerId,new ArrayList<>(Collections.singletonList(serverCard)));
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


    public void drawnKuckuckCard(UUID playerId, ServerCard kuckuckCard) {
        Platform.runLater(() -> {
            Spieler player = players.get(playerId);
            if (Objects.equals(playerId, client.getClientId())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("A Kuckuck card!");
                alert.setHeaderText("Congratulations!");
                alert.setContentText("You have drawn a Kuckuck card. Your egg-Score has been increased by 1!");
                alert.showAndWait();

                player.raisePunkte();

                removeMultipleCards(playerId, new ArrayList<>(Collections.singletonList(kuckuckCard)));


            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("A Kuckuck card!");
                alert.setHeaderText(player.getServerPlayerName() + " has drawn a Kuckuck card!");
                alert.setContentText("His/her egg-Score has been increased by 1!");
                alert.showAndWait();
                player.raisePunkte();
                removeMultipleCards(playerId, new ArrayList<>(Collections.singletonList(kuckuckCard)));
            }

        });

    }

    public void drawnFoxCard(UUID playerID, ServerCard foxCard) {

        Platform.runLater(() -> {

            selectedCards = new ArrayList<>();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Spieler player = players.get(playerID);
            if (Objects.equals(playerID, client.getClientId())) {
                // Create the dialog
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Fox Card Drawn!");
                dialog.setHeaderText("You have drawn a fox card.");

                // TabPane for selecting an opponent player and viewing their cards
                TabPane tabPane = new TabPane();

                // Iterate over players to create a tab for each opponent
                for (Spieler spieler : players.values()) {
                    if (!spieler.getServerPlayerId().equals(playerID)) {
                        Tab tab = new Tab(spieler.getServerPlayerName());

                        // Use the cloneGridPaneWithImageViews method to clone the player's card grid
                        CardGridPane clonedGridPane = cloneGridPaneWithImageViews(spieler.getCardGridPane(), true);
                        tab.setContent(clonedGridPane);

                        tabPane.getTabs().add(tab);
                    }
                }

                // Buttons for the actions
                ButtonType stealOneButtonType = new ButtonType("Steal one card", ButtonBar.ButtonData.OK_DONE);
                ButtonType stealAllButtonType = new ButtonType("Steal all cards", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE); // Set as default

                // Add action buttons to the dialog
                dialog.getDialogPane().getButtonTypes().addAll(stealOneButtonType, stealAllButtonType, cancelButtonType);

                // Set the TabPane as the content of the dialog
                dialog.getDialogPane().setContent(tabPane);

                // Handle the result from the dialog
                dialog.setResultConverter(dialogButton -> {
                    // Handle the action to steal one card
                    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                    UUID target = null;
                    for (Spieler spieler : players.values()) {
                        if (selectedTab.getText().equals(spieler.getServerPlayerName())) {
                            target = spieler.getServerPlayerId();
                        }
                    }
                    try {
                        Spieler targetedPlayer = players.get(target);
                        Hand targetedPlayerHand = targetedPlayer.getCardHand();
                        // you can only steal if the targetedPlayer has some cards in hand

                        if (dialogButton == stealOneButtonType ) {
                            // Logic to steal one card from the selected player
                            if(targetedPlayerHand.size() != 0){
                                if (!selectedCards.isEmpty() && checkForAvailableCell(targetedPlayer)) {
                                    client.stealOneCard(target, selectedCards);
                                } else {
                                    if(foxCard != null){
                                        client.removeFoxCard(foxCard);
                                    }

                                    alert.setTitle("Illegal Move");
                                    alert.setHeaderText("You have to select one cared");
                                    alert.showAndWait();
                                    drawnFoxCard(playerID,null);
                                }
                            } else {
                                if (Objects.equals(currentPlayerID, client.getClientId())){
                                    client.endPlayerTurn();

                                }
                            }



                        } else if (dialogButton == stealAllButtonType ) {
                            // Logic to steal all cards from the selected player

                            // if the targets only has one card you can only steal that card
                            if (targetedPlayerHand.size() == 1 && checkForAvailableCell(targetedPlayer)){

                                Map<UUID,ServerCard> bioCornCards = targetedPlayerHand.getBioCornCards();
                                Map<UUID,ServerCard> cornCards = targetedPlayerHand.getCornCards();
                                ServerCard card;
                                if (bioCornCards.isEmpty()){
                                    card = cornCards.values().iterator().next();
                                } else {
                                    card = bioCornCards.values().iterator().next();
                                }
                                selectedCards.add(card);
                                if(checkForAvailableCell(targetedPlayer)) client.stealOneCard(target, selectedCards);

                            } else if(targetedPlayerHand.size() == 0 && Objects.equals(currentPlayerID, client.getClientId())){


                                if (Objects.equals(currentPlayerID, client.getClientId())){
                                    client.endPlayerTurn();

                                }

                            }else {
                                if(checkForAvailableCell(targetedPlayer)) client.stealAllCards(target);
                            }

                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    return null; // Dialog has no result
                });

                // Show the dialog and wait for the user's response
                dialog.showAndWait();

                // remove card from the target
                if(foxCard != null){
                    removeMultipleCards(playerID, new ArrayList<>(Collections.singletonList(foxCard)));
                }

            } else {



                // remove card from the target
                if(foxCard != null){
                    alert.setTitle("A Fox card!");
                    alert.setHeaderText(player.getServerPlayerName() + " has drawn a Fox card!");
                    alert.setContentText("His/her has the chance to steal someone's cards");
                    alert.showAndWait();
                    removeMultipleCards(playerID, new ArrayList<>(Collections.singletonList(foxCard)));
                }

            }


        });
    }
    public void removeFoxCard(ServerCard foxCard) {
        Platform.runLater(() -> {
            for(Map.Entry<UUID,Spieler> entry : players.entrySet()) {
                Spieler player = entry.getValue();
                removeMultipleCards(player.getServerPlayerId(), new ArrayList<>(Collections.singletonList(foxCard)));
            }
        });
    }

    public void oneCardStolen(UUID targetId, ServerCard stolenCard, UUID playerId) {
        // TODO FINISH THE CARD STOLEN METHOD (DELETE IT PROPERLY AND ADD IT ON FOX OWNER)
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Card card = convertCard(stolenCard);
            Spieler player = players.get(playerId);
            Spieler targetedPlayer = players.get(targetId);
            if (client.getClientId().equals(targetId)) {
                alert.setTitle("A fox appears!");
                alert.setHeaderText(player.getServerPlayerName() + " is stealing the card below from You!");
            } else if (client.getClientId().equals(playerId)) {
                alert.setTitle("A fox appears!");
                alert.setHeaderText("You have successfully stolen the card below: ");
            } else {
                alert.setTitle("A fox appears!");
                alert.setHeaderText(player.getServerPlayerName() + " is stealing the card below from " + targetedPlayer.getServerPlayerName() + "!");
            }

            ImageView imageView = new ImageView(card.getImage());
            imageView.setFitWidth(100); // Set the width as needed
            imageView.setFitHeight(100); // Set the height as needed

            // Create a custom DialogPane
            VBox customPane = new VBox(imageView);

            // Set the custom DialogPane with the image as content
            alert.getDialogPane().setContent(customPane);
            alert.showAndWait();

            //Add card to the player how is stealing the card
            addMultipleCards(playerId,new ArrayList<>(Collections.singletonList(stolenCard)));


            // remove card from the target
            removeMultipleCards(targetId, new ArrayList<>(Collections.singletonList(stolenCard)));



        });
    }

    public void allCardsStolen(UUID targetId, UUID thiefID) {
        // TODO FINISH THE METHOD (MAKE A SELECTION FOR THE target, then make a remote to delete and add)
        Platform.runLater(() -> {
            //rest the selectedCards
            selectedCards = new ArrayList<>();
            //create an alert to let all other players
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            Spieler player = players.get(thiefID);

            if (client.getClientId().equals(targetId)) {
                // TODO Selection like getEgg, but only one card is selectable and this gets protected from stealing.
                String dialogTitle = "Fox Card Drawn!";
                String dialogHeader = player.getServerPlayerName() + " has decided to steal all your cards! " +
                        "\n you have to choose one card you want to keep";
                auswahl(targetId,true,dialogTitle,dialogHeader);
                if (selectedCards.isEmpty()){
                    alert.setTitle("Illegal Move");
                    alert.setHeaderText("You have to select one cared");
                    alert.showAndWait();
                    allCardsStolen(targetId,thiefID);
                } else{
                    //store th selected card
                    ServerCard card = selectedCards.get(0);

                    // empty the array list
                    selectedCards = new ArrayList<>();

                    // remove all the Unselect card from targets hand
                    Spieler targetedPlayer = players.get(targetId);
                    Hand targetedPlayerHand = targetedPlayer.getCardHand();
                    Iterator<Map.Entry<UUID, ServerCard>> cornCards = targetedPlayerHand.getCornCards().entrySet().iterator();
                    Iterator<Map.Entry<UUID, ServerCard>> bioCornCards = targetedPlayerHand.getBioCornCards().entrySet().iterator();

                    // Loop through both maps simultaneously and add the cards to the arraylist
                    while (cornCards.hasNext() || bioCornCards.hasNext()) {
                        if (cornCards.hasNext()) {
                            Map.Entry<UUID, ServerCard> entry1 = cornCards.next();
                            if (!Objects.equals(card.getServeCardID(),entry1.getKey())){
                                selectedCards.add(entry1.getValue());
                            }

                        }

                        if (bioCornCards.hasNext()) {
                            Map.Entry<UUID, ServerCard> entry2 = bioCornCards.next();
                            if (!Objects.equals(card.getServeCardID(),entry2.getKey())){
                                selectedCards.add(entry2.getValue());
                            }
                        }
                    }

                    // remove the cards from the hand
                    addMultipleCards(thiefID,selectedCards);
                    removeMultipleCards(targetId,selectedCards);

                    try {
                        System.out.println("sending request to steal all cards");
                        client.stealingInProgress(thiefID, targetId,selectedCards);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        });
    }
    public void stealingCardCompleted(UUID targetId, UUID thiefID, ArrayList<ServerCard> stollenCards) {
        Platform.runLater(() -> {
            String targetedPlayerName = players.get(targetId).getServerPlayerName();
            String thiefName = players.get(thiefID).getServerPlayerName();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Steal Cards successful");

            if (client.getClientId().equals(thiefID)) {
                // This client is the thief
                alert.setHeaderText("You have successfully stolen all the cards of " + targetedPlayerName + " except for one card");
                alert.showAndWait();
                addMultipleCards(thiefID,stollenCards);
                removeMultipleCards(targetId,stollenCards);

                // endplayer turn
                CustomTimer timer = new CustomTimer();
                timer.schedule(() -> {

                    try {
                        if (Objects.equals(currentPlayerID, client.getClientId())){
                            client.endPlayerTurn();
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                }, 5);

            } else if (!client.getClientId().equals(targetId)) {
                // This client is the targeted player, maybe no alert is needed or a different message
                alert.setHeaderText(thiefName +" is stealing all the cards of " + targetedPlayerName + " except for one card");
                alert.showAndWait();
                addMultipleCards(thiefID,stollenCards);
                removeMultipleCards(targetId,stollenCards);
            }
            System.out.println("Stealing Process Successfully complete");

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
            removeMultipleCards(playerID, discardedCards);


            //display the discarded card on the AblageDeck
            Card newDiscardcard = convertCard(discardedCard);
            // Create a new card ImageView for distribution
            ImageView cardView = new ImageView(newDiscardcard.getImage());
            cardView.setFitHeight(89);
            cardView.setFitWidth(97);
            mainCardsGridPane.add(cardView, 0, 0);


        });
    }

    public void addMultipleCards(UUID playerID, ArrayList<ServerCard> serverCards){
        // Executor to schedule card removal with delays
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        // Schedule the card removal with a delay
        int delay = 0; // Initial delay
        Spieler player = players.get(playerID);
        for(ServerCard serverCard : serverCards){
            Card card = convertCard(serverCard);
            executorService.schedule(() -> Platform.runLater(() -> player.addCard(card,serverCard)), delay, TimeUnit.SECONDS);
            delay += 1; // Increment delay for the next card

        }
        // Ensure the executor service is properly shutdown after all tasks
        executorService.schedule(() -> {
            // Reorganize the GridPane after all removals are complete
            Platform.runLater(() -> players.get(playerID).reorganizeGridPane());
            executorService.shutdown();
        }, delay + 1, TimeUnit.SECONDS); // Schedule this after the last removal
    }

    public void removeMultipleCards(UUID playerID, ArrayList<ServerCard> discardedCards) {
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

    private Parent root;

    public void setRoot(Parent root) {
        this.root = root;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage stage;

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    private String gameName;

    public void switchToResults(ServerPlayer winner) {
        try {
            serverPlayers = this.client.getPlayers();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            // If gameName is not empty, proceed to switch scenes
            Alert winnerAlert = new Alert(Alert.AlertType.INFORMATION);
            Label winnerLabel = new Label("Winner: " + winner.getServerPlayerName());

            // Create a VBox to hold the winner label
            VBox vbox = new VBox(winnerLabel);

            // Create an alert dialog and set its content to the VBox

            winnerAlert.setTitle("Game Over");
            winnerAlert.setHeaderText("Game Over");
            winnerAlert.getDialogPane().setContent(vbox);

            // Show the alert
            winnerAlert.showAndWait();


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Spielauswertung.fxml"));
                root = loader.load();
                //create the ServerTable Application and TableController
                //SpielauswertungApplication spielauswertungApplication = new SpielauswertungApplication();
                SpielauswertungController spielauswertungController = loader.getController();
                spielauswertungController.Sortieren(serverPlayers); // Call Sortieren before initialize
                spielauswertungController.initialize(null, null);
                //spielauswertungController.setClient(client);
                // assign the ServerCard Grid Pane

                // set up the stage
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setTitle(gameName + "@gameId:  " + client.getGameId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void leaveGameSession(){

        client.disconnectFromServer(true);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyStage2.fxml"));
            root = loader.load();

            LobbyController2 LobbyController2 = loader.getController();

            LobbyController2.welcome.setText("Welcome " + client.getClientName());
            LobbyController2.username = client.getClientName();
            LobbyController2.setClient(this.client);


            Scene scene2 = new Scene(root, 800, 800);
            stage.setScene(scene2);
            stage.show();
            stage.setTitle("Lobby");

            // Assuming the ImageView has the fx:id="backgroundView" in your FXML file
            ImageView backgroundView = (ImageView) root.lookup("#backgroundView");

            // Ensure the image covers the entire StackPane area
            backgroundView.fitWidthProperty().bind(stage.widthProperty());
            backgroundView.fitHeightProperty().bind(stage.heightProperty());

            // Remove the preserveRatio to allow the image to cover the entire area
            backgroundView.setPreserveRatio(false);

            // Set preferred window size
            stage.setMinWidth(800); // Minimum width of the window
            stage.setMinHeight(600); // Minimum height of the window

            // Adjust the stage size after the scene is shown to ensure proper layout
            stage.sizeToScene();
            AnchorPane card = (AnchorPane) scene2.lookup("#card");
            if (card != null) {
                // Bind the card's layoutXProperty to keep it centered
                card.layoutXProperty().bind(scene2.widthProperty().subtract(card.widthProperty()).divide(2));
                // Bind the card's layoutYProperty to keep it at the same relative position from the top
                card.layoutYProperty().bind(scene2.heightProperty().multiply(599.0 / 1080.0));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}