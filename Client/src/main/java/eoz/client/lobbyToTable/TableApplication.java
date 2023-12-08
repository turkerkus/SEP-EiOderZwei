package eoz.client.lobbyToTable;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

// Programm, um den Spieltisch für das Spiel anzuzeigen.
public class TableApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Die Bilder werden aus dem Ressources Ordner importiert und ihre Größe wird verändert.
        // Durch StackPane sind die Bilder zentriert postioniert.
        primaryStage.setTitle("Ei oder Zwei!");
       // primaryStage.setMaxHeight(1440);
        primaryStage.setMinHeight(600);
        //primaryStage.setMaxWidth(2560);
        primaryStage.setMinWidth(800);
        //primaryStage.setFullScreen(true);
        //primaryStage.setFullScreenExitHint("");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
        Parent root = loader.load();

        // Accessing controller and setting the primary stage
        TableController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

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