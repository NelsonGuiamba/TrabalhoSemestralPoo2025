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

public class RegisterController implements Initializable {
    public AnchorPane panePrincial;
    @FXML
    private Stage stage;

    private Parent root;
    private Scene scene;
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField txtNome;
    @FXML
    private Button loginBtn;
    @FXML
    private Label lbErroNome;
    @FXML
    private Label lbErroEmail;
    @FXML
    private Label lbErroPassword;
    private boolean isProcessing = false;
    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        if (source instanceof Button && isProcessing) {
            source.setCursor(Cursor.WAIT);
            return;
        }
        source.setCursor(Cursor.HAND);
    }

    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void abrirHome(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void abrirAuth(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbErroEmail.setVisible(false);
        lbErroPassword.setVisible(false);
        lbErroNome.setVisible(false);
    }

    public boolean validarNome(KeyEvent keyEvent) {
        String nome = txtNome.getText();
        if(nome.isEmpty() || nome.isBlank()) {
            lbErroNome.setVisible(true);
            lbErroNome.setText("Nome nao pode estar vazio");
            Utils.adicionarStyle(lbErroNome, "invalid");
            return false;
        }
        if(nome.length() < 3) {
            lbErroNome.setVisible(true);
            lbErroNome.setText("Nome deve ter no minimo 3 caracteres");
            Utils.adicionarStyle(lbErroNome, "invalid");
            return false;
        }
        lbErroNome.setVisible(false);
        Utils.removerStyle(lbErroNome, "invalid");
        return true;
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

    public void cadastrar(ActionEvent event) throws IOException {
        if(isProcessing) return;
        if( !(validarEmail(null) && validarNome(null) && validarPassword(null))) {
            Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("Todos os campos devem estar validados");
            alert.showAndWait();
            return;
        }
        String nome = txtNome.getText().strip();
        String email = txtEmail.getText().strip();
        String password  = pfPassword.getText().strip();
        UsuarioService service = new UsuarioService();
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                Thread.sleep(2000);
                try{
                    return service.register(nome, password, email, UserType.CLIENT);
                }catch(Exception e){
                    return service.OTHERERROR;
                }
            }
        };
        isProcessing = true;
        loginBtn.setText("Cadastrando...");
        panePrincial.setCursor(Cursor.WAIT);
        loginBtn.setCursor(Cursor.WAIT);
        Utils.adicionarStyle(loginBtn, "processing");
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        task.setOnSucceeded(e -> {
            isProcessing = false;
            loginBtn.setText("Cadastar");
            panePrincial.setCursor(Cursor.DEFAULT);
            loginBtn.setCursor(Cursor.DEFAULT);
            Utils.removerStyle(loginBtn, "processing");
            Integer status = task.getValue();
            if(status == service.EMAILEXISTS){
                Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Usuario com este email ja existe");
                alert.setContentText("Efectue o login com o email");
                alert.showAndWait();
                return;
            } else if (status == service.OTHERERROR) {
                Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Algo deu errado");
                alert.setContentText("Por favor tente novamente");
                alert.showAndWait();
                return;
            }
            AppContext.getInstance().setUsuarioLogado(status);
            AppContext.getInstance().setUser(UserType.CLIENT);
            Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Usuario criado com sucesso");
            alert.setContentText("Agora voce pode ter acesso a todas funcionalidades do sistema");
            alert.showAndWait();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));
            } catch (IOException ex) {
                System.out.println("algo deu errado");
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });

    }

}
