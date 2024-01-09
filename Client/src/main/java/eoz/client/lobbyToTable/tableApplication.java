package eoz.client.lobbyToTable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;

// Programm, um den Spieltisch für das Spiel anzuzeigen.
public class tableApplication extends Application {

    public  static int anzahlVonSpieler;

    // I will have to make some config on the slider with the number of players in the GameSetup file
    public static Spiellogik spiellogik;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
            Parent root = loader.load();

            // Retrieve the controller after loading the FXML
            tableController controller = loader.getController();



            // Setting up the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ei oder Zwei");
            primaryStage.setMinHeight(800);
            primaryStage.setMinWidth(800);
            primaryStage.show();

            initializeBind(controller, root);



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // used to bind all the necessary elements in tableController
    public  void initializeBind(tableController controller, Parent root){
        AnchorPane anchorPane2 = controller.anchorPane2;
        AnchorPane anchorPane3 = controller.anchorPane3;
        AnchorPane anchorPane4 = controller.anchorPane4;
        SplitPane splitPane2 = controller.splitPane2;
        GridPane buttonGridPane = controller.buttonGridPane;
        GridPane tableGridPane1 = controller.tableGridPane1;
        GridPane recTableGridPane = controller.recTableGridPane;
        GridPane mainCardsGridPane = controller.mainCardsGridPane;
        GridPane player1GridPane = controller.player1GridPane;
        GridPane player2GridPane = controller.player2GridPane;
        GridPane player3GridPane = controller.player3GridPane;
        GridPane player4GridPane = controller.player4GridPane;
        GridPane player5GridPane = controller.player5GridPane;
        GridPane player6GridPane = controller.player6GridPane;

        splitPane2.prefHeightProperty().bind(anchorPane2.heightProperty());
        splitPane2.prefWidthProperty().bind(anchorPane2.widthProperty());
        //anchorPane3.prefHeightProperty().bind(splitPane2.heightProperty());
        anchorPane3.prefWidthProperty().bind(splitPane2.widthProperty());
        //anchorPane3.prefHeightProperty().bind(splitPane2.heightProperty());
        anchorPane4.prefWidthProperty().bind(splitPane2.widthProperty());

        // Assuming the ImageView has the fx:id="backgroundView" in your FXML file
        ImageView backgroundView = (ImageView) root.lookup("#backgroundView");

        // Ensure the image covers the entire StackPane area
        backgroundView.fitWidthProperty().bind(anchorPane3.widthProperty());
        backgroundView.fitHeightProperty().bind(anchorPane3.heightProperty());

        // Remove the preserveRatio to allow the image to cover the entire area
        backgroundView.setPreserveRatio(false);

        buttonGridPane.prefWidthProperty().bind(anchorPane4.widthProperty());
        buttonGridPane.prefHeightProperty().bind(anchorPane4.heightProperty());
        tableGridPane1.prefWidthProperty().bind(anchorPane4.widthProperty());

        // Binding the recTable  content to the tableGridPane1
        //binding the image
        ImageView recTable = (ImageView) root.lookup("#recTable");
        RowConstraints row1Constraints = tableGridPane1.getRowConstraints().get(1);

        recTable.fitWidthProperty().bind(
                tableGridPane1.widthProperty()
        );
        recTable.fitHeightProperty().bind(
                tableGridPane1.heightProperty().multiply(row1Constraints.getPercentHeight()).divide(100)
        );

        //binding the GridPane
        recTableGridPane.prefWidthProperty().bind(
                tableGridPane1.widthProperty()
        );
        recTableGridPane.prefHeightProperty().bind(
                tableGridPane1.heightProperty().multiply(row1Constraints.getPercentHeight()).divide(100)
        );


        //binding the mainCardsGridPane to recTableGridPane
        RowConstraints row2Constraints2 = recTableGridPane.getRowConstraints().get(2);
        ColumnConstraints column2Constraints = recTableGridPane.getColumnConstraints().get(2);

        mainCardsGridPane.prefWidthProperty().bind(
                recTableGridPane.widthProperty().multiply(column2Constraints.getPercentWidth()).divide(100)
        );
        mainCardsGridPane.prefHeightProperty().bind(
                recTableGridPane.heightProperty().multiply(row2Constraints2.getPercentHeight()).divide(100)
        );

        //binding mainCard to mainCardsGridPane
        ImageView mainCard = (ImageView) root.lookup("#mainCard");
        ColumnConstraints column1Constraints2 = mainCardsGridPane.getColumnConstraints().get(1);

        mainCard.fitWidthProperty().bind(
                mainCardsGridPane.widthProperty().multiply(column1Constraints2.getPercentWidth()).divide(100)
        );
        mainCard.fitHeightProperty().bind(
                mainCardsGridPane.heightProperty()
        );

        //card distribution animation
        mainCard.setOnMouseClicked(
                event -> {
                    // add the image to the grid of player at gridPanes.get(idx)
                    controller.distributeCards();

                }
        );

        //binding playerGridPane to recTableGridPane
        //player1GridPane
        RowConstraints row1Constraints3 = recTableGridPane.getRowConstraints().get(1);
        ColumnConstraints column1Constraints = recTableGridPane.getColumnConstraints().get(1);


        player1GridPane.prefWidthProperty().bind(
                recTableGridPane.widthProperty().multiply(column1Constraints.getPercentWidth()).divide(100)
        );
        player1GridPane.prefHeightProperty().bind(
                recTableGridPane.heightProperty().multiply(row1Constraints3.getPercentHeight()).divide(100)
        );

        //player2GridPane
        player2GridPane.prefWidthProperty().bind(
                recTableGridPane.widthProperty().multiply(column1Constraints.getPercentWidth()).divide(100)
        );
        player2GridPane.prefHeightProperty().bind(
                recTableGridPane.heightProperty().multiply(row1Constraints3.getPercentHeight()).divide(100)
        );

        //player3GridPane
        player3GridPane.prefWidthProperty().bind(
                recTableGridPane.widthProperty().multiply(column1Constraints.getPercentWidth()).divide(100)
        );
        player3GridPane.prefHeightProperty().bind(
                recTableGridPane.heightProperty().multiply(row1Constraints3.getPercentHeight()).divide(100)
        );

        //player4GridPane
        player4GridPane.prefWidthProperty().bind(
                recTableGridPane.widthProperty().multiply(column1Constraints.getPercentWidth()).divide(100)
        );
        player4GridPane.prefHeightProperty().bind(
                recTableGridPane.heightProperty().multiply(row1Constraints3.getPercentHeight()).divide(100)
        );

        //player5GridPane
        player5GridPane.prefWidthProperty().bind(
                recTableGridPane.widthProperty().multiply(column1Constraints.getPercentWidth()).divide(100)
        );
        player5GridPane.prefHeightProperty().bind(
                recTableGridPane.heightProperty().multiply(row1Constraints3.getPercentHeight()).divide(100)
        );

        //player6GridPane
        player6GridPane.prefWidthProperty().bind(
                recTableGridPane.widthProperty().multiply(column1Constraints.getPercentWidth()).divide(100)
        );
        player6GridPane.prefHeightProperty().bind(
                recTableGridPane.heightProperty().multiply(row1Constraints3.getPercentHeight()).divide(100)
        );
    }


    public static void main(String[] args) {
        launch(args);
    }
}
