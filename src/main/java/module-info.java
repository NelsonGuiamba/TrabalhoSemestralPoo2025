module org.example.trabalhosemestralpoo2025 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.example.trabalhosemestralpoo2025 to javafx.fxml;
    exports org.example.trabalhosemestralpoo2025;
}