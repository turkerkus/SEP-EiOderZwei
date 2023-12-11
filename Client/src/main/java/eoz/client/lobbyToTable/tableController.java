package eoz.client.lobbyToTable;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class tableController {
    @FXML
    private Label p1;

    @FXML
    private Label p2;

    @FXML
    private Label p3;

    @FXML
    private Label p4;
    @FXML
    private Label p5;
    @FXML
    private Label p6;

    @FXML
    private ImageView myImageView;
    private Stage primaryStage;

    @FXML
    private ImageView im;

    @FXML
    private FlowPane playerhand;
    double xAchse;
    double yAchse;

    @FXML
    void maus(MouseEvent event) {// Wenn auf das Deck geklickt wird, löst sich das folgende Ereignis aus.
        ImageView img = new ImageView(getClass().getResource("eie.jpg").toExternalForm());//Dummy Kart als imageView erstellen
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

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void bindImageViewSize() {
        // Bind ImageView width to stage width
        myImageView.fitWidthProperty().bind(primaryStage.widthProperty());

        // Bind ImageView height to stage height
        myImageView.fitHeightProperty().bind(primaryStage.heightProperty());
    }

    public void displayName(String username, double numOfPlayers){
        p1.setText(username);

        switch ((int) numOfPlayers){
            case 1:
                p2.setText("Bot 1");
                p3.setText("Bot 2");
                p4.setText("Bot 3");
                p5.setText("Bot 4");
                p6.setText("Bot 5");
                break;
            case 2:
                p3.setText("Bot 1");
                p4.setText("Bot 2");
                p5.setText("Bot 3");
                p6.setText("Bot 4");
                break;
            case 3:
                p4.setText("Bot 1");
                p5.setText("Bot 2");
                p6.setText("Bot 3");
                break;
            case 4:
                p5.setText("Bot 1");
                p6.setText("Bot 2");
                break;
            case 5:
                p6.setText("Bot 1");
                break;
        }
    }


}