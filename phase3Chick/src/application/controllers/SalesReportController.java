package application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class SalesReportController implements Initializable{
    private PostgreSQL psql = new PostgreSQL();
    private CFADataBase db = new CFADataBase();
    private String startDate = "2022-09-10";
    private String endDate = "2022-09-10";
    private String startTime = "06:00:00";
    private String endTime = "22:30:00";


    @FXML
    private Button back_button;

    @FXML
    private TextField end_date_box;

    @FXML
    private TextField end_time_box;

    @FXML
    private TableColumn<SalesItem, String> menu_item_col;

    @FXML
    private AnchorPane menu_pane;

    @FXML
    private AnchorPane menu_pane1;

    @FXML
    private TableView<SalesItem> menu_table;

    @FXML
    private TextField start_date_box;

    @FXML
    private TextField start_time_box;

    @FXML
    private Button submit_button;

    @FXML
    private TableColumn<SalesItem,Float> total_sales_col;

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
    void handleSubmitButton(ActionEvent event) {
        if(event.getSource() == submit_button){
            submitButton();
        }
    }

    public void initialize(URL url, ResourceBundle rb){
        menu_item_col.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        total_sales_col.setCellValueFactory(new PropertyValueFactory<>("item_total"));

        menu_table.getColumns().add(menu_item_col);
        menu_table.getColumns().add(total_sales_col);

        menu_table.setItems(getSalesReport());
    }


    public ObservableList<SalesItem> getSalesReport(){
        ObservableList<SalesItem> report = FXCollections.observableArrayList();
        Map<String, Map<String, String>> salesReport = db.getSalesReport(startDate,endDate,startTime,endTime);
        for(Map<String, String> item : salesReport.values()){
            String menu_item = item.get("menu_item");
            String total_sales = item.get("total_revenue");
            System.out.println("menu_item: " + menu_item + ", total_revenue: " + total_sales);
            SalesItem menuItem = new SalesItem(menu_item, Float.parseFloat(total_sales));
            report.add(menuItem);
        }
        return report;
    }
    /**
     * This function is implemented whenever the submit button is clicked on, this creates a report for a specified date and time.
     * @author Ryan Paul
     */
    private void submitButton(){
        startDate = start_date_box.getText();
        endDate = end_date_box.getText();
        startTime = start_time_box.getText();
        endTime = end_time_box.getText();
        menu_table.setItems(getSalesReport());
    }


}
