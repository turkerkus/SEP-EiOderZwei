module eoz.server.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens eoz.server.server to javafx.fxml;
    exports eoz.server.server;
}