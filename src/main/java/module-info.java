module com.example.map {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.map to javafx.fxml;
    exports com.example.map;
    exports com.example.model;
    opens com.example.model to javafx.fxml;

}