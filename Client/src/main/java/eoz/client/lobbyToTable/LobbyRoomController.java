package eoz.client.lobbyToTable;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

public class LobbyRoomController implements Serializable {


    public Button startGameSession;


    public Label playerLabel;

    public Label roomName;
    public Label gameID;
    public Button copyGameID;
    private Client client;
    private Integer numOfPlayers;
    private String username;
    private String gameName;

    private Parent root;


    private boolean uiUpdateFromServer;


    private Stage stage;

    public void initialize() {

    }

    public void setUiUpdateFromServer(boolean uiUpdateFromServer) {
        this.uiUpdateFromServer = uiUpdateFromServer;
    }

    public void setPlayerLabel(String playerLabel) {
        this.playerLabel.setText(playerLabel);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setNumOfPlayers(Integer numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void switchSceneToTable() {
        if (client.startGameTable() || uiUpdateFromServer) {

            // If gameName is not empty, proceed to switch scenes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
            try {
                root = loader.load();
                //create the ServerTable Application and TableController
                tableApplication tableApplication = new tableApplication();
                tableController tableController = loader.getController();
                tableController.setClient(client);
                try {
                    // assign the ServerCard Grid Pane
                    tableController.assignCardGridPane();
                } catch (RemoteException e) {
                    e.getMessage();

                }
                // Setup players in the tableController
                client.setTableController(tableController);


                // set up the stage
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setTitle(gameName + "@gameId:  " + client.getGameId());

                // bind the elements of the table to the window
                tableApplication.initializeBind(tableController, root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Starting Game Session");
            alert.setHeaderText("Player : " + username + " not allowed to Start Game ServerTable");
            alert.setContentText("The host must start Game");
            alert.show();
        }


    }


    @FXML
    private void copyGameID() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(client.getGameId().toString()); // Assuming 'copyGameID' is a Label or a control with text
        clipboard.setContent(content);
    }



}
