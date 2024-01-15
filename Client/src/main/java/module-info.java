module eoz.client.lobbyToTable {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.rmi;



    opens eoz.client.lobbyToTable to javafx.fxml;
    opens eoz.client.spielbrett to javafx.fxml;
    exports eoz.client.lobbyToTable;
    exports eoz.client.spielbrett;
    exports rmi;
    exports sharedClasses;
}