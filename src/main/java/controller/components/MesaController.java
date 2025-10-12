package controller.components;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MesaController {
    @FXML
    private Label lbPessoas;
    @FXML
    private Label lbMesa;
    @FXML
    private VBox pane;

    public void setMesa(String mesa) {
        lbMesa.setText(mesa);
    }

    public void setPessoas(String pessoas) {
        lbPessoas.setText(pessoas + " pessoas");
    }

    public VBox getPane() {
        return pane;
    }

    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }
}
