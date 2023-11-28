package teamcyan.spielbrett.spielbrett;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ImageView im;

    @FXML
    private FlowPane playerhand;
    double xasis;
    double yaxsis;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerhand.setHgap(15); // gap bestimmen
        playerhand.setVgap(15); // gap bestimmen
        playerhand.setPadding(new Insets(15, 15, 15, 15)); // zwischen Karten gap bestimmen
    }

    @FXML
    void maus(MouseEvent event) {
        ImageView img = new ImageView(getClass().getResource("/image/eie.jpg").toExternalForm());
        img.setFitHeight(75);
        img.setFitWidth(55);
        playerhand.getChildren().add(img);
        playerhand.layout();

        img.setOnMousePressed(mouseEvent1 -> {
            xasis = mouseEvent1.getSceneX() - img.getTranslateX(); // Lokation von X-Achse der Karte
            yaxsis = mouseEvent1.getSceneY() - img.getTranslateY(); // Lokation von Y-Achse der Karte
        });

        img.setOnMouseDragged(mouseEvent1 -> {
            img.setTranslateX(mouseEvent1.getSceneX() - xasis);
            img.setTranslateY(mouseEvent1.getSceneY() - yaxsis);
        });
    }
}