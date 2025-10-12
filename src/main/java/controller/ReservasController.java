package controller;

import controller.components.MesaController;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import model.Mesa;
import services.MesaService;
import services.PedidoService;
import services.ReservaService;
import util.Utils;
import view.AppContext;
import view.AppInitializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReservasController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private DatePicker dataReserva;
    private int selectedMesa;
    private List<Mesa> mesas;
    @FXML
    private FlowPane listaMesas;
    @FXML
    private FlowPane listaHorarios;
    @FXML
    private Label lbMesaSelect, lbHorarioSelect;
    @FXML
    private RadioButton rBtn1, rBtn2, rBtn3, rBtn4;
    @FXML
    private Button btnConfirmar;
    private int selectedDuration;
    private int[] selectedHorario;
    private boolean isProcessing = false;

    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void animateVisibility(Node node, boolean show) {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), node);
        if (show) {
            node.setVisible(true);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
        } else {
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(event -> node.setVisible(false));
        }
        ft.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatePicker datePicker = new DatePicker();

// Set minimum selectable date
        LocalDate minDate = LocalDate.now().plusDays(1);
        dataReserva.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(minDate));
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataReserva.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return LocalDate.parse(string, formatter);
            }
        });
        MesaService mesaService = new MesaService();
        mesas = mesaService.getMesasDisponiveis(LocalDateTime.now());
        System.out.println(mesas);
        selectedMesa = -1;
        lbMesaSelect.setText("");
        lbHorarioSelect.setText("");
        listaMesas.getChildren().clear();
        listaHorarios.getChildren().clear();
