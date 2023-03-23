package application.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SalesReport extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load PostgreSQL JDBC driver.");
            e.printStackTrace();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(SalesReport.class.getResource("salesreport.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 939, 427);
        stage.setTitle("Chick-fil-A Sales Report");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}