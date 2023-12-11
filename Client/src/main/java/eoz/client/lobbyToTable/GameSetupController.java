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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameSetupController {

    public ImageView backgroundView;

    @FXML
    public TextField gameName;

    public  String username;


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

    public void switchBackToScene2(ActionEvent event) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyStage2.fxml"));
            root = loader.load();

            LobbyController2 LobbyController2 = loader.getController();
            LobbyController2.welcome.setText("Welcome " + username);

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            Scene scene2 = new Scene(root,800,800);
            stage.setScene(scene2);
            stage.show();
            stage.setTitle(gameName.getText());

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
            AnchorPane card = (AnchorPane) scene2.lookup("#card");
            if (card != null) {
                // Bind the card's layoutXProperty to keep it centered
                card.layoutXProperty().bind(scene2.widthProperty().subtract(card.widthProperty()).divide(2));
                // Bind the card's layoutYProperty to keep it at the same relative position from the top
                card.layoutYProperty().bind(scene2.heightProperty().multiply(599.0 / 1080.0));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchToScene4(ActionEvent event) {


        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
            root = loader.load();

            tableController tableController = loader.getController();
            tableController.displayName(username);

            // root = FXMLLoader.load(getClass().getResource(("tableView.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            tableController.setPrimaryStage(stage);
            tableController.bindImageViewSize();
            scene = new Scene(root);
            root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("tablestyle.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
