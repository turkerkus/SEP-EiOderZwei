package eoz.client.lobbyToTable;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class tableController {

    /** Chat Vars **/
    @FXML
    TextField input = new TextField();
    @FXML
    public ScrollPane scroll = new ScrollPane();
    @FXML
    public VBox messagesBox = new VBox();
    @FXML
    Button sendButton = new Button("Senden");
    @FXML
    VBox background = new VBox();

    public static String uID = UUID.randomUUID().toString();
    /**Chat Vars end**/

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



   public void onEnter(){
       sendButton.setOnAction(actionEvent -> {
           send(input.getText(), messagesBox);
       });
    }

    void send(String mes, VBox messagesBox){
           messagesBox.getChildren().add(new javafx.scene.control.Label(mes));

       input.clear();
    }




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