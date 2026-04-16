package controller;

import controller.components.PratoController2;
import dao.MenuItemDAO;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.ItemCategory;
import model.MenuItem;
import services.AdminMenuService;
import util.Utils;
import view.AppContext;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;
import java.nio.file.Files;
import java.nio.file.Path;

public class AdminController2 implements Initializable {
    public ScrollPane scrollMenu;
    @FXML
    private TextField qtdInput;
    @FXML
    protected TextField searchInput;
    @FXML
    protected Label qtdLabel;
    @FXML
    protected Label searchLabel;
    @FXML
    protected AnchorPane sidePanel;
    @FXML
    protected BorderPane mainPane;
    @FXML
    protected FlowPane listaMenu;
    @FXML
    protected FlowPane listaCart;
    @FXML
    protected Label lbEntrada;
    @FXML
    protected Label lbPrato;
    @FXML
    protected Label lbSobremesa;
    @FXML
    protected Label lbSubtotal;
    @FXML
    protected Label lbTotal;
    @FXML
    protected Label lbIva, lbUserName, lbDataHoje;
    @FXML
    protected Button btnPagamento;
    @FXML
    protected TextField txtNome, txtPreco;
    @FXML
    protected ImageView imgAlterar;
    @FXML
    protected ComboBox<String> cbCategoria;
    protected Label lbEmpty;
    protected Map<String, String> cartItems;
    protected List<MenuItem> menuItems;
    ;
    private Map<String, Integer> userMap;
    protected AdminMenuService service;
    private int currentEdit = -2;
    private File currentFile;
    boolean isVisible;

    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void allowNumberOnly(KeyEvent mouseEvent) {
        this.allowNumberOnly("-1", mouseEvent);
    }

