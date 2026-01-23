package agh.ics.oop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primary_stage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("start.fxml"));
        BorderPane view_root = loader.load();
        configureStage(primary_stage,view_root);
        primary_stage.show();

    }

    private void configureStage(Stage primary_stage, BorderPane view_root){
        Scene scene = new Scene(view_root);
        primary_stage.setScene(scene);
        primary_stage.setTitle("Configuration");
        primary_stage.minWidthProperty().bind(view_root.minWidthProperty());
        primary_stage.minHeightProperty().bind(view_root.minHeightProperty());
        primary_stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
