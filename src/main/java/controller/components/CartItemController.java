package controller.components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class CartItemController implements Initializable {

    @FXML
    private Button btnRemover;

    @FXML
    private ImageView imageView;
    @FXML
    private TextField qtdInput;
    @FXML
    private Label lbNomePrato;
    @FXML
    private Label lbPreco;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        qtdInput.setText("1");
    }

    public TextField getQtdInput(){
        return qtdInput;
    }

    public Button getBtnRemover() {
        return btnRemover;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Label getLbNomePrato() {
        return lbNomePrato;
    }

    public Label getLbPreco() {return lbPreco;}

}
