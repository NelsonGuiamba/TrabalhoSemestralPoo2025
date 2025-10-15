package view;

import dao.UserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.UserType;
import services.UsuarioService;

public class TestMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
//        AppContext.getInstance().setUsuarioLogado(1);
//        AppContext.getInstance().setUser(UserType.CLIENT);
        UserDAO userDAO = new UserDAO();
        userDAO.findById(11);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Stash Restaurantes");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
