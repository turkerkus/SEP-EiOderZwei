module com.example.spieltischgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.spieltischgui to javafx.fxml;
    exports com.example.spieltischgui;
}