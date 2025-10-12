package util;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class Utils {
    public static int getHoraDeFecho(){
        LocalDate  date = LocalDate.now();
        return getHoraDeFecho(date);
    }

    public static int getHoraDeFecho(LocalDate date){
        DayOfWeek diaDeSemana = date.getDayOfWeek();
        if(diaDeSemana.equals(DayOfWeek.SUNDAY))
            return 20;
        else
            return 23;
    }

    public static int getHoraDeAbertura(){
        return 9;
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean validateEmail(String textField) {
        return Pattern.matches(EMAIL_REGEX, textField);
    }

    public static void adicionarStyle(Node node, String style) {
        if (!node.getStyleClass().contains(style)) {
            node.getStyleClass().add(style);
        }
    }
    public static void removerStyle(Node node, String style) {
        if (node.getStyleClass().contains(style)) {
            node.getStyleClass().remove(style);
        }
    }

    public static Alert criarAlerta(Alert.AlertType type){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Utils.class.getResource("/view/css/alerts.css").toExternalForm());
        dialogPane.getStyleClass().add("my-alert");
        return alert;
    }



}
