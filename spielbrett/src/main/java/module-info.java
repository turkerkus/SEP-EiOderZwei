module teamcyan.spielbrett.spielbrett {
    requires javafx.controls;
    requires javafx.fxml;


    opens teamcyan.spielbrett.spielbrett to javafx.fxml;
    exports teamcyan.spielbrett.spielbrett;
}