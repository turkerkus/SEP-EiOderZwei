module eoz.rmi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens eoz.rmi to javafx.fxml;
    exports eoz.rmi;
}