package teamcyan.spielbrett.spielbrett;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ei oder Zwei ");
        stage.setMaxHeight(1440);
        stage.setMinHeight(1000);
        stage.setMaxWidth(2560);
        stage.setMinWidth(1000);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}