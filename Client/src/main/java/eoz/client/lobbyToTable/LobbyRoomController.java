package eoz.client.lobbyToTable;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sharedClasses.LobbyRoomControllerInterface;
import sharedClasses.ServerPlayer;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public class LobbyRoomController implements Serializable, LobbyRoomControllerInterface {


    public Button startGameSession;


    public Label playerLabel;

    public Label roomName;
    public Label gameID;
    public Button copyGameID;
    public VBox playerList;
    public Button backButton;
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
        Platform.runLater(() -> {
            this.uiUpdateFromServer = uiUpdateFromServer;
        });

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
        Platform.runLater(() -> {
            this.numOfPlayers = numOfPlayers;
        });

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void addPlayerToLobby(String playerName, Integer numOfPlayersPresent) {
        Label playerLabel = new Label(numOfPlayersPresent + ". " + playerName);
        // Customize the label if needed
        playerLabel.setFont(new Font("Arial", 16));
        Platform.runLater(() -> {
            playerList.getChildren().add(playerLabel);
        });
    }

    public void removePLayer(String isHostPlayer) {
        if ("isNotHostPlayer".equals(isHostPlayer)) {
            Platform.runLater(() -> {
                try {
                    Map<UUID, ServerPlayer> players = client.getPlayers();
                    playerList.getChildren().clear();
                    int playerCount = 1;
                    for (ServerPlayer player : players.values()) {
                        addPlayerToLobby(player.getServerPlayerName(), playerCount);
                    }

                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Host Player Left");
                alert.setHeaderText("Host Player Left the Game Session!");
                alert.setContentText("The host player has left the game, and you will be returned to the lobby.");
                alert.showAndWait();

                backToLobby();
            });
        }

    }


    public void switchSceneToTable() {
        Platform.runLater(() -> {

            if (client.startGameTable() || uiUpdateFromServer) {

                // If gameName is not empty, proceed to switch scenes
                FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
                try {
                    root = loader.load();
                    //create the ServerTable Application and TableController
                    TableApplication tableApplication = new TableApplication();
                    TableController tableController = loader.getController();
                    tableController.setClient(client);
                    tableController.setStage(this.stage);
                    tableController.setRoot(this.root);
                    tableController.setGameName(gameName);
                    try {
                        // assign the ServerCard Grid Pane
                        tableController.assignCardGridPane();
                    } catch (RemoteException e) {
                        e.getMessage();

                    }
                    // Setup players in the TableController
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
        });


    }


    @FXML
    private void copyGameID() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(client.getGameId().toString()); // Assuming 'copyGameID' is a Label or a control with text
        clipboard.setContent(content);
    }

    public void backToLobby() {
        try {

            client.leaveLobbyRoom();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyStage2.fxml"));
            root = loader.load();

            LobbyController2 lobbyController2 = loader.getController();
            lobbyController2.welcome.setText("Welcome " + username);
            lobbyController2.username = username;
            lobbyController2.setClient(this.client);

            Scene scene2 = new Scene(root, 800, 800);

            stage.setScene(scene2);

            // Set preferred window size
            stage.setMinWidth(800); // Minimum width of the window
            stage.setMinHeight(600); // Minimum height of the window

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


}
