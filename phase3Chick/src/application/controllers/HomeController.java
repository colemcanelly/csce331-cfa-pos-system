package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Used to controll the Home GUI landing page
 * 
 * @author      Weston Cadena <westoncadena@gmail.com>
 * @version     1.3                 (current version number of program)
 * @since       1.0          (the version of the package this class was first added to)
 */
public class HomeController {
	
	private Stage stage;
	private Scene scene;
	private Parent rootParent;

    @FXML
    private Button manager_inventory_button;

    @FXML
    private Button manager_menu_button;

    @FXML
    private Button server_button;
    

    @FXML
    private Button report_button;


    @FXML
    void SwithToManagerInventoryPanel(ActionEvent event) {
    	  try {
    		   
    		   Parent root = FXMLLoader.load(getClass().getResource("../fxml/ManagerInv.fxml"));
    		   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
   				scene = new Scene(root);
   				stage.setScene(scene);
   				stage.show();
    		   
    		  } catch(Exception e) {
    		   e.printStackTrace();
    		  }
    }

    @FXML
    void SwitchToManagerMenuPanel(ActionEvent event) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/ManagerMenu.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void SwitchToServerPanel(ActionEvent event) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Customer.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void SwitchToReport(ActionEvent event) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Report.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    

}
