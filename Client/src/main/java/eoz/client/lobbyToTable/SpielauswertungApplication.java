package eoz.client.lobbyToTable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SpielauswertungApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Spielauswertung.fxml")));
        Scene scene1 = new Scene(root,1000,600);
        primaryStage.setScene(scene1);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setTitle("Spielauswertung");
    }
}
