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
    double xAchse;
    double yAchse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerhand.setHgap(15);
        playerhand.setVgap(15);
        playerhand.setPadding(new Insets(15, 15, 15, 15)); //Platzierung,Abstand
        // Bestimmen des Abstands zwischen den Karten und Festlegen, wie die neuen Karten hinzugefügt werden sollen
    }

    @FXML
    void maus(MouseEvent event) {// Wenn auf das Deck geklickt wird, löst sich das folgende Ereignis aus.
        ImageView img = new ImageView(getClass().getResource("/image/eie.jpg").toExternalForm());//Dummy Kart als imageView erstellen
        img.setFitHeight(75); //Höhe von Dummy Karte
        img.setFitWidth(55); //Breite von Dummy Karte
        playerhand.getChildren().add(img); // Die Karte wird an die Spielerhand gesendet
        playerhand.layout();

        img.setOnMousePressed(mouseEvent1 -> {//Es wird angerufen ,wenn man die Maustaste auf die Karte drückt
            xAchse= mouseEvent1.getSceneX() - img.getTranslateX(); //Speicherung der Anfangsposition der X-Achse der Karte
            yAchse = mouseEvent1.getSceneY() - img.getTranslateY(); //Speicherung der Anfangsposition der Y-Achse der Karte
        });

        img.setOnMouseDragged(mouseEvent1 -> {//-Verschiebung-Es wird angerufen,wenn die Maus bewegt wird
            img.setTranslateX(mouseEvent1.getSceneX() - xAchse);// Bestimmung der neuen Position des Bildes unter Berücksichtigung der Differenz
            // zwischen der aktuellen Mausposition und der ursprünglichen Position der Karte für die X- und Y-Achse.
            img.setTranslateY(mouseEvent1.getSceneY() - yAchse);
        });
    }
}