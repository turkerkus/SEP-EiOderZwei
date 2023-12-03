module eoz.client.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens eoz.client.client to javafx.fxml;
    opens eoz.client.lobby to javafx.fxml;
    opens eoz.client.spielbrett to javafx.fxml;
    opens eoz.client.spieltischa to javafx.fxml;
    exports eoz.client.client;
    exports eoz.client.lobby;
    exports eoz.client.spielbrett;
    exports eoz.client.spieltischa;
}