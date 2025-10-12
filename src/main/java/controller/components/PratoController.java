package controller.components;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class PratoController {
    @FXML
    private ImageView imagemView;
    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbPreco;
    @FXML
    private VBox container;

    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void setNome(String nome) {
        lbTitulo.setText(nome);
    }
    public void setPreco(Double preco) {
        lbPreco.setText(String.format("%.2f", preco) + " MT");
    }
    public void setImage(Image image) {
        imagemView.setImage(image);
    }

    public VBox getContainer(){
        return container;
    }
}
