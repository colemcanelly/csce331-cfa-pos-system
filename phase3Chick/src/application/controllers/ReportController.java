package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ReportController {
	
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
    private Button back_button;

    @FXML
    private Button excess_report;

    @FXML
    private Button restock_report_button;

    @FXML
    private Button sales_report_button;

    @FXML
    private Button xz_report_button;

    @FXML
    void SwitchToExcessReportPanel(ActionEvent event) {
    	try {
 		   
 		   Parent root = FXMLLoader.load(getClass().getResource("../fxml/ManagerExcessReport.fxml"));
 		   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
 		   
 		  } catch(Exception e) {
 		   e.printStackTrace();
 		  }
    }

    @FXML
    void SwitchToHome(ActionEvent event) {
    	try {
 		   
 		   Parent root = FXMLLoader.load(getClass().getResource("../fxml/Home.fxml"));
 		   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
 		   
 		  } catch(Exception e) {
 		   e.printStackTrace();
 		  }
    }

    @FXML
    void SwitchToRestockReport(ActionEvent event) {
    	try {
 		   
 		   Parent root = FXMLLoader.load(getClass().getResource("../fxml/RestockReport.fxml"));
 		   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
 		   
 		  } catch(Exception e) {
 		   e.printStackTrace();
 		  }
    }

    @FXML
    void SwitchToXZReportPanel(ActionEvent event) {
    	try {
 		   
 		   Parent root = FXMLLoader.load(getClass().getResource("../fxml/XZReport.fxml"));
 		   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
 		   
 		  } catch(Exception e) {
 		   e.printStackTrace();
 		  }
    }

    @FXML
    void SwithToSalesReportPanel(ActionEvent event) {
    	try {
 		   
 		   Parent root = FXMLLoader.load(getClass().getResource("../fxml/salesreport.fxml"));
 		   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
 		   
 		  } catch(Exception e) {
 		   e.printStackTrace();
 		  }
    }


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
