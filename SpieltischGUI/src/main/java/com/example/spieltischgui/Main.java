package com.example.spieltischgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane stackPane = new StackPane();
        Image image = new Image(getClass().getResourceAsStream("background.png"));
        Image image2 = new Image(getClass().getResourceAsStream("tisch.png"));
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView = new ImageView(image);

        imageView2.setFitHeight(1200);
        imageView2.setFitWidth(1200);
        imageView2.setPreserveRatio(true);

        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());

        imageView.setPreserveRatio(false);

        stackPane.getChildren().addAll(imageView, imageView2);

        Scene scene = new Scene(stackPane);

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
    }
}