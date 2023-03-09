package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


/** * Central class that links the FXML, CSS, and controller files to display the GUI.
 * The class contains a start and main method. start() builds the GUI while the main 
 * method actually launches it.
 * <p>
 * The GUI will be initialized with some components, such as the menu items and the date/time.
 *
 */
public class Customer extends Application {
	
	/** * Builds the GUI to be displayed.
	  * <p>
	  * start() creates a GUI frame for all components to be housed and sizes 
	  * it correctly, attaching the FXML file provided in the directory to be 
	  * loaded up. It then styles the frame by passing in the CSS file attached,  
	  * sets it all up and displays. An error message is printed if the program
	  * fails.
	  * 
	  * @param  primaryStage   the top-level GUI container to store all components
	  */
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Customer.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("Customer.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** * Launches the already built GUI; just the main method to execute the start method
	  *
	  * @param  args   command line arguments
	  */
	public static void main(String[] args) {
		launch(args);
	}
}
