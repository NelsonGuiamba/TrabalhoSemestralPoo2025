package controller;

import controller.components.ReservaItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Pedido;
import model.PedidoStatus;
import model.Reserva;
import model.ReservaStatus;
import services.PedidoService;
import services.ReservaService;
import util.Utils;
import view.AppContext;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class PedidosMenuController implements Initializable {

    private Stage stage;

    private Scene scene;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Label  lbUserName;
    @FXML
    private Label lbDataHoje;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;
    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void abrirHome(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void adicionarReserva(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/FazerPedidoB.fxml"));
        stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
        lbUserName.setText(AppContext.getInstance().getUserName());
        lbDataHoje.setText(dateFormatter.format(LocalDateTime.now()));
        dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm", new Locale("pt", "BR"));
        renderReservaItem();
    }

    private void renderReservaItem() {
        PedidoService service = new PedidoService();
        flowPane.getChildren().clear();
        // appini
        for(Pedido p : service.getPedidosDeHoje()){
            System.out.println(p);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/components/reservaItem.fxml"));
            VBox reservaComp = null; // o VBox do FXML
            try {
                reservaComp = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ReservaItemController controller = loader.getController();
            double hours = 1.5;
            DecimalFormat df = new DecimalFormat("#.#");
            controller.setData(dateFormatter.format(p.getDataCompra()));
            if(p.isTakeway())
                controller.setMesa("-");
            else
                controller.setMesa(""+p.getMesa().getCapacidade());
            controller.setDuracao(timeFormatter.format(p.getDataCompra()), df.format(hours));
            controller.getBtnCancelar().setText("Finalizar pedido");
            controller.getBtnCancelar().setOnAction(e -> {
                if(p.isTakeway()) {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Vereficacao de pedido");
                    dialog.setHeaderText("Digite o email do dono:");
                    dialog.setContentText("Email:");

                    dialog.getDialogPane().getStylesheets().add(
                            getClass().getResource("/view/css/alerts.css").toExternalForm()
                    );

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(texto -> {
                        if(texto.strip().equals(p.getClient().getEmail())){
                            service.finalizarPedido(p.getId(), PedidoStatus.CONCLUIDO);
                            Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
                            alert.setTitle("Sucesso");
                            alert.setHeaderText("Pedido confirmado com sucesso");
                            alert.showAndWait();
                            renderReservaItem();
                        }else{
                            Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                            alert.setTitle("Erro");
                            alert.setHeaderText("Email incorrecto");
                            alert.setContentText("Por favor verifique o email e tente novamente");
                            alert.showAndWait();
                        }
                    });
                    return;
                }

                service.finalizarPedido(p.getId(), PedidoStatus.CONCLUIDO);
                renderReservaItem();
            });
            flowPane.getChildren().add(reservaComp);
        }
    }
}
