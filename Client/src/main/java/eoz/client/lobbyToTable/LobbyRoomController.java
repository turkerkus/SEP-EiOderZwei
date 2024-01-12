package eoz.client.lobbyToTable;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import rmi.Client;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class LobbyRoomController {


    public Label timerLabel;
    private Client client;
    private Integer numOfPlayers;
    private String username;
    private String gameName;
    private Parent root;



    private Stage stage;

    public void initialize() {

    }
    public void startTimer(){
        client.createGameSession();

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
                    if (gameReady) {
                        // Close the dialog and proceed
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

    private void switchSceneToTable() {
        // Close the dialog and proceed
        Platform.runLater(() -> {

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


        });
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