//        listaMesas.setVisible(false);
//        listaHorarios.setVisible(false);
        selectedDuration = 0;
        selectedHorario = new int[2];
        selectedHorario[0] = -1;
        selectedHorario[1] = -1;
        btnConfirmar.setVisible(false);
        //renderMesas();
    }

    public void renderMesas() {
        listaMesas.getChildren().clear();
        for (Mesa mesa : mesas) {
            FXMLLoader loader;
            if (mesa.getId() == selectedMesa) {
                loader = new FXMLLoader(getClass().getResource("/view/components/mesaActiva.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/view/components/mesa.fxml"));
            }
            Node node;
            try {
                node = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MesaController controller = loader.getController();
            controller.setMesa("" + mesa.getId());
            controller.setPessoas("" + mesa.getCapacidade());
            if (selectedMesa != mesa.getId())
                controller.getPane().setOnMouseClicked(event -> {
                    selectedMesa = mesa.getId();
                    renderMesas();
                    renderHorarios();
                });
            listaMesas.getChildren().add(node);
            lbMesaSelect.setText("Selecione uma mesa");
        }
        if (selectedMesa != -1) renderHorarios();
    }

    public void renderHorarios() {
        MesaService service = new MesaService();
        System.out.println(selectedDuration);

        List<String> horarios = service.getHorariosDisponiveisParaMesa(selectedMesa, dataReserva.getValue(), selectedDuration);
        listaHorarios.getChildren().clear();
        boolean hasShowed = false;
        for (String horario : horarios) {
            System.out.println(horario);

            String h = horario.split(":")[0];
            String m = horario.split(":")[1];
            Label label = new Label();
            StringBuilder sb = new StringBuilder();
            if(h.length() == 1)
                sb.append("0"+h);
            else
                sb.append(h);
            if(m.length() == 1)
                sb.append(":0"+m);
            else
                sb.append(":"+m);
            label.setText(sb.toString());
            label.setPrefWidth(475);
            label.setPrefHeight(58);
            label.setOnMouseClicked(event -> {
                String hh = label.getText().split(":")[0];
                String mm = label.getText().split(":")[1];
                selectedHorario[0] = Integer.parseInt(hh);
                selectedHorario[1] = Integer.parseInt(mm);
                renderHorarios();
                btnConfirmar.setVisible(true);
            });
            label.setOnMouseEntered(event -> {
                this.changeToClicable(event);
            });
            label.setOnMouseExited(event -> {
                this.revertLink(event);
            });
            if (Integer.parseInt(h) == selectedHorario[0] &&
                    Integer.parseInt(m) == selectedHorario[1])
                Utils.adicionarStyle(label, "horarioSelected");
            else
                Utils.adicionarStyle(label, "horarioActivo");
            listaHorarios.getChildren().add(label);
            hasShowed = true;
        }
        if (!hasShowed) {
            Label label = new Label("Sem horarios para os filtros selecionados");
            label.setPrefWidth(475);
            label.setPrefHeight(58 * 5);
            label.setAlignment(Pos.CENTER);
            label.setWrapText(true);
            Utils.adicionarStyle(label, "horarioSem");
            listaHorarios.getChildren().add(label);
        }
        lbHorarioSelect.setText("Selecione um horario");
    }

    public void selectDuration() {
        if (rBtn1.isSelected())
            selectedDuration = 90;
        else if (rBtn2.isSelected()) {
            selectedDuration = 120;
        } else if (rBtn3.isSelected()) {
            selectedDuration = 180;
        } else if (rBtn4.isSelected()) {
            selectedDuration = 240;
        }

        if (dataReserva.getValue() != null) renderMesas();
    }

    public void selectDate(ActionEvent actionEvent) {
        if (selectedDuration >= 1) {
            renderMesas();
        }
    }

    public void confirmarReserva(ActionEvent event) {
        if (isProcessing) return;
        ReservaService service = new ReservaService();
        Alert confirmar = Utils.criarAlerta(Alert.AlertType.CONFIRMATION);
        confirmar.getDialogPane().setPrefHeight(600);
        confirmar.setTitle("Confirmar Reserva");
        confirmar.setHeaderText("Detalhes da reserva");
        LocalDateTime dataInicio =dataReserva.getValue()
                .atTime(selectedHorario[0], selectedHorario[1]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm");
        confirmar.setContentText("Preco a pagar " + ReservaService.calcularPreco(selectedMesa, selectedDuration) + " MT" +
                "\nData da reserva " + formatter.format(dataInicio) +
                "\nMesa : " + selectedMesa);
        confirmar.showAndWait();

        if (confirmar.getResult() == ButtonType.OK) {
            Task<Integer> task = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    try {
                        // TODO : Appinitializer
                        return service.criarReserva(AppContext.getInstance().getUsuarioLogado(),
                                selectedMesa, dataInicio, dataInicio.plusMinutes(selectedDuration));
                    } catch (Exception e) {
                        return -1;
                    }
                }
            };
            isProcessing = true;
            btnConfirmar.setText("Confirmando a reserva...");
            mainPane.setCursor(Cursor.WAIT);
            btnConfirmar.setCursor(Cursor.WAIT);
            Utils.adicionarStyle(btnConfirmar, "processing");
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

            task.setOnSucceeded(e -> {
                isProcessing = false;
                btnConfirmar.setCursor(Cursor.DEFAULT);
                mainPane.setCursor(Cursor.DEFAULT);
                Utils.removerStyle(btnConfirmar, "processing");
                btnConfirmar.setText("Fazer o pagamento");
                int value = task.getValue();
                if(value > 0){
                    Alert alert = Utils.criarAlerta(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText("Reserva confirmada com sucesso");
                    alert.showAndWait();
                    selectedMesa = -1;
                    lbMesaSelect.setText("");
                    lbHorarioSelect.setText("");
                    listaMesas.getChildren().clear();
                    listaHorarios.getChildren().clear();
                    selectedHorario[0] = -1;
                    selectedHorario[1] = -1;
                    btnConfirmar.setVisible(false);
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/view/ReservaMenu.fxml"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }else if(value == -22){
                    Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Voce ja tem uma reserva para este dia");
                    alert.setContentText("Cada usuario so pode ter uma reserva por dia");
                    alert.showAndWait();
                    selectedMesa = -1;
                    lbMesaSelect.setText("");
                    lbHorarioSelect.setText("");
                    listaMesas.getChildren().clear();
                    listaHorarios.getChildren().clear();
                    selectedHorario[0] = -1;
                    selectedHorario[1] = -1;
                    btnConfirmar.setVisible(false);
                    renderMesas();
                }else{
                    Alert alert = Utils.criarAlerta(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Algo deu errado");
                    alert.setContentText("Por favor tente novamente");
                    alert.showAndWait();
                    selectedMesa = -1;
                    lbMesaSelect.setText("");
                    lbHorarioSelect.setText("");
                    listaMesas.getChildren().clear();
                    listaHorarios.getChildren().clear();
                    selectedHorario[0] = -1;
                    selectedHorario[1] = -1;
                    btnConfirmar.setVisible(false);
                    renderMesas();
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
