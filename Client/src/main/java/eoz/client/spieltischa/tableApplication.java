package eoz.client.spieltischa;

import eoz.client.lobby.UsernameHolder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

// Programm, um den Spieltisch für das Spiel anzuzeigen.
public class tableApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Die Bilder werden aus dem Resources Ordner importiert und ihre Größe wird verändert.
        // Durch StackPane sind die Bilder zentriert positioniert.
        primaryStage.setTitle("Ei oder Zwei!");
        primaryStage.setMaxHeight(1440);
        primaryStage.setMinHeight(1000);
        primaryStage.setMaxWidth(2560);
        primaryStage.setMinWidth(1000);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        // Accessing controller and setting the primary stage
        tableController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        // Set the username in the label
        controller.setPlayerName(UsernameHolder.getUsername());


        // Setting up the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("tablestyle.css").toExternalForm());
        // Binding ImageView size to stage size
        controller.bindImageViewSize();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }}