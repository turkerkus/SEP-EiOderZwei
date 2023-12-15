module eoz.client.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens eoz.client.client to javafx.fxml;
    opens eoz.client.lobbyToTable to javafx.fxml;
    opens eoz.client.spielbrett to javafx.fxml;
    exports eoz.client.client;
    exports eoz.client.lobbyToTable;
    exports eoz.client.spielbrett;
}