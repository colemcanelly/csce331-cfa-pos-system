module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens menutable to javafx.fxml;
    exports menutable;
}