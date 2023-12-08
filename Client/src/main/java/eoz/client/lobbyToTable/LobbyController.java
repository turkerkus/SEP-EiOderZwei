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

            TableController tableController = loader.getController();
            tableController.displayName(username);

           // root = FXMLLoader.load(getClass().getResource(("tableView.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

    }

   /* @FXML
    void createNewUser(MouseEvent event) {
        // Display a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (!Objects.equals(usernameField.getText(), "") ) {
            alert.setTitle("New User Created");
            alert.setHeaderText(null);
            alert.setContentText("Welcome " + usernameField.getText());

        }else{
            alert.setContentText("Please enter Username, !Mugu");
            alert.showAndWait();
        }


    }*/




   /* @FXML
    void playGame(MouseEvent event) {
        switchToTableScene();
    }*/

    // Helper method to start the game
  /*  private void switchToTableScene() {
        try {
            // Load the table scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
            Parent tableView = loader.load();

            // Get the current stage
            Stage stage = (Stage) sign_in.getScene().getWindow();

            stage.setScene(new Scene(tableView));

            // Pass data to the table scene if needed
            tableController controller = loader.getController();
            controller.setPlayerName(usernameField.getText());

            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // Print the exception if there's an error

        }
    }*/

}
