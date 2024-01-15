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



    /*
    public void startTimer() {
        if(!joiningGame){
            client.createGameSession();
            System.out.println("Game ID: "+ client.getGameId());

        }


        // Use a separate thread to update the timer
        new Thread(() -> {
            boolean gameReady = false;
            try {
                while (!gameReady) {
                    long remainingTime = client.fetchRemainingTime(); // Replace with your actual method call
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime) % 60;

                    Platform.runLater(() -> timerLabel.setText(String.format("Timer: %02d:%02d", minutes, seconds)));
                    gameReady = client.isGameReady();
                    Thread.sleep(1000); // Sleep for a second
                    if (joiningGame) {
                        setNumOfPlayers(client.getNumOfPlayers());
                        Platform.runLater(this::switchSceneToTable);
                    } else if (gameReady) {
                        Platform.runLater(this::switchSceneToTable);
                    } else {
                        Thread.sleep(1000); // Sleep for a second
                    }

                }
            } catch (InterruptedException e) {
                // Handle thread interruption
                Thread.currentThread().interrupt();
            } catch (RemoteException e) {
                // Handle RMI exceptions
                e.getMessage();
            }

        }).start();
    }

     */

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
                //create the Table Application and TableController
                tableApplication tableApplication = new tableApplication();
                tableController tableController = getTableController(loader);
                tableController.setClient(client);


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
            alert.setHeaderText("Player : " + username + " not allowed to Start Game Table");
            alert.setContentText("The host must start Game");
        }


    }


    @FXML
    private void copyGameID() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(client.getGameId().toString()); // Assuming 'copyGameID' is a Label or a control with text
        clipboard.setContent(content);
    }

    private tableController getTableController(FXMLLoader loader) throws RemoteException {
        tableController tableController = loader.getController();


        // Setup players in the tableController
        tableController.setClient(client);
        tableController.displayName(numOfPlayers);
        tableController.setMainPlayerName(username);
        try {
            // assign the Card Grid Pane
            tableController.assignCardGridPane();
        } catch (RemoteException e) {
            e.getMessage();

        }

        return tableController;
    }


}
