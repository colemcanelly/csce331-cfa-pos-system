module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens menutable to javafx.fxml;
    exports menutable;
}