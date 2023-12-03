package eoz.client.lobby;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class LobbyController {

    @FXML
    private AnchorPane card;

    @FXML
    private ImageView backgroundView;

    @FXML
    private TextField usernameField;

    @FXML
    private Button sign_in;

    @FXML
    private Button exit;

    @FXML
    private Button guest;

    @FXML
    void createNewUser(MouseEvent event) {
        // Display a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (!Objects.equals(usernameField.getText(), "") ) {
            alert.setTitle("New User Created");
            alert.setHeaderText(null);
            alert.setContentText("Welcome " + usernameField.getText());
            alert.showAndWait();

        }else{
            alert.setContentText("Please enter Username, !Mugu");
            alert.showAndWait();
        }
        // I will add the logic that puts the player´s name on the table here
    }


    @FXML
    void exitGame(MouseEvent event) {
        // Display a confirmation dialog before exiting the game
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Game");
        alert.setHeaderText("Are you sure you want to exit the game?");
        alert.setContentText("Any unsaved progress may be lost.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User clicked OK, exit the game gracefully
                Platform.exit();
            } else {
                // User clicked Cancel or closed the dialog, do nothing
            }
        });

    }

    @FXML
    void playGame(MouseEvent event) {
        startGame();
    }

    // Helper method to start the game
    private void startGame() {
        // Should display a message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Started");
        alert.setHeaderText(null);
        alert.setContentText("The game is starting!");
        alert.showAndWait();
        // I will add logic to take the player to the game table
    }

}
