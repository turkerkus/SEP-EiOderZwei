package eoz.client.lobbyToTable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class LobbyApplication extends Application {

    private Client client ;

    @Override
    public void start(Stage stage) {
        this.client =  new Client();
        try {
            // Set and show the Stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyStage.fxml"));
            Parent root = loader.load();
            LobbyController lobbyController = loader.getController();
            lobbyController.setClient(this.client);

            Scene scene1 = new Scene(root,800,800);
            stage.setScene(scene1);
            stage.show();
            stage.setTitle("Lobby");
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("eiicon.png"))));


            // The ImageView has the fx:id="backgroundView" in my FXML file
            ImageView backgroundView = (ImageView) root.lookup("#backgroundView");

            // This ensures the image covers the entire StackPane area
            backgroundView.fitWidthProperty().bind(stage.widthProperty());
            backgroundView.fitHeightProperty().bind(stage.heightProperty());

            // This removes the preserveRatio to allow the image to cover the entire area
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
    @Override
    public void stop() {
        try {
            // Close RMI connections or other network connections
            if (client != null) {
                client.disconnectFromServer(false); // Ensure this method closes all RMI resources
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        } finally {
            // Other cleanup if necessary

            Platform.exit(); // Ensure JavaFX thread is terminated
            System.exit(0);  // Force JVM shutdown (use cautiously)
        }
    }



    public static void main (String[]args){
        launch(args);
    }

}
