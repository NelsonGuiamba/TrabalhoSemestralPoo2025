package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.AdminServices;
import view.AppContext;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminController1 implements Initializable {

    public Label lbUserName;
    public Label lbDataHoje;
    public Label lbReceita;
    public Label lbPedidos;
    public LineChart<Number, Number> receitaLineChart;
    public BarChart<String, Number> barChartCategoria, barChartCategoria2;
    public PieChart piechartVendidos;
    public Label lbClientes;

    public void abrirHome(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AdminServices ses = new AdminServices();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
        lbUserName.setText(AppContext.getInstance().getUserName());
        lbDataHoje.setText(dateFormatter.format(LocalDateTime.now()));

        lbClientes.setText("" + ses.getTotalClientes());
        lbPedidos.setText("" + ses.getTotalPedido());
        double valor = ses.getTotalReceita();
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        lbReceita.setText(nf.format(valor) + " MT");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for(Map.Entry<Integer, Double> entry : ses.getReceitaData().entrySet()){
            double arredondado = Math.round(entry.getValue() * 100.0) / 100.0;
            series.getData().add(new XYChart.Data<>(entry.getKey(), arredondado));
        }

        receitaLineChart.getData().add(series);

        XYChart.Series<String, Number> barseries = new XYChart.Series<>();
        for(Map.Entry<String, Double> entry : ses.getReceitaDataByCategoria().entrySet()){
            double arredondado = Math.round(entry.getValue() * 100.0) / 100.0;
            barseries.getData().add(new XYChart.Data<>(entry.getKey(), arredondado));
        }


        barChartCategoria.getData().add(barseries);
        for(Map.Entry<String, Integer> entry : ses.getMaisVendidos().entrySet()){
            System.out.println(entry);
            PieChart.Data slice1 = new PieChart.Data(entry.getKey(), entry.getValue());
            piechartVendidos.getData().add(slice1);
        }

        barseries = new XYChart.Series<>();
        for(Map.Entry<String, Double> entry : ses.getPedidoTrabalhador().entrySet()){
            double arredondado = Math.round(entry.getValue() * 100.0) / 100.0;
            barseries.getData().add(new XYChart.Data<>(entry.getKey(), arredondado));
        }


        barChartCategoria2.getData().add(barseries);

    }
}
