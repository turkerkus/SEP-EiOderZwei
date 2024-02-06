package eoz.client.lobbyToTable;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sharedClasses.JoinGameControllerInterface;
import sharedClasses.LobbyRoomControllerInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public class JoinGameController implements JoinGameControllerInterface {
    public ListView gameSessionList;
    Map<String, UUID> gameSessionIds;
    @FXML
    private TextField gameIdField;
    @FXML
    private Button joinGameButton;
    private String username;
    private Parent root;
    private Stage stage;
    private UUID gameId;
    private Scene scene;
    private Client client;
    private final ObservableList<String> gameSessions = FXCollections.observableArrayList();

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

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
                boolean gameIsFull = client.isGameFull(gameId);
                System.out.println("GameExist: " + gameExists);


                if (gameExists && !gameIsFull) {
                    client.setGameId(gameId);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyRoom.fxml"));
                    root = loader.load();
                    LobbyRoomController lobbyRoomController = loader.getController();

                    // Setup lobbyRoomController
                    client.setLobbyRoomController(lobbyRoomController);
                    client.addPlayer(username, gameId);
                    lobbyRoomController.setClient(client);
                    lobbyRoomController.setGameName(client.getGameName(gameId));
                    lobbyRoomController.setUsername(username);
                    lobbyRoomController.setPlayerLabel(username + "@Player");
                    lobbyRoomController.roomName.setText(client.getGameName(gameId));
                    lobbyRoomController.gameID.setText("Game ID: " + gameId);


                    // set up the stage
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    lobbyRoomController.setStage(stage);

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    stage.setTitle("Lobby Room@_" + client.getGameName(gameId));

                } else {
                    // Show an alert if the game ID doesn't exist
                    if (gameIsFull) {
                        alert.setTitle("Joining Error");
                        alert.setHeaderText("Game Session is Full");
                        alert.setContentText("The Game Session You are trying to join is Full");
                        alert.showAndWait();
                    } else {
                        alert.setTitle("Invalid Game ID");
                        alert.setHeaderText("The entered Game ID does not exist.");
                        alert.setContentText("Please check the Game ID and try again.");
                        alert.showAndWait();
                    }


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

    public void backToLobby() {

        try {
            client.leaveLobbyRoom();
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

            Platform.runLater(() -> {
                // Lookup and position the card and background image after the new scene is rendered
                ImageView backgroundView = (ImageView) scene2.lookup("#backgroundView");
                if (backgroundView != null) {
                    backgroundView.fitWidthProperty().bind(scene2.widthProperty());
                    backgroundView.fitHeightProperty().bind(scene2.heightProperty());
                    backgroundView.setPreserveRatio(false);
                }

                AnchorPane card = (AnchorPane) scene2.lookup("#card");
                if (card != null) {
                    card.setLayoutX((scene2.getWidth() - card.getWidth()) / 2);
                    card.setLayoutY(scene2.getHeight() * 599.0 / 1080.0);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateGameSessionList() {
        // Add a listener to the scene's width property
        Platform.runLater(() -> {
            try {
                gameSessions.clear();
                gameSessionIds = client.getGameSessionIds();
                gameSessionIds.keySet().forEach(gameName -> {
                    System.out.println(gameName);
                    UUID gameId = gameSessionIds.get(gameName);
                    try {
                        System.out.println(!client.isGameReady(gameId));
                        System.out.println(!client.isGameFull(gameId));
                        if (!client.isGameReady(gameId) && !client.isGameFull(gameId)) {
                            gameSessions.add(gameName); // Add the game name to the list if the game is not ready
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });

                gameSessionList.setItems(gameSessions);
                gameSessionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Update the Game ID field with the selected game session
                        UUID gameId = gameSessionIds.get(newValue);
                        System.out.println("Joining game session: " + newValue);
                        gameIdField.setText(gameId.toString());
                    }
                });

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

    }


}