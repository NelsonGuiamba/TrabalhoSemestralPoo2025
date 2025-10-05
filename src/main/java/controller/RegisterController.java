package controller;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class RegisterController {
    public void changeToClicable(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }

    public void revertLink(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        source.setCursor(Cursor.HAND);
    }
}
