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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Reserva;
import model.ReservaStatus;
import services.ReservaService;
import view.AppContext;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReservasMenuController implements Initializable {

    private Stage stage;

    private Scene scene;
    @FXML
    private FlowPane flowPane;
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
        Parent root = FXMLLoader.load(getClass().getResource("/view/Reserva.fxml"));
        stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm", new Locale("pt", "BR"));
        renderReservaItem();
    }

    private void renderReservaItem() {
        ReservaService service = new ReservaService();
        flowPane.getChildren().clear();
        // appini
        for(Reserva r : service.getReservasDeUser(AppContext.getInstance().getUsuarioLogado())){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/components/reservaItem.fxml"));
            VBox reservaComp = null; // o VBox do FXML
            try {
                reservaComp = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ReservaItemController controller = loader.getController();
            Duration duration = Duration.between(r.getDataInicio(), r.getDataFim());
            double hours = duration.getSeconds() / 3600.0;
            DecimalFormat df = new DecimalFormat("#.#");
            controller.setPessoas(""+r.getMesa().getCapacidade());
            controller.setData(dateFormatter.format(r.getDataInicio()));
            controller.setMesa(""+r.getMesa().getId());
            controller.setDuracao(timeFormatter.format(r.getDataInicio()), df.format(hours));
            controller.getBtnCancelar().setOnAction(e -> {
                service.finalizarReserva(r.getId(), ReservaStatus.CANCELADA);
                renderReservaItem();
            });
            flowPane.getChildren().add(reservaComp);
        }
    }
}
