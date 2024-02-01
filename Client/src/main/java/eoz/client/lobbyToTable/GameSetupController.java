package eoz.client.lobbyToTable;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameSetupController implements Initializable {

    public ImageView backgroundView;
    @FXML
    public TextField gameName;
    @FXML
    private Slider numOfPlayers;
    @FXML
    private ChoiceBox<String> botDifficulty;
    ObservableList<String> botOptions = FXCollections.observableArrayList("Easy", "Hard", "Random");
    public String username;


    private Stage stage;

    private Parent root;

    public void setClient(Client client) {
        this.client = client;
    }

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final int maxLength = 32;

        gameName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                String limitedText = newValue.substring(0, maxLength);
                gameName.setText(limitedText);
            }
        });
        botDifficulty.setValue("Hard"); // it has to be an element in the ObservableList
        botDifficulty.setItems(botOptions);
    }


    public void switchBackToScene2(ActionEvent event) {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyStage2.fxml"));
            root = loader.load();

            LobbyController2 lobbyController2 = loader.getController();
            lobbyController2.welcome.setText("Welcome " + username);
            lobbyController2.username = username;
            lobbyController2.setClient(this.client);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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


    public void switchToScene4(ActionEvent event) {
        // Show alert if the game name is empty
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Check if gameName TextField is empty
        if (gameName.getText().isEmpty()) {
            alert.setTitle("Invalid Game Name");
            alert.setHeaderText("You have to set the Game Name!");
            alert.setContentText("You cannot leave the Game name empty.");
            alert.showAndWait();
        } else {
            try {


                // Attempt to connect to the server
                if (client.isConnectedToServer) {
                    client.setGameName(gameName.getText());
                    client.setNumOfPlayers((int) numOfPlayers.getValue());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyRoom.fxml"));
                    root = loader.load();
                    LobbyRoomController lobbyRoomController = loader.getController();

                    // Setup lobbyRoomController
                    lobbyRoomController.setClient(client);
                    client.setLobbyRoomController(lobbyRoomController);
                    client.createGameSession();
                    System.out.println("Game Id : " + client.getGameId());
                    lobbyRoomController.setGameName(gameName.getText());
                    lobbyRoomController.setNumOfPlayers((int) numOfPlayers.getValue());
                    lobbyRoomController.setUsername(username);
                    lobbyRoomController.setPlayerLabel(username + "@Host");
                    lobbyRoomController.roomName.setText("Room :" + gameName.getText());
                    lobbyRoomController.gameID.setText("Game ID: " + client.getGameId());


                    // set up the stage
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    lobbyRoomController.setStage(stage);

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    stage.setTitle("Lobby Room@_" + gameName.getText());
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
}