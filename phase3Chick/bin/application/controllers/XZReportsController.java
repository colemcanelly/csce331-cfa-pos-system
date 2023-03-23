package application.controllers;


import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class XZReportsController {
		
	private CFADataBase db = new CFADataBase();
	
    @FXML
    private TextArea reportTextArea;

    @FXML
    private Button xReportButton;

    @FXML
    private Button zReportBtn;
    
    @FXML
    private Button back_button;
    
    private Stage stage;
	private Scene scene;
	private Parent rootParent;
    @FXML
    void SwitchToHome(ActionEvent event) {
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
    

    @FXML
    void backButton(MouseEvent event) {
    	
    }

    @FXML
    void generateXReport(MouseEvent event) {
    	String text_return = "X REPORT GENERATION\n------------------";
    	
    	// query sales since most recent Z report
    	// query: SELECT SUM(order_total) FROM orders WHERE order_id > (SELECT end_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));
    	String x_report_query = "SELECT SUM(order_total) FROM orders WHERE order_id > (SELECT end_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));";
    	String x_report_val = "";
    	try {
        	ResultSet res = db.psql.select(x_report_query);
        	res.next();
			x_report_val = ((Float) res.getFloat("sum")).toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			x_report_val = "0";
		}
    	
    	
    	// end date
    	String end_date_query = "SELECT order_date FROM orders WHERE order_id = (SELECT end_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));";
    	// execute the query
    	String end_date = "";
    	try {
        	ResultSet res = db.psql.select(end_date_query);
        	res.next();
			end_date = res.getString("order_date");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// end time
    	String end_time_query = "SELECT order_time FROM orders WHERE order_id = (SELECT end_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));";
    	String end_time = "";
    	try {
        	ResultSet res = db.psql.select(end_time_query);
        	res.next();
			end_time = res.getString("order_time");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        text_return += "\nX-REPORT, which covers all time since last Z-Report opened at " + end_time + " on " + end_date
        		+ "\nSales: $" + x_report_val;
        
        
        reportTextArea.setText(text_return);
    }

    @FXML
    void generateZReport(MouseEvent event) {
    	String text_return = "Z REPORT GENERATION\n-----------------";
    	
    	// query sales since most recent Z report
    	String z_report_query = "SELECT SUM(order_total) FROM orders WHERE order_id > (SELECT end_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));";
    	String z_report_val = "";
    	try {
        	ResultSet res = db.psql.select(z_report_query);
        	res.next();
			z_report_val = ((Float) res.getFloat("sum")).toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			z_report_val = "0";
		}
    	
    	// add new z report to database
    	String z_report_insert = "INSERT INTO z_reports(report_id, sales, start_order_id, end_order_id) VALUES ( (SELECT (MAX(report_id) + 1) FROM z_reports), (SELECT SUM(order_total) FROM orders WHERE order_id > (SELECT end_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports))), (SELECT (end_order_id + 1) FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports)), (SELECT MAX(order_id) FROM orders) );";
        if (db.psql.query(z_report_insert) != 1) {
        	System.out.println("failed to insert new z_report");
        }
    	
    	
    	// start date
    	String start_date_query = "SELECT order_date FROM orders WHERE order_id = (SELECT start_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));";
    	// execute the query
    	String start_date = "";
    	try {
        	ResultSet res = db.psql.select(start_date_query);
        	res.next();
			start_date = res.getString("order_date");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// start time
    	String start_time_query = "SELECT order_time FROM orders WHERE order_id = (SELECT start_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));";
    	String start_time = "";
    	try {
        	ResultSet res = db.psql.select(start_time_query);
        	res.next();
			start_time = res.getString("order_time");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// end date
    	String end_date_query = "SELECT order_date FROM orders WHERE order_id = (SELECT end_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));";
    	String end_date = "";
    	try {
        	ResultSet res = db.psql.select(end_date_query);
        	res.next();
			end_date = res.getString("order_date");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// end time
    	String end_time_query = "SELECT order_time FROM orders WHERE order_id = (SELECT end_order_id FROM z_reports WHERE report_id = (SELECT MAX(report_id) FROM z_reports));";
    	// execute the query
    	String end_time = "";
    	try {
        	ResultSet res = db.psql.select(end_time_query);
        	res.next();
			end_time = res.getString("order_time");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// report id 
    	String report_id_query = "SELECT MAX(report_id) FROM z_reports;";
    	// execute the query
    	String report_id = "";
    	try {
        	ResultSet res = db.psql.select(report_id_query);
        	res.next();
			report_id = res.getString("max");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	text_return += "\nZ-REPORT #" + report_id + ", which covers " + start_date + " at " + start_time + " to " + end_date + " at " + end_time
    			+ "\nSales: $" + z_report_val;
    	
    	reportTextArea.setText(text_return);
    }
    
    

}

