package org.example.trabalhosemestralpoo2025;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("test.fxml"));
        Parent root = fxmlLoader.load();
        String css = this.getClass().getResource("/landing.css").toExternalForm();
        root.getStylesheets().add(css);
        Scene scene = new Scene(root);
        stage.setTitle("JavaFX FXML Example");
        stage.setScene(scene);
        stage.show();
    }
}
