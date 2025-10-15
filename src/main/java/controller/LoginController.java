package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.UserType;
import services.UsuarioService;
import util.Utils;
import view.AppContext;

public class LoginController implements Initializable {
    public AnchorPane mainPane;
    @FXML
    private Stage stage;

    private Parent root;
    private Scene scene;
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbErroEmail;
    @FXML
    private Label lbErroPassword;
    @FXML
    private Button loginBtn;
    private boolean isProcessing = false;
    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }
    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void abrirHome(MouseEvent event) throws IOException {
        AppContext.getInstance().setRoute("Home");
        Parent root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void abrirAuth(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Register.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public boolean validarEmail(KeyEvent keyEvent) {
        String email = txtEmail.getText();
        if(email.isEmpty() || email.isBlank()) {
            lbErroEmail.setVisible(true);
            lbErroEmail.setText("Email nao pode estar vazio");
            Utils.adicionarStyle(lbErroEmail, "invalid");
            return false;
        }
        if(!Utils.validateEmail(email)) {
            lbErroEmail.setVisible(true);
            lbErroEmail.setText("Email invalido");
            Utils.adicionarStyle(lbErroEmail, "invalid");
            return false;
        }
        lbErroEmail.setVisible(false);
        Utils.removerStyle(lbErroEmail, "invalid");
        return true;
    }

    public boolean validarPassword(KeyEvent keyEvent) {
        String password = pfPassword.getText();
        if(password.isEmpty() || password.isBlank()) {
            lbErroPassword.setVisible(true);
            lbErroPassword.setText("Password nao pode estar vazio");
            Utils.adicionarStyle(lbErroPassword, "invalid");
            return false;
        }
        if(!password.matches(".*[0-9].*")){
            lbErroPassword.setVisible(true);
            lbErroPassword.setText("Password deve conter numeros");
            Utils.adicionarStyle(lbErroPassword, "invalid");
            return false;
        }
        if(!password.matches(".*[a-zA-Z].*")){
            lbErroPassword.setVisible(true);
            lbErroPassword.setText("Password deve conter letras");
            Utils.adicionarStyle(lbErroPassword, "invalid");
            return false;
        }
        if(password.length() < 4) {
            lbErroPassword.setVisible(true);
            lbErroPassword.setText("Password deve ter no minimo 4 caracteres");
            Utils.adicionarStyle(lbErroPassword, "invalid");
            return false;
        }
        lbErroPassword.setVisible(false);
        Utils.removerStyle(lbErroPassword, "invalid");
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbErroEmail.setVisible(false);
        lbErroPassword.setVisible(false);
    }

    public void login(ActionEvent actionEvent) throws IOException {
        if(isProcessing) return;
        if( !(validarEmail(null) && validarPassword(null))) {
            Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("Todos os campos devem estar validados");
            alert.showAndWait();
            return;
        }
        String email = txtEmail.getText().strip();
        String password = pfPassword.getText().strip();
        UsuarioService service = new UsuarioService();
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                Thread.sleep(2000);
                try {
                    return service.login(email, password);
                }catch (Exception e) {
                    return service.OTHERERROR;
                }
            }
        };
        isProcessing = true;
        loginBtn.setText("Logando...");
        mainPane.setCursor(Cursor.WAIT);
        loginBtn.setCursor(Cursor.WAIT);
        Utils.adicionarStyle(loginBtn, "processing");
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        task.setOnSucceeded(e -> {
            isProcessing = false;
            loginBtn.setText("Login");
            mainPane.setCursor(Cursor.DEFAULT);
            loginBtn.setCursor(Cursor.DEFAULT);
            Utils.removerStyle(loginBtn, "processing");
            Integer status = task.getValue();
            if (status == service.PASSWORDERROR) {
                Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Palavra passe incorrecta");
                alert.setContentText("Por favor verifique a palavra passe  e tente novamente");
                alert.showAndWait();
                return;
            } else if (status == service.OTHERERROR) {
                Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Algo deu errado");
                alert.setContentText("Por favor tente novamente");
                alert.showAndWait();
                return;
            } else if (status == service.LOGINERROR) {
                Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Conta nao existe");
                alert.setContentText("Por favor verifique as credencias e tente novamente\nOu faca o cadastro");
                alert.showAndWait();
                return;
            }
            AppContext.getInstance().setUsuarioLogado(status);
            AppContext.getInstance().setUser(service.getUserType());
            Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Usuario logado com sucesso");
            alert.setContentText("Agora voce pode ter acesso a todas funcionalidades do sistema");
            alert.showAndWait();
            Parent root = null;
            try {
                if(service.getUserType().equals(UserType.ADMIN))
                    root = FXMLLoader.load(getClass().getResource("/view/TelaAdmin1.fxml"));
                else
                    root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));
            } catch (IOException ex) {
                System.out.println("Algo deu errado");
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }
}
