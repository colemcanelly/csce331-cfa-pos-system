package application.controllers;


import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.HashMap;
import java.util.ArrayList;
import java.sql.*;
import java.util.Map;
import java.util.Vector;

import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import static java.util.Map.entry;
/**
 * @author      Weston Cadena <westoncadena@gmail.com>
 * @version     1.3                 (current version number of program)
 * @since       1.1          (the version of the package this class was first added to)
 * ManagerIngredientController contains the java needed to interact with the manager inventory fxml file. The manager
 * will be able to update each ingredient's current amount
 */
public class ManagerIngredientController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent rootParent;
	

    @FXML
    private Text current_amount;
	
	@FXML
	private Label stock_graph_title;
	
	@FXML
	private ChoiceBox<String> ingredient_choice_box;

	@FXML
	private ChoiceBox<String> ingredient_choice_box1;
	
    @FXML
    private Button update_button;
	
	@FXML
    private ChoiceBox<String> menu_item_choice_box;
	
	@FXML
    private Label daily_orders;

    @FXML
    private Label daily_sales;

    @FXML
    private Button home_button;

    @FXML
    private ListView<?> restock_list;
    
    @FXML
    private Label need_restock;

    @FXML
    private Button restock_button;

    @FXML
    private Label sold_graph_title;
    
    @FXML
    private LineChart<?, ?> ingredient_graaph;
    
    @FXML
    private Text current_quantity;

    @FXML
    private LineChart<?, ?> menu_graph;


	/**
	 * return_home sets the stage back to the home.fxml
	 *
	 *
	 * @param event action event that happens when the back button is interacted with in the fxml file
	 * @since             1.1
	 */
    @FXML
    void return_home(ActionEvent event) {
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
	
	// Variables used to get data from menu and ingredients
	Vector<String> menuStrings = new Vector<String>();
	Vector<String> ingredientStrings = new Vector<String>();
	Vector<Integer> thresholdIntegers = new Vector<Integer>();
	
	
	private static final String MENU_QUERY = "SELECT * FROM menu";
	private static final String SUPPLY_QUERY = "SELECT * FROM supply";
	
	

    private Map<String, Map<String, String>> menu = null;
    
   
    private Map<String, Map<String, String>> supplyMap = null;
    private Map<String, Map<String, String>> eodMap = null;
    private Map<String, Double> currentQuantitiesMap = null;
    
    
    
    
    // Initialization of backend classes
    PostgreSQL psql = new PostgreSQL();
    CFADataBase database = new CFADataBase();
    
	
	/**
	 * Pouplates the hashmaps needed to populate the table on the front end, uses functions from the PostgreSQL.java file
	 *
	 *
	 * @since             1.1
	 */
	public ManagerIngredientController()
    {
		// Populate the Hashmaps
        menu = database.get(CFADataBase.Table.MENU);
        supplyMap = database.get(CFADataBase.Table.SUPPLY);
        eodMap = database.get(CFADataBase.Table.DAILY_INV);
        
    }
	
	/**
	 * Initalizes the 
	 *
	 *
	 * @param arg0 URL arguement passed in when originally initialized
	 * @param arg1 Rescources passed in to correctly launch the fxml file
	 * @since             1.1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Populate the two Choice Boxes
		for (Map.Entry<String,Map<String, String>> entry : menu.entrySet()) {
			menuStrings.add(entry.getValue().get("menu_item"));
			//System.out.println(entry.getValue().get("menu_item"));
		}
		for (Map.Entry<String,Map<String, String>> entry : supplyMap.entrySet()) { 
			ingredientStrings.add(entry.getValue().get("ingredient"));
			System.out.println(entry.getValue().get("ingredient"));
		}	
		
		ingredient_choice_box.getItems().addAll(ingredientStrings);
		ingredient_choice_box1.getItems().addAll(ingredientStrings);
		menu_item_choice_box.getItems().addAll(menuStrings);
		
		iniMenuGraph();
		iniSupplyGraph();
		//ingredient_choice_box.setOnAction(new );
	}
	

	/**
	 * This Function Initializes and populates the ingredient graph. It will Update the graph based on the selection that the manager inputs
	 *
	 *
	 * @param event Arguements that are passed in via command line when running code
	 * @since             1.1
	 */
    @FXML
    void updateSupplyGraph(MouseEvent event) {
    	String ingredient_item = ingredient_choice_box.getValue();
		stock_graph_title.setText(ingredient_item + "'s Stock");
    }
    
    
    @FXML
    void ChangeCurrentValue(MouseEvent event) {
    	String ingredient_item = ingredient_choice_box1.getValue();
    	String eod_quantityString = eodMap.get(ingredient_item).get("qty_eod");
    	current_quantity.setText(ingredient_item);
    	
    }
    
    /**
	 * This Function Initializes and populates the menu graph. It will Update the graph based on the selection that the manager inputs
	 */
    @FXML
    void updateMenuGraph(MouseEvent event) {
    	String menu_item = menu_item_choice_box.getValue();
		stock_graph_title.setText(menu_item + " Sold");
    }
    
    private void itemsToRestock() {
    	for (Map.Entry<String,Map<String, String>> entry : supplyMap.entrySet()) {
    		String ingredient_nameString = entry.getValue().get("ingredient");
    		//if (current_quantities.get(ingredient_nameString) < entry.getValue().get("ingredient")) {
    			//restock_List.getItems().addAll(ingredientStrings);
    		//}
		}
    	//restock_List.getItems().addAll("some new element");
    }


    private void displayCurrentAmount() {
    	String ingredient_selected = ingredient_choice_box1.getValue();
    }
    
	/**
	 * This Function Initializes the supply graph when first called
	 *
	 *
	 * @since             1.1
	 */
	private void iniSupplyGraph() {
		XYChart.Series series = new XYChart.Series();
		long millis=System.currentTimeMillis();  
        java.sql.Date date1=new java.sql.Date(millis);  
		series.getData().add(new XYChart.Data("Wednesday",56.6));
		series.getData().add(new XYChart.Data("Tuesday",72.3));
		ingredient_graaph.setAnimated(false);
		ingredient_graaph.getData().addAll(series);
		ingredient_graaph.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
		series.getNode().setStyle("-fx-stroke: #FFd6DC");
	}
	
    private void iniMenuGraph() {
//		
	}
	
	
	
}
