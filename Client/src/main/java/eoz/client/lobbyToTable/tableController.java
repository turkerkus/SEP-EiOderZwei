package eoz.client.lobbyToTable;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import rmi.ChatController;
import rmi.ChatObserver;
import rmi.ChatObserverImpl;



import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;


public class tableController implements Initializable, ChatObserver {

    /**
     * Chat Vars
     **/
    private ChatObserver chatObserver;
    private ChatController controller;
    @FXML
    TextFlow emojiList;
    @FXML
    Button btnSend;
    @FXML
    Button btnEmoji;
    @FXML
    VBox chatBox;
    @FXML
    ScrollPane scrollPane;
    @FXML
    TextArea txtMsg;
    @FXML
    public AnchorPane chatPane;

    private String username="user";
    /**
     * Chat Vars end
     **/

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

    public void setUsername(String username){
        this.username=username;
        System.out.println(this.username);
    }

    public void initialize(URL location, ResourceBundle resources) {
        gridPanes.add(player1GridPane);
        gridPanes.add(player2GridPane);
        gridPanes.add(player3GridPane);
        gridPanes.add(player4GridPane);
        gridPanes.add(player5GridPane);
        gridPanes.add(player6GridPane);

        for(Node text : emojiList.getChildren()){
            text.setOnMouseClicked(event -> {
                txtMsg.setText(txtMsg.getText()+" "+((Text)text).getText());
                emojiList.setVisible(false);
            });
        }
        scrollPane.vvalueProperty().bind(chatBox.heightProperty());
        try{
            chatObserver = new ChatObserverImpl(this);
            //controller = ServerConnector.getServerConnector().getController();
            //controller.addChatObserver(chatObserver);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    Integer gridPanesIdx = 0;

    public void sendAction(javafx.event.ActionEvent actionEvent) {
        try {
            if(txtMsg.getText().trim().isEmpty())return;
            controller.notifyAllClients(username,txtMsg.getText().trim());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        txtMsg.setText("");
        txtMsg.requestFocus();
    }

    public void emojiAction(javafx.event.ActionEvent actionEvent) {
        emojiList.setVisible(!emojiList.isVisible());
    }

    @Override
    public boolean update(String username, String message) throws RemoteException {
        Text text = new Text(message);

        text.setFill(Color.WHITE);
        text.getStyleClass().add("message");
        TextFlow tempFlow = new TextFlow();
        if(!this.username.equals(username)){
            Text txtName = new Text(username + "\n");
            txtName.getStyleClass().add("txtName");
            tempFlow.getChildren().add(txtName);

        }
        tempFlow.getChildren().add(text);
        tempFlow.setMaxWidth(200);

        TextFlow flow = new TextFlow(tempFlow);
        HBox hbox = new HBox(12);

        if(!this.username.equals(username)){

            tempFlow.getStyleClass().add("tempFlowFlipped");
            flow.getStyleClass().add("textFlowFlipped");
            chatBox.setAlignment(Pos.TOP_LEFT);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().add(flow);

        }else{
            text.setFill(Color.WHITE);
            tempFlow.getStyleClass().add("tempFlow");
            flow.getStyleClass().add("textFlow");
            hbox.setAlignment(Pos.BOTTOM_RIGHT);
            hbox.getChildren().add(flow);
        }

        hbox.getStyleClass().add("hbox");
        Platform.runLater(() -> chatBox.getChildren().addAll(hbox));


        return true;
    }

    @Override
    public ArrayList<Spieler> getOnlineUsers() throws RemoteException {
        return null;
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }


    public ChatObserver getChatObserver(){
        return chatObserver;
    }


  /* public void sendMessage(){
       String messageText = input.getText().trim();
       if(!messageText.isEmpty()){
           String senderID = messageText.split(":")[0].trim();
           MessageLabel messageLabel = new MessageLabel(uID + ": " + messageText);

           // Sets my messages to the right and received messages to the left
           if (senderID.startsWith(uID)){
               messageLabel.setAlignment(Pos.BASELINE_RIGHT);
               messageLabel.getStyleClass().add("sent-message");

           } else {
               messageLabel.setAlignment(Pos.BASELINE_LEFT);
               messageLabel.getStyleClass().add("received-message");
           }

           messagesBox.getChildren().add(messageLabel);

           input.clear(); //clears the textField after I send my message



           scroll.setVvalue(1.0); // auto scroll to newest messages
       }
   }

   private static class MessageLabel extends javafx.scene.control.Label{
       public MessageLabel(String message){
           super(message);
           setWrapText(true);
           setMaxWidth(200);
       }
   }*/


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
            gridPanesIdx = (gridPanesIdx) % gridPanes.size();
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


    public void displayName(String username, double numOfPlayers) {
        p1.setText(username);

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