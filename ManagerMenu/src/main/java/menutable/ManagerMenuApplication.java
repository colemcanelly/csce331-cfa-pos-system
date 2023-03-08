package menutable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerMenuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerMenuApplication.class.getResource("ManagerMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 725, 400);
        stage.setTitle("Chick-fil-A Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}