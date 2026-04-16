package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.UserType;
import services.PedidoService;
import util.Utils;
import view.AppContext;

public class LandingControl implements Initializable {
    @FXML
    private HBox menuLinks;
    @FXML
    private Button btnLogout;
    private Stage stage;
    private Parent root;
    private Scene scene;
    private String current;

    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void abrirMenu(MouseEvent event) throws IOException {
        AppContext.getInstance().setRoute("menu");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void abrirContacto(MouseEvent event) throws IOException {
        AppContext.getInstance().setRoute("contacto");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AboutUs.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void abrirHome(MouseEvent event) throws IOException {
        AppContext.getInstance().setRoute("home");
        Parent root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void abrirRegister(MouseEvent event) throws IOException {

            Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText("Aviso");
            alert.setContentText("Você primeiro deve estar logado para continuar.");
            alert.showAndWait();
            Parent root = FXMLLoader.load(getClass().getResource("/view/Register.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("state" + AppContext.getInstance().getRoute());
        if(AppContext.getInstance().getUsuarioLogado() == -1)
            btnLogout.setVisible(false);
        else
            btnLogout.setVisible(true);
        if (AppContext.getInstance().getUsuarioLogado() == -1 ||
                AppContext.getInstance().getUser().equals(UserType.CLIENT)) {
            Label home = new Label("Home".toUpperCase());
            Utils.adicionarStyle(home, "menuTitles");
            home.setOnMouseClicked((MouseEvent mouseEvent) -> {
                try {
                    abrirHome(mouseEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label menu = new Label("menu".toUpperCase());
            Utils.adicionarStyle(menu, "menuTitles");
            menu.setOnMouseClicked((event) -> {
                try {
                    abrirMenu(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label reservar = new Label("Reservar".toUpperCase());
            Utils.adicionarStyle(reservar, "menuTitles");
            reservar.setOnMouseClicked((event) -> {
                try {
                    abrirReserva(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label sobre = new Label("Sobre NÓS".toUpperCase());
            Utils.adicionarStyle(sobre, "menuTitles");
            sobre.setOnMouseClicked((event) -> {
                try {
                    abrirContacto(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            if (AppContext.getInstance().getRoute().equals("contacto")) {
                sobre.getStyleClass().clear();
                sobre.getStyleClass().add("menuTitle");
                sobre.setOnMouseClicked((event) -> {
                });
            } else if (AppContext.getInstance().getRoute().equals("menu")) {
                menu.getStyleClass().clear();
                menu.getStyleClass().add("menuTitle");
                menu.setOnMouseClicked((event) -> {
                });
            } else {
                home.getStyleClass().clear();
                home.getStyleClass().add("menuTitle");
                home.setOnMouseClicked((event) -> {
                });
            }
            menuLinks.getChildren().clear();
            menuLinks.getChildren().addAll(home, menu, reservar, sobre);
        } else if (AppContext.getInstance().isUserType(UserType.WORKER)) {
            Label home = new Label("Home".toUpperCase());
            Utils.adicionarStyle(home, "menuTitles");
            home.setOnMouseClicked((MouseEvent mouseEvent) -> {
                try {
                    abrirHome(mouseEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label menu = new Label("menu".toUpperCase());
            Utils.adicionarStyle(menu, "menuTitles");
            menu.setOnMouseClicked((event) -> {
                try {
                    abrirMenu(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label reservar = new Label("Fazer pedidos".toUpperCase());
            Utils.adicionarStyle(reservar, "menuTitles");
            reservar.setOnMouseClicked((event) -> {
                try {
                    abrirFazerPedido(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label sobre = new Label("Sobre NÓS".toUpperCase());
            Utils.adicionarStyle(sobre, "menuTitles");
            sobre.setOnMouseClicked((event) -> {
                try {
                    abrirContacto(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            if (AppContext.getInstance().getRoute().equals("contacto")) {
                sobre.getStyleClass().clear();
                sobre.getStyleClass().add("menuTitle");
                sobre.setOnMouseClicked((event) -> {
                });
            } else if (AppContext.getInstance().getRoute().equals("menu")) {
                menu.getStyleClass().clear();
                menu.getStyleClass().add("menuTitle");
                menu.setOnMouseClicked((event) -> {
                });
            } else {
                home.getStyleClass().clear();
                home.getStyleClass().add("menuTitle");
                home.setOnMouseClicked((event) -> {
                });
            }
            menuLinks.getChildren().clear();
            menuLinks.getChildren().addAll(home, menu, reservar, sobre);
        }
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public void abrirFazerPedido(MouseEvent event) throws IOException {
        if (AppContext.getInstance().isUserType(UserType.CLIENT)) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/view/FazerPedido.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if (AppContext.getInstance().isUserType(UserType.WORKER)) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/PedidoMenu.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            abrirRegister(event);
        }
    }

    public void abrirReserva(MouseEvent event) throws IOException {
        if (AppContext.getInstance().isUserType(UserType.CLIENT)) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/view/ReservaMenu.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if (AppContext.getInstance().isUserType(UserType.WORKER)) {
            Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText("Aviso");
            alert.setContentText("Sem permissao para aceder a pagina");
            alert.showAndWait();
        } else {
            abrirRegister(event);
        }
    }

    public void fazerLogout() {
        Alert confirmar = Utils.criarAlerta(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Logout");
        confirmar.setHeaderText("Deseja realmente sair do sistema?");
        confirmar.setContentText("\nVoce tera que fazer login novamente para aceder o sistema");
        confirmar.showAndWait();
        if (confirmar.getResult() == ButtonType.OK) {
            AppContext.getInstance().setUser(null);
            AppContext.getInstance().setUsuarioLogado(-1);

            Label home = new Label("Home".toUpperCase());
            Utils.adicionarStyle(home, "menuTitles");
            home.setOnMouseClicked((MouseEvent mouseEvent) -> {
                try {
                    abrirHome(mouseEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label menu = new Label("menu".toUpperCase());
            Utils.adicionarStyle(menu, "menuTitles");
            menu.setOnMouseClicked((event) -> {
                try {
                    abrirMenu(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label reservar = new Label("Reservar".toUpperCase());
            Utils.adicionarStyle(reservar, "menuTitles");
            reservar.setOnMouseClicked((event) -> {
                try {
                    abrirReserva(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Label sobre = new Label("Sobre NÓS".toUpperCase());
            Utils.adicionarStyle(sobre, "menuTitles");
            sobre.setOnMouseClicked((event) -> {
                try {
                    abrirContacto(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            btnLogout.setVisible(false);
            menuLinks.getChildren().clear();
            menuLinks.getChildren().addAll(home, menu, reservar, sobre);
        }
    }
}
