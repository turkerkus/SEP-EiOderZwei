package eoz.client.lobbyToTable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LobbyApplication extends Application {

    @Override
    public void start(Stage stage) {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyStage.fxml"));
        //Parent root = loader.load();
        try {
            // Set and show the Stage
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lobbyStage.fxml")));
            Scene scene1 = new Scene(root);
            stage.setScene(scene1);
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
            AnchorPane card = (AnchorPane) scene1.lookup("#card");
            if (card != null) {
                // Bind the card's layoutXProperty to keep it centered
                card.layoutXProperty().bind(scene1.widthProperty().subtract(card.widthProperty()).divide(2));
                // Bind the card's layoutYProperty to keep it at the same relative position from the top
                card.layoutYProperty().bind(scene1.heightProperty().multiply(599.0 / 1080.0));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public static void main (String[]args){
            launch(args);
        }

}
