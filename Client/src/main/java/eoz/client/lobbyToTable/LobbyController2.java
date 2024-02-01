package eoz.client.lobbyToTable;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LobbyController2 {

    public ImageView backgroundView;
    @FXML
    Label welcome;



    private Stage stage;
    private Scene scene;

    private Parent root;

    public String username;


    private Client client;

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
    public void setClient(Client client) {
        this.client = client;
    }


    public void switchBackToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(("lobbyStage2.fxml"))));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene3(ActionEvent event) {



        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameSetup.fxml"));
            root = loader.load();

            GameSetupController gameSetupController = loader.getController();
            gameSetupController.username = username;
            gameSetupController.setClient(this.client);

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            Scene scene3 = new Scene(root,800,800);
            stage.setScene(scene3);
            stage.show();
            stage.setTitle("Lobby");

            // Assuming the ImageView has the fx:id="backgroundView" in your FXML file
            ImageView backgroundView = (ImageView) root.lookup("#backgroundView");

            // Ensure the image covers the entire StackPane area
            backgroundView.fitWidthProperty().bind(stage.widthProperty());
            backgroundView.fitHeightProperty().bind(stage.heightProperty());

            // Remove the preserveRatio to allow the image to cover the entire area
            backgroundView.setPreserveRatio(false);

            // Set preferred window size
            stage.setMinWidth(800); // Minimum width of the window
            stage.setMinHeight(600); // Minimum height of the window

            // Adjust the stage size after the scene is shown to ensure proper layout
            stage.sizeToScene();
            AnchorPane card = (AnchorPane) scene3.lookup("#card");
            if (card != null) {
                // Bind the card's layoutXProperty to keep it centered
                card.layoutXProperty().bind(scene3.widthProperty().subtract(card.widthProperty()).divide(2));
                // Bind the card's layoutYProperty to keep it at the same relative position from the top
                card.layoutYProperty().bind(scene3.heightProperty().multiply(480.0 / 1080.0));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void joinGameRoom (ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("joinGame.fxml"));
            root = loader.load();

            JoinGameController joinGameController = loader.getController();
            joinGameController.setUsername(username);
            joinGameController.setClient(this.client);
            joinGameController.setStage(stage);


            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            joinGameController.setStage(stage);

            Scene scene3 = new Scene(root,800,800);
            stage.setScene(scene3);
            stage.show();
            stage.setTitle("Join Game");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
