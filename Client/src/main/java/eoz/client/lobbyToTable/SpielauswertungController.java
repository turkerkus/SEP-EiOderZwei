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
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rmi.Server;
import sharedClasses.ServerPlayer;
import sharedClasses.ServerTable;

import java.rmi.RemoteException;
import java.util.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class SpielauswertungController implements Initializable {
    private Stage primaryStage; // Variable zur Speicherung der PrimaryStage

    // Methode zum Setzen der PrimaryStage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private Map<UUID, ServerPlayer> serverPlayers;

    public void setClient(Client client) {
        this.client = client;
    }

    private Client client;

    @FXML
    private TableColumn<Integer, Integer>platz;
    @FXML
    private TableColumn<ServerPlayer, Integer> punkte;

    @FXML
    private TableColumn<ServerPlayer, String> spieler;

    @FXML
    private TableView<ServerPlayer> table; // Use Spieler type for TableView
    private ArrayList<ServerPlayer> spie = new ArrayList<>();

    //Eine Funktion um bei Knopfdruck zurück zur Lobby zu gelangen.
    @FXML
    void backtolobby() {
        if (primaryStage != null) {
            primaryStage.close();
        }
        LobbyApplication backlobby = new LobbyApplication();
        Stage stage = new Stage();
        backlobby.start(stage);

    }
    /*
    public void getPlayers() {
        try {
            serverPlayers = this.client.getPlayers();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Convert the map entries to a list of Map.Entry
        List<Map.Entry<UUID, ServerPlayer>> entryList = new ArrayList<>(serverPlayers.entrySet());

        // Sort the list in descending order based on ServerPlayer's getPunkte()
        entryList.sort(Comparator.comparing(entry -> entry.getValue().getPunkte(), Comparator.reverseOrder()));

        // Create a new LinkedHashMap to maintain the order
        serverPlayers  = entryList.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));



        // Now, sortedMap contains the serverPlayers sorted by getPunkte() in descending order
        for (Map.Entry<UUID, ServerPlayer> entry : serverPlayers.entrySet()) {
            spie.add(entry.getValue());
        }
    }
    ObservableList<ServerPlayer> data = observableArrayList(spie);
    */
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID id3 = UUID.randomUUID();

    ObservableList<ServerPlayer> data = FXCollections.observableArrayList(
            new ServerPlayer(id1, "test1", false),
            new ServerPlayer(id2, "test2", false),
            new ServerPlayer(id3, "test3", false)
        );
    public void initialize(URL url, ResourceBundle rb) {
        int i = 1;
        for(ServerPlayer sp: data){
            sp.setRank(i);
            i += 1;
        }
// Associate TableColumn with Spieler class properties
        platz.setCellValueFactory(new PropertyValueFactory<>("rank"));
        spieler.setCellValueFactory(new PropertyValueFactory<>("serverPlayerName"));
        punkte.setCellValueFactory(new PropertyValueFactory<>("punkte"));

        // Set the data to the table
        table.setItems(data);
    }
}
