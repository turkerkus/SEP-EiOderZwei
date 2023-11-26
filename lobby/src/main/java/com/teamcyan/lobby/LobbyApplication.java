package com.teamcyan.lobby;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LobbyApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyStage.fxml"));
        Parent root = loader.load();

        // Assuming the ImageView has the fx:id="backgroundView" in your FXML file
        ImageView backgroundView = (ImageView) root.lookup("#backgroundView");

        // Ensure the image covers the entire StackPane area
        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());

        // Remove the preserveRatio to allow the image to cover the entire area
        backgroundView.setPreserveRatio(false);

        // Create the Scene with the loaded FXML root
        Scene scene = new Scene(root);

        // Set and show the Stage
        primaryStage.setTitle("Lobby");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Adjust the stage size after the scene is shown to ensure proper layout
        primaryStage.sizeToScene();
        AnchorPane card = (AnchorPane) scene.lookup("#card");
        if (card != null) {
            // Bind the card's layoutXProperty to keep it centered
            card.layoutXProperty().bind(scene.widthProperty().subtract(card.widthProperty()).divide(2));
            // Bind the card's layoutYProperty to keep it at the same relative position from the top
            card.layoutYProperty().bind(scene.heightProperty().multiply(599.0 / 1080.0));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
