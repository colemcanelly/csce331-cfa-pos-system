package application;
	
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

/**
 * This is the main controller used for our javaFX file
 * 
 * @author      Weston Cadena <westoncadena@gmail.com>
 * @version     1.3                 (current version number of program)
 * @since       1.0          (the version of the package this class was first added to)
 */
public class Main extends Application {

		/**
	 * Starts the javafx program.
	 *
	 *
	 * @param primaryStage Stage that which the scenes will be displayed on
	 * @since             1.0
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("fxml/Home.fxml"));
			primaryStage.setTitle("Manager Dashboard");
			primaryStage.setScene(new Scene(root, 939, 427));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

			/**
	 * Main for the javafx program.
	 *
	 *
	 * @param args Arguements that are passed in via command line when running code
	 * @since             1.0
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
