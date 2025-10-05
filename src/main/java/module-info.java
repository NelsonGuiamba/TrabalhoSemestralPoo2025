module org.example.trabalhosemestralpoo2025 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Hibernate & Jakarta Persistence
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.persistence;
    requires java.naming;

    // open to JavaFX
    opens org.example.trabalhosemestralpoo2025 to javafx.fxml;

    // open entities to Hibernate (reflection required for @Entity classes)
    opens model to org.hibernate.orm.core, javax.persistence;

    exports org.example.trabalhosemestralpoo2025;
    opens view to javafx.fxml, javafx.graphics;
    opens controller to javafx.fxml, javafx.graphics;
    exports view;
    exports controller;
}
