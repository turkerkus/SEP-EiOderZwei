module eoz.client.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens eoz.client.client to javafx.fxml;
    exports eoz.client.client;
}