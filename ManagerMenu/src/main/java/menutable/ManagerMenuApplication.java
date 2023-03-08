package menutable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerMenuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load PostgreSQL JDBC driver.");
            e.printStackTrace();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerMenuApplication.class.getResource("ManagerMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 422);
        stage.setTitle("Chick-fil-A Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}