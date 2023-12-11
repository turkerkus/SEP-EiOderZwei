package eoz.client.lobbyToTable;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;


import java.io.IOException;
import java.util.Objects;

public class LobbyController {

    @FXML
    TextField usernameField;

    private Stage stage;
    private Scene scene;

    private Parent root;


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
            }
            // User clicked Cancel or closed the dialog, do nothing
        });
    }

    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(("lobbyStage.fxml"))));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene2(ActionEvent event) throws IOException {
            String username = usernameField.getText();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
            root = loader.load();

            tableController tableController = loader.getController();
            tableController.displayName(username);

           // root = FXMLLoader.load(getClass().getResource(("tableView.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("tablestyle.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();

    }

   /* @FXML
    void playGame(MouseEvent event) {
        switchToTableScene();
    }*/


}
