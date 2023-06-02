package com.example.map;

import com.example.map.MapController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("map.fxml"));
        Pane gridPane = fxmlLoader.load();
        MapController mapController = fxmlLoader.getController();
        Scene scene = new Scene(gridPane, 720,520);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case W -> mapController.movePlayerW();
                    case A -> mapController.movePlayerA();
                    case S -> mapController.movePlayerS();
                    case D -> mapController.movePlayerD();
                    case M -> mapController.calculateDistancesKeys();
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                switch (keyEvent.getCode()) {
                    case W -> mapController.stopMovePlayerW();
                    case A -> mapController.stopMovePlayerA();
                    case S -> mapController.stopMovePlayerS();
                    case D -> mapController.stopMovePlayerD();
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}