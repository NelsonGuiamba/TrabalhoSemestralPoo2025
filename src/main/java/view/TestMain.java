package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LandingPage.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("JavaFX FXML Example");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
