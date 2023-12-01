module teamcyan.spieltischa {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens teamcyan.spieltischa to javafx.fxml;
    exports teamcyan.spieltischa;
}