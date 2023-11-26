package com.teamcyan.lobby;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LobbyController {

    @FXML
    private AnchorPane card;

    @FXML
    private ImageView backgroundView;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button sign_in;

    @FXML
    private Button exit;

    @FXML
    private Button guest;

    @FXML
    void createNewUser(MouseEvent event) {

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

    }

}
