package controller;

import controller.components.CartItemController;
import controller.components.PratoController;
import dao.MenuItemDAO;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.ItemCategory;
import model.MenuItem;
import model.UserType;
import services.PedidoService;
import util.Utils;
import view.AppContext;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;

public class MenuController implements Initializable {
    @FXML
    protected TextField qtdInput;
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
    protected Label lbEmpty;
    protected Map<String, String> cartItems;
    protected List<MenuItem> menuItems;
    protected boolean wasVisibleCart = false;
    protected boolean isProcessing = false;
    @FXML
    protected ComboBox<String> cbUser, cbMesa;
    private Map<String, Integer> userMap;

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
                if (newText.length() > 2)
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


        source.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // perdeu o foco
                if (source.getText().isEmpty()) {
                    source.setText("1");
                }
                String newText = source.getText();
                if (!key.equals("-1"))
                    try {
                        int num = Integer.parseInt(newText);
                        if (num > 0) {
                            cartItems.put(key, "" + num);
                        }
                    } catch (NumberFormatException e) {
                        cartItems.put(key, "-1");
                    }
                renderMoney();
            }
        });

    }

    protected void slideIn(Pane rightPane, BorderPane borderPane) {
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
        //slideOut(sidePanel, mainPane);
        listaMenu.getChildren().clear();
        MenuItemDAO dao = new MenuItemDAO();
        menuItems = dao.findAll();
        for (MenuItem item : menuItems) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/components/pratoBox.fxml"));
            VBox pratoComp = null; // o VBox do FXML
            try {
                pratoComp = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PratoController controller = loader.getController();

            controller.setNome(item.getNomeDoPrato());
            controller.setPreco(item.getPreco());
            System.out.println(item.getImagem());
            Image img = new Image(getClass().getResourceAsStream(item.getImagem()));
            controller.setImage(img);
            pratoComp.setUserData(item);
            controller.getContainer().setOnMouseClicked(event -> {
                this.addToCart("" + item.getId());
            });
            listaMenu.getChildren().add(pratoComp);
        }
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
    }

    private void renderWorker() {
        if (AppContext.getInstance().isUserType(UserType.WORKER)) {
            PedidoService service = new PedidoService();
            cbUser.getItems().clear();
            userMap = service.getUsers();
            for (Map.Entry<String, Integer> entry : userMap.entrySet()) {
                cbUser.getItems().add(entry.getKey());
            }
            System.out.println(userMap);
            cbMesa.getItems().clear();
            for (String mesa : service.getMesasDisponiveis())
                cbMesa.getItems().add("Mesa " + mesa);
            cbUser.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (userMap.getOrDefault(newValue, 0) > 0) {
                    cbMesa.setValue("Mesa " + userMap.get(newValue));
                    cbMesa.setDisable(true);
                } else {
                    cbMesa.setDisable(false);
                }
            });

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

    protected void renderCart() {
        listaCart.getChildren().clear();
        boolean hasElements = false;
        for (MenuItem item : menuItems) {
            String key = "" + item.getId();
            String value = cartItems.getOrDefault(key, "-1");
            int qtd = Integer.parseInt(value);
            if (qtd > 0) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/components/cartItem.fxml"));
                VBox pratoComp = null; // o VBox do FXML
                try {
                    pratoComp = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                CartItemController controller = loader.getController();
                System.out.println(item.getImagem());
                Image img = new Image(getClass().getResourceAsStream(item.getImagem()));
                controller.getImageView().setImage(img);
                controller.getLbNomePrato().setText(item.getNomeDoPrato());
                controller.getBtnRemover().setOnAction(e -> {
                    cartItems.remove(key);
                    renderCart();
                });
                controller.getQtdInput().setOnKeyTyped(e -> this.allowNumberOnly(key, e));
                controller.getLbPreco().setText(String.format("%.2f", item.getPreco()) + " MT");
                listaCart.getChildren().add(pratoComp);
                hasElements = true;
            }
        }
        if (hasElements) {
            renderMoney();
            if (!wasVisibleCart) {
                slideIn(sidePanel, mainPane);
                wasVisibleCart = true;
            }
        } else {
            if (wasVisibleCart) {

                slideOut(sidePanel, mainPane);
                wasVisibleCart = false;
            }
        }
    }

    protected void addToCart(String key) {
        if (!cartItems.containsKey(key)) {
            cartItems.put(key, "1");
        }
        renderCart();
    }

    protected void renderMoney() {
        Double total = 0.0;
        for (MenuItem item : menuItems) {
            String key = "" + item.getId();
            String value = cartItems.getOrDefault(key, "-1");
            int qtd = Integer.parseInt(value);
            if (qtd > 0) {
                total += qtd * item.getPreco();
            }
        }
        lbTotal.setText(String.format("%.2f", total));
        lbSubtotal.setText(String.format("%.2f", total / 1.16));
        lbIva.setText(String.format("%.2f", (total / 1.16) * 0.16));
    }

    @FXML
    protected void processPayment() {
        if (isProcessing) return;
        renderMoney();
        String nomeClient = cbUser.getValue();
        int mesaId;
        if (AppContext.getInstance().isUserType(UserType.WORKER)) {
            if (nomeClient == null || nomeClient.isEmpty()) {
                Alert confirmar = Utils.criarAlerta(Alert.AlertType.ERROR);
                confirmar.setHeaderText("Erro");
                confirmar.setContentText("Por favor selecione o cliente");
                confirmar.showAndWait();
                return;
            }
            String nomeMesa = cbMesa.getValue();
            if (nomeMesa == null || nomeMesa.isEmpty()) {
                Alert confirmar = Utils.criarAlerta(Alert.AlertType.ERROR);
                confirmar.setHeaderText("Erro");
                confirmar.setContentText("Por favor selecione a mesa");
                confirmar.showAndWait();
                return;
            }
            mesaId = Integer.parseInt(nomeMesa.split(" ")[1]);
        } else {
            mesaId = -1;
        }
        String totalApagar = lbTotal.getText();
        Alert confirmar = Utils.criarAlerta(Alert.AlertType.CONFIRMATION);
        confirmar.setHeaderText("Confirmar o pagamento?");
        confirmar.setContentText("Deseja confirmar o pagamento de " + totalApagar + " MT");
        confirmar.showAndWait();
        PedidoService service = new PedidoService();
        if (confirmar.getResult() == ButtonType.OK) {
            Task<Integer> task = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    try {
                        if (AppContext.getInstance().isUserType(UserType.CLIENT))
                            return service.criarPedidoTakeway(
                                    AppContext.getInstance().getUsuarioLogado()
                                    , cartItems);
                        else
                            return service.criarPedidoNormal(nomeClient,
                                    AppContext.getInstance().getUsuarioLogado(),
                                    mesaId,
                                    cartItems);
                    } catch (Exception e) {
                        return -1;
                    }
                }
            };
            isProcessing = true;
            btnPagamento.setText("Fazendo o pagamento...");
            mainPane.setCursor(Cursor.WAIT);
            btnPagamento.setCursor(Cursor.WAIT);
            Utils.adicionarStyle(btnPagamento, "processing");
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

            task.setOnSucceeded(e -> {
                isProcessing = false;
                btnPagamento.setCursor(Cursor.DEFAULT);
                mainPane.setCursor(Cursor.DEFAULT);
                Utils.removerStyle(btnPagamento, "processing");
                btnPagamento.setText("Fazer o pagamento");
                int value = task.getValue();
                if (value > 0) {
                    Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText("Pedido realizado com sucesso");
                    alert.showAndWait();
                    cartItems.clear();
                    renderCart();
                    renderWorker();
                } else {
                    Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Algo deu errado");
                    alert.setContentText("Por favor tente novamente");
                    alert.showAndWait();
                }
            });
        }
    }

    public void abrirHome(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
