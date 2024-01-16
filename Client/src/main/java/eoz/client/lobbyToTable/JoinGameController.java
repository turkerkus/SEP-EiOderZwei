package eoz.client.lobbyToTable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class JoinGameController {
    @FXML
    private TextField gameIdField;

    @FXML
    private Button joinGameButton;

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private Parent root;
    private Stage stage;
    private UUID gameId;



    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }
    @FXML
    private void joinGame(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        gameId = UUID.fromString(gameIdField.getText().trim()); // Get the entered game ID
        System.out.println("Game ID  @ " + gameId);
        try {
            // Create a client
            // Attempt to connect to the server
            if (client.isConnectedToServer) {
                // Check if the game ID exists (you'll need to implement this logic)
                boolean gameExists = client.doesGameExist(gameId);
                System.out.println("GameExist: "+ gameExists);


                if (gameExists) {
                    client.setGameId(gameId);
                    client.addPlayer(username,gameId);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyRoom.fxml"));
                    root = loader.load();
                    LobbyRoomController lobbyRoomController = loader.getController();

                    // Setup lobbyRoomController
                    lobbyRoomController.setClient(client);
                    lobbyRoomController.setGameName(client.getGameName(gameId));
                    lobbyRoomController.setUsername(username);
                    lobbyRoomController.setPlayerLabel(username + "@Player");
                    lobbyRoomController.roomName.setText(client.getGameName(gameId));
                    lobbyRoomController.gameID.setText("Game ID: " + gameId);


                    // set up the stage
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    lobbyRoomController.setStage(stage);
                    client.setLobbyRoomController(lobbyRoomController);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    stage.setTitle("Lobby Room@_"+ client.getGameName(gameId));

                } else {
                    // Show an alert if the game ID doesn't exist

                    alert.setTitle("Invalid Game ID");
                    alert.setHeaderText("The entered Game ID does not exist.");
                    alert.setContentText("Please check the Game ID and try again.");
                    alert.showAndWait();
                }

            } else {
                // Show an alert indicating connection failure
                alert.setTitle("Connection Error");
                alert.setHeaderText("Failed to connect to the server.");
                alert.setContentText("Please check your network connection and try again.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}