package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class TelaLogin {

    public void onAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");
        alert.setHeaderText("Funciona java 21");
        alert.setContentText("Digite seu nome:");
        alert.showAndWait();
    }
}
