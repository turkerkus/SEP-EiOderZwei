package eoz.client.spieltischa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
// Programm, um den Spieltisch für das Spiel anzuzeigen.
public class table extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Die Bilder werden aus dem Ressources Ordner importiert und ihre Größe wird verändert.
        // Durch StackPane sind die Bilder zentriert postioniert.
        StackPane stackPane = new StackPane();
        Image image = new Image(getClass().getResourceAsStream("/images/bg.png"));
        Image image2 = new Image(getClass().getResourceAsStream("/images/table.png"));
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView = new ImageView(image);
        imageView2.setFitHeight(1200);
        imageView2.setFitWidth(1200);
        imageView2.setPreserveRatio(true);
        //Das Hintergrundbild wird an die Fenstergröße angepasst.
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());

        imageView.setPreserveRatio(false);

        stackPane.getChildren().addAll(imageView, imageView2);

        Scene scene = new Scene(stackPane);
        // Hier werden Beschränkungen für Fenstergröße und der Titelname gesetzt.
        primaryStage.setTitle("Ei oder Zwei!");
        primaryStage.setMaxHeight(1440);
        primaryStage.setMinHeight(1000);
        primaryStage.setMaxWidth(2560);
        primaryStage.setMinWidth(1000);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }}