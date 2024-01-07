package eoz.client.lobbyToTable;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

public class SpielauswertungController implements Initializable {
    @FXML
    private TableColumn<Integer, Integer> platz;

    @FXML
    private TableColumn<Spieler, Integer> score;

    @FXML
    private TableColumn<Spieler, String> spieler;

    @FXML
    private TableView<Spieler> table; // Use Spieler type for TableView

    ObservableList<Spieler> data = FXCollections.observableArrayList(
            new Spieler(1, "spieler1", 0, 0, new ArrayList<>(), false,0),
            new Spieler(2, "spieler2", 1, 0, new ArrayList<>(), false,0),
            new Spieler(3, "spieler3", 2, 0, new ArrayList<>(), false,0)
    );
    public void initialize(URL url, ResourceBundle rb){
        data.sort((spieler1, spieler2) -> Integer.compare(spieler2.getPunkte(), spieler1.getPunkte()));
        int i = 1;
        for(Spieler sp: data){
            sp.setRank(i);
            i += 1;
        }
        // Associate TableColumn with Spieler class properties
        platz.setCellValueFactory(new PropertyValueFactory<>("rank"));
        spieler.setCellValueFactory(new PropertyValueFactory<>("name"));
        score.setCellValueFactory(new PropertyValueFactory<>("punkte"));

        // Set the data to the table
        table.setItems(data);
        /*
        table.getSortOrder().add(score);
        table.sort();
         */
    }
}
