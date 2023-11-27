package teamcyan.spielbrett.spielbrett;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button but;


    @FXML
    private ImageView im;
    @FXML
    ImageView im1;
    @FXML
    private FlowPane playerhand;
    @FXML
    private AnchorPane ply1;
    private  double xasis;
    private double yaxsis;


    @FXML
    private Label welcomeText;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        playerhand.setHgap(15); // gap bestimmen
        playerhand.setHgap(15); // gap bestimmen
        playerhand.setVgap(15); // gap bestimmen
        playerhand.setPadding(new Insets(15, 15, 15, 15)); // zwischen karten gap bestimmen
        im.setOnMouseClicked(mouseEvent -> {
            ImageView img= new ImageView(getClass().getResource("/image/eie.jpg").toExternalForm());
            img.setFitHeight(50);
            img.setFitWidth(35);
            playerhand.getChildren().add(img);
            playerhand.layout();
            img.setOnMousePressed(mouseEvent1 -> {
                xasis = mouseEvent1.getSceneX() - img.getTranslateX(); //Lokation von X Achse von Card
                yaxsis = mouseEvent1.getSceneY() - img.getTranslateY(); //Lokation von Y Achse von Card
            });
            img.setOnMouseDragged(mouseEvent1 -> {
                img.setTranslateX(mouseEvent1.getSceneX()-xasis);
                img.setTranslateY(mouseEvent1.getSceneY()-yaxsis);

            });

        });



    }}