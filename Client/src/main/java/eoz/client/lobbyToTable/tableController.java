package eoz.client.lobbyToTable;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class tableController {
    @FXML
    Label p1;

    @FXML
    private Label p2;

    @FXML
    private Label p3;

    @FXML
    private Label p4;

    @FXML
    private ImageView myImageView;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void bindImageViewSize() {
        // Bind ImageView width to stage width
        myImageView.fitWidthProperty().bind(primaryStage.widthProperty());

        // Bind ImageView height to stage height
        myImageView.fitHeightProperty().bind(primaryStage.heightProperty());
    }

    public void displayName(String username){
        p1.setText(username);
    }


}