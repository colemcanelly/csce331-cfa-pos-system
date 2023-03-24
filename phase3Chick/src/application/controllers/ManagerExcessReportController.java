package application.controllers;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.print.Printable;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * ManagerExcessReportController contains the java needed to produce the Excess Report for our POS system. This class will display items that
 * did not sell more than 10 percent of there inventory over a centain amount of time
 * 
 * @author      Weston Cadena <westoncadena@gmail.com>
 * @version     1.3                 (current version number of program)
 * @since       1.3          (the version of the package this class was first added to)
 */
public class ManagerExcessReportController implements Initializable {

    private PostgreSQL psql = new PostgreSQL();
    private CFADataBase db = new CFADataBase();

    @FXML
    private Button back_button;

    @FXML
    private Button calculate_button;

    @FXML
    private DatePicker date_selected;

    @FXML
    private TableColumn<ExcessItem, ?> percent_difference_col;

    @FXML
    private TableColumn<ExcessItem, ?> quantity_sold_col;

    @FXML
    private TableColumn<ExcessItem, ?> start_quantity_col;
    
    @FXML
    private TableColumn<ExcessItem, ?> ingredient_col;
    

    @FXML
    private TextField category_box;

    @FXML
    private TextField date_box;

    @FXML
    private TextField ingredient_box;


    @FXML
    private TableView<ExcessItem> excess_table;

    @FXML
    private AnchorPane menu_pane;

    @FXML
    private AnchorPane menu_pane1;

    @FXML
    private TextField supply_box;

    @FXML
    private Button update_button;
    
    private Stage stage;
	private Scene scene;
	private Parent rootParent;
	private Node oldNode;
	
	private String currDateString;
    
	
    /**
	 * Function to return to the previous fxml file in the project. In this instance it is Report.fxml
	 *
	 *
	 * @param event Action event when a button is pressed
	 * @since             1.3
	 */
    @FXML
    void GoBack(ActionEvent event) {
    	try {
            // set the oldNode variable to the button that was clicked
            oldNode = (Node) event.getSource();
            Stage previousStage = (Stage) oldNode.getScene().getWindow();
            previousStage.close();
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/Report.fxml"));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
    * Collects date after the calulate excess button is pressed
    *
    *
    * @param event Action event when a button is pressed
    * @since             1.3
    */
    @FXML
    void handleButtonAction(ActionEvent event) {
        if(event.getSource() == calculate_button){
        	LocalDate date = date_selected.getValue();
            updateExcessTable(date);
        } 
    }

    
    @FXML
    void handleMouseAction(MouseEvent event) {
        
        LocalDate date = date_selected.getValue();
        getExcessItemsList(date);
        
    }


/**
 * Creates an observable list to display in the table.
 *
 * <p>Use {@link #doMove(int fromFile, int fromRank, int toFile, int toRank)} to move a piece.
 *
 * @param date date used for the sql command
 * @return            obserable list to display in the GUI
 * @since             1.3
 */
public ObservableList<ExcessItem> getExcessItemsList(LocalDate date){
    	
        ObservableList<ExcessItem> Excess_Items_List = FXCollections.observableArrayList();
        
        Map<String, Map<String, String>> excess_items = db.getExcessReport(date);
        for(Map<String, String> item : excess_items.values()){
            String ingredient = item.get("ingredient");
            String start_quantity = item.get("qty_sod");
            String quantity_sold = item.get("total_qty_sold");
            String percent_difference = item.get("percentage_diff");
            System.out.println("Ingredient: " + ingredient + ", start_quantity: " + start_quantity + ", quantity_sold: " + quantity_sold + ", percent_difference: " + percent_difference);
            ExcessItem ExcessItem = new ExcessItem(ingredient, start_quantity, quantity_sold, percent_difference);
            Excess_Items_List.add(ExcessItem);
        }

        return Excess_Items_List;
    }


    /**
     * Updates the table with the obserable list created by getExcessItemsList
     *
     * @param date date used for sql command to get excess items
     * @since             1.3
     */
   private void updateExcessTable(LocalDate date){
	   
        excess_table.setItems(getExcessItemsList(date));
        
    }
    
    public void initialize(URL url, ResourceBundle rb){
    	ingredient_col.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
    	start_quantity_col.setCellValueFactory(new PropertyValueFactory<>("start_quantity"));
    	quantity_sold_col.setCellValueFactory(new PropertyValueFactory<>("quantity_sold"));
        percent_difference_col.setCellValueFactory(new PropertyValueFactory<>("percent_difference"));
        
        if (excess_table != null) {
            // check if the columns already exist
            if (!excess_table.getColumns().contains(ingredient_col)) {
                excess_table.getColumns().add(ingredient_col);
            }
            if (!excess_table.getColumns().contains(start_quantity_col)) {
                excess_table.getColumns().add(start_quantity_col);
            }
            if (!excess_table.getColumns().contains(quantity_sold_col)) {
                excess_table.getColumns().add(quantity_sold_col);
            }
            if (!excess_table.getColumns().contains(percent_difference_col)) {
                excess_table.getColumns().add(percent_difference_col);
            }
            // set the data source for the table view
            //excess_table.setItems(getInventoryList());
            
        }
        else {
            System.out.println("excess_table is null!");
        }
    }

}