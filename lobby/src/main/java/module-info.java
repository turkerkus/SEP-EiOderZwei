module com.teamcyan.lobby {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.teamcyan.lobby to javafx.fxml;
    exports com.teamcyan.lobby;
}