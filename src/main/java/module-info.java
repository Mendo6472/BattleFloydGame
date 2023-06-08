module com.example.map {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.map to javafx.fxml, javafx.media;
    exports com.example.map;
    exports com.example.model;
    opens com.example.model to javafx.fxml, javafx.media;

}