    public void allowNumberOnly(String key, KeyEvent event) {
        TextField source = (TextField) event.getSource();

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Apenas dígitos
                if (newText.length() > 4)
                    return null;
                if (newText.equals("0"))
                    return null;
                return change;
            }
            return null; // Rejeita a alteração
        };

        if (source.getTextFormatter() == null) {
            source.setTextFormatter(new TextFormatter<>(filter));
            source.setText("1");
        }

    }

    protected void slideIn(Pane rightPane, BorderPane borderPane) {
        if (mainPane.getRight() != null) return;
        // Adiciona o painel primeiro
        borderPane.setRight(rightPane);

        // Executa a animação após o layout ser aplicado
        Platform.runLater(() -> {
            double width = rightPane.getWidth();
            rightPane.setTranslateX(width);

            Timeline slideIn = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(rightPane.translateXProperty(), width)),
                    new KeyFrame(Duration.millis(800),
                            new KeyValue(rightPane.translateXProperty(), 0, Interpolator.EASE_BOTH))
            );
            slideIn.play();
        });
    }

    protected void slideOut(Pane rightPane, BorderPane borderPane) {
        if (mainPane.getRight() == null) return;
        double width = rightPane.getWidth();

        Timeline slideOut = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(rightPane.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(rightPane.translateXProperty(), width, Interpolator.EASE_BOTH))
        );

        slideOut.setOnFinished(event -> borderPane.setRight(null));
        slideOut.play();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
        lbUserName.setText(AppContext.getInstance().getUserName());
        lbDataHoje.setText(dateFormatter.format(LocalDateTime.now()));
        slideOut(sidePanel, mainPane);
        renderMenu();
        lbEmpty = new Label("Nenhum item satisfaz a sua pesquisa");

        lbEmpty.setFont(Font.font("Montserrat", 36));
        lbEmpty.setPrefWidth(500);
        lbEmpty.setPrefHeight(300);
        lbEmpty.setTextFill(Color.WHITE);
        lbEmpty.setAlignment(Pos.CENTER);
        lbEmpty.setTextAlignment(TextAlignment.CENTER);
        lbEmpty.setWrapText(true);
        lbEmpty.setVisible(false);
        lbEmpty.setManaged(false);
        listaMenu.getChildren().add(lbEmpty);
        cartItems = new HashMap<>();
        service = new AdminMenuService();
        currentFile = null;
        cbCategoria.getItems().add("Entrada");
        cbCategoria.getItems().add("Prato principal");
        cbCategoria.getItems().add("Sobremesas");
        cbCategoria.setValue("Entrada");
    }

    private void renderMenu() {
        listaMenu.getChildren().clear();
        MenuItemDAO dao = new MenuItemDAO();
        menuItems = dao.findAll();
        for (MenuItem item : menuItems) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/components/pratoBox2.fxml"));
            VBox pratoComp = null; // o VBox do FXML
            try {
                pratoComp = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PratoController2 controller = loader.getController();

            controller.setNome(item.getNomeDoPrato());
            controller.setPreco(item.getPreco());
            System.out.println(item.getImagem());
            URL resourceUrl = getClass().getResource(item.getImagem());
            if (resourceUrl != null) {
                File file = null;
                try {
                    file = new File(resourceUrl.toURI());
                    Image img = new Image(file.toURI().toString(), false);
                    controller.setImage(img);
                } catch (URISyntaxException e) {
                    Image img = new Image(getClass().getResourceAsStream("/view/images/pratos/pratovazio.png"));
                    controller.setImage(img);
                }


            } else {
                Image img = new Image(getClass().getResourceAsStream("/view/images/pratos/pratovazio.png"));
                controller.setImage(img);
            }

            pratoComp.setUserData(item);
            controller.getContainer().setOnMouseClicked(event -> {
                this.currentEdit = item.getId();
                this.txtNome.setText(item.getNomeDoPrato());
                this.txtPreco.setText("" + item.getPreco());
                this.imgAlterar.setImage(controller.getImage());
                slideIn(sidePanel, mainPane);
            });
            controller.getBtnDelete().setOnAction(e -> {
                Alert confirmar = Utils.criarAlerta(Alert.AlertType.CONFIRMATION);
                confirmar.setHeaderText("Voce deseja realmente alterar o status do prato?");
                confirmar.setContentText("O prato "+item.getNomeDoPrato()+" sera alterado, mas voce pode reverter a alteracao a qualquer momento");
                confirmar.showAndWait();
                System.out.println();

                if (confirmar.getResult() == ButtonType.OK) {
                    boolean isActive = service.alterarStatus(item.getId());
                    Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText("Prato alterado com sucesso");
                    alert.showAndWait();
                    if (isActive) {
                        controller.getBtnDelete().setText("Desativar");
                        Utils.removerStyle(controller.getBtnDelete(), "activar");
                    } else {
                        controller.getBtnDelete().setText("Activar");
                        Utils.adicionarStyle(controller.getBtnDelete(), "activar");
                    }
                }
            });

            if (item.isActive()) {
                controller.getBtnDelete().setText("Desativar");
            } else {
                controller.getBtnDelete().setText("Activar");
                Utils.adicionarStyle(controller.getBtnDelete(), "activar");
            }
            listaMenu.getChildren().add(pratoComp);
        }
    }

    public void toggleSide(MouseEvent mouseEvent) {
        if (mainPane.getRight() == null) {
            slideIn(sidePanel, mainPane);
        } else {
            slideOut(sidePanel, mainPane);
        }
    }

    public void toggleEntrada(MouseEvent mouseEvent) {
        boolean entradaActivo = lbEntrada.getStyleClass().contains("active");
        String search = searchInput.getText();

        for (Node node : listaMenu.getChildren()) {
            MenuItem menuItem = (MenuItem) node.getUserData();
            if (!(node.getUserData() instanceof MenuItem)) continue;
            if (!menuItem.getNomeDoPrato().toLowerCase().strip().contains(search)) continue;
            if (entradaActivo) {
                node.setVisible(true);
                node.setManaged(true);
                Utils.removerStyle(node, "active");
            } else {
                if (menuItem.getCategoria().equals(ItemCategory.ENTRADA)) {
                    node.setVisible(true);
                    node.setManaged(true);
                } else {
                    node.setVisible(false);
                    node.setManaged(false);
                }
            }
        }
        Utils.removerStyle(lbSobremesa, "active");
        Utils.removerStyle(lbPrato, "active");
        if (entradaActivo) {
            Utils.removerStyle(lbEntrada, "active");
        } else {
            Utils.adicionarStyle(lbEntrada, "active");
        }
        scrollMenu.setVvalue(0);
    }

    public void togglePrato(MouseEvent mouseEvent) {
        boolean entradaActivo = lbPrato.getStyleClass().contains("active");
        String search = searchInput.getText();
        for (Node node : listaMenu.getChildren()) {
            MenuItem menuItem = (MenuItem) node.getUserData();
            if (!(node.getUserData() instanceof MenuItem)) continue;
            if (!menuItem.getNomeDoPrato().toLowerCase().strip().contains(search)) continue;
            if (entradaActivo) {
                node.setVisible(true);
                node.setManaged(true);
                Utils.removerStyle(node, "active");
            } else {
                if (menuItem.getCategoria().equals(ItemCategory.PRATO_PRINCIPAL)) {
                    node.setVisible(true);
                    node.setManaged(true);
                } else {
                    node.setVisible(false);
                    node.setManaged(false);
                }
            }
        }
        Utils.removerStyle(lbSobremesa, "active");
        Utils.removerStyle(lbEntrada, "active");
        if (entradaActivo) {
            Utils.removerStyle(lbPrato, "active");
        } else {
            Utils.adicionarStyle(lbPrato, "active");
        }
        checkEmpty();
        scrollMenu.setVvalue(0);
    }

    public void toggleSobremesa(MouseEvent mouseEvent) {
        boolean entradaActivo = lbSobremesa.getStyleClass().contains("active");
        String search = searchInput.getText();
        for (Node node : listaMenu.getChildren()) {
            MenuItem menuItem = (MenuItem) node.getUserData();
            if (!(node.getUserData() instanceof MenuItem)) continue;
            if (!menuItem.getNomeDoPrato().toLowerCase().strip().contains(search)) continue;
            if (entradaActivo) {
                node.setVisible(true);
                node.setManaged(true);
                Utils.removerStyle(node, "active");
            } else {
                if (menuItem.getCategoria().equals(ItemCategory.SOBREMESA)) {
                    node.setVisible(true);
                    node.setManaged(true);
                } else {
                    node.setVisible(false);
                    node.setManaged(false);
                }
            }
        }
        Utils.removerStyle(lbEntrada, "active");
        Utils.removerStyle(lbPrato, "active");
        if (entradaActivo) {
            Utils.removerStyle(lbSobremesa, "active");
        } else {
            Utils.adicionarStyle(lbSobremesa, "active");
        }
        checkEmpty();
        scrollMenu.setVvalue(0);
    }

    public void filtarPratos(KeyEvent keyEvent) {
        boolean entrada = lbEntrada.getStyleClass().contains("active");
        boolean sobremesa = lbSobremesa.getStyleClass().contains("active");
        boolean prato = lbPrato.getStyleClass().contains("active");
        String search = searchInput.getText().strip().toLowerCase();
        for (Node node : listaMenu.getChildren()) {
            if (!(node.getUserData() instanceof MenuItem)) continue;
            MenuItem menuItem = (MenuItem) node.getUserData();

            if (entrada) {
                if (menuItem.getCategoria().equals(ItemCategory.ENTRADA)) {
                    if (menuItem.getNomeDoPrato().toLowerCase().strip().contains(search)) {
                        node.setVisible(true);
                        node.setManaged(true);
                    } else {
                        node.setVisible(false);
                        node.setManaged(false);
                    }
                }
            } else if (prato) {
                if (menuItem.getCategoria().equals(ItemCategory.PRATO_PRINCIPAL)) {
                    if (menuItem.getNomeDoPrato().toLowerCase().strip().contains(search)) {
                        node.setVisible(true);
                        node.setManaged(true);
                    } else {
                        node.setVisible(false);
                        node.setManaged(false);
                    }
                }
            } else if (sobremesa) {
                if (menuItem.getCategoria().equals(ItemCategory.SOBREMESA)) {
                    if (menuItem.getNomeDoPrato().toLowerCase().strip().contains(search)) {
                        node.setVisible(true);
                        node.setManaged(true);
                    } else {
                        node.setVisible(false);
                        node.setManaged(false);
                    }
                }
            } else {
                if (menuItem.getNomeDoPrato().toLowerCase().strip().contains(search)) {
                    node.setVisible(true);
                    node.setManaged(true);
                } else {
                    node.setVisible(false);
                    node.setManaged(false);
                }
            }
        }
        checkEmpty();
    }

    protected void checkEmpty() {
        boolean todosInvisiveis = listaMenu.getChildren().stream()
                .filter(n -> n.getUserData() instanceof MenuItem)
                .allMatch(n -> !n.isVisible());
        lbEmpty.setVisible(todosInvisiveis);
        lbEmpty.setManaged(todosInvisiveis);
    }

    public void abrirHome(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/TelaAdmin1.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void abrirNovo() {
        this.currentEdit = -1;
        txtNome.setText("");
        txtPreco.setText("");
        Image img = new Image(getClass().getResourceAsStream("/view/images/pratos/pratovazio.png"));
        imgAlterar.setImage(img);
        slideIn(sidePanel, mainPane);
    }

    public void confirmarAlteracao() {
        Alert confirmar = Utils.criarAlerta(Alert.AlertType.CONFIRMATION);
        confirmar.getDialogPane().setPrefHeight(600);
        if (currentEdit == -1) {
            confirmar.setTitle("Confirmar prato");
            confirmar.setHeaderText("Deseja realmente adicionar o prato");
        }else {
            confirmar.setTitle("Confirmar Alteracoes");
            confirmar.setHeaderText("Deseja realmente alterar o prato");
        }
        confirmar.showAndWait();
        if (confirmar.getResult() == ButtonType.OK) {
            double preco;
            try {
                preco = Double.parseDouble(txtPreco.getText());
            } catch (Exception e) {
                preco = 0;
            }
            File selectedFile = currentFile;
            String url = "null";
            if (currentFile != null) {
                File destDir = new File("src/main/resources/view/images/pratos");
                if (!destDir.exists()) destDir.mkdirs();
                Path dest = destDir.toPath().resolve(selectedFile.getName());
                try {
                    Files.copy(selectedFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Algo deu errado");
                    alert.setContentText("Por favor tente novamente");
                    alert.showAndWait();
                    return;
                }
                url = "/view/images/pratos/" + selectedFile.getName();
            }


            service.alterarObjecto(currentEdit, txtNome.getText(), preco, url, cbCategoria.getValue());
            Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Prato salvo com sucesso");
            alert.showAndWait();
            this.currentEdit = -1;
            txtNome.setText("");
            txtPreco.setText("");
            Image img = new Image(getClass().getResourceAsStream("/view/images/pratos/pratovazio.png"));
            imgAlterar.setImage(img);
            renderMenu();
            slideOut(sidePanel, mainPane);
        }
    }

    public void alterarImagem(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PNG Image");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Pictures"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            this.currentFile = selectedFile;
            imgAlterar.setImage(new Image(selectedFile.toURI().toString()));
        }
    }
}
