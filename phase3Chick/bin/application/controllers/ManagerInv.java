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
import java.util.Map;
import java.util.ResourceBundle;

public class ManagerInv implements Initializable {

    private PostgreSQL psql = new PostgreSQL();
    private CFADataBase db = new CFADataBase();

    @FXML
    private Button back_button;

    @FXML
    private TextField category_box;

    @FXML
    private TableColumn<IngredientItem, ?> current_date;

    @FXML
    private TextField date_box;

    @FXML
    private TextField ingredient_box;

    @FXML
    private TableColumn<IngredientItem, ?> ingredient_col;

    @FXML
    private TableView<IngredientItem> ingredient_table;

    @FXML
    private AnchorPane menu_pane;

    @FXML
    private AnchorPane menu_pane1;

    @FXML
    private TextField supply_box;

    @FXML
    private TableColumn<IngredientItem, ?> supply_col;

    @FXML
    private Button update_button;
    
    private Stage stage;
	private Scene scene;
	private Parent rootParent;
	private Node oldNode;
	
	private String currDateString;
    
    @FXML
    void GoBack(ActionEvent event) {
    	try {
            // set the oldNode variable to the button that was clicked
            oldNode = (Node) event.getSource();
            Stage previousStage = (Stage) oldNode.getScene().getWindow();
            previousStage.close();
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/Home.fxml"));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleButtonAction(ActionEvent event) {
        if(event.getSource() == update_button){
            updateSupply();
        } 
    }

    @FXML
    void handleMouseAction(MouseEvent event) {
        IngredientItem item = (IngredientItem) ingredient_table.getSelectionModel().getSelectedItem();
        ingredient_box.setText(""+ item.getIngredient());
        supply_box.setText(""+item.getSupply());
        date_box.setText(""+item.getCurr_date());

    }



    public ObservableList<IngredientItem> getInventoryList(){
    	
        ObservableList<IngredientItem> Ingredient_Supply_List = FXCollections.observableArrayList();
        
        Map<String, Map<String, String>> daily_inv = db.getNewestInv();
        for(Map<String, String> item : daily_inv.values()){
            String ingredeint = item.get("ingredient");
            String qty_eod = item.get("qty_eod");
            String curr_date = item.get("entry_date");
            currDateString = curr_date;
            System.out.println("Ingredient: " + ingredeint + ", qty_eod: " + qty_eod + ", curr_date: " + curr_date);
            IngredientItem IngredientItem = new IngredientItem(ingredeint, qty_eod, curr_date);
            Ingredient_Supply_List.add(IngredientItem);
        }

        return Ingredient_Supply_List;
    }


    /**
     * This function is implemented whenever the update button is clicked on. This updates the menu given the current menu item with
     * its price.
     * @author Ryan Paul
     */
   private void updateSupply(){
//        String old_value = ingredient_box.getText();
	   Float updated_valFloat = Float.parseFloat(supply_box.getText()) + Float.parseFloat(category_box.getText());
	   currDateString = date_box.getText();
        String query = "UPDATE daily_inventory SET ingredient = '" + ingredient_box.getText() + "', qty_eod = '" + updated_valFloat  +
        		"' WHERE ingredient = '" + ingredient_box.getText() + "' AND entry_date = '" + currDateString  + "';";
        System.out.print(query);
        psql.query(query);
        ingredient_table.setItems(getInventoryList());
    }
    
    public void initialize(URL url, ResourceBundle rb){
    	ingredient_col.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        supply_col.setCellValueFactory(new PropertyValueFactory<>("supply"));
        current_date.setCellValueFactory(new PropertyValueFactory<>("curr_date"));
        if (ingredient_table != null) {
            // check if the columns already exist
            if (!ingredient_table.getColumns().contains(ingredient_col)) {
                ingredient_table.getColumns().add(ingredient_col);
            }
            if (!ingredient_table.getColumns().contains(supply_col)) {
                ingredient_table.getColumns().add(supply_col);
            }
            if (!ingredient_table.getColumns().contains(current_date)) {
                ingredient_table.getColumns().add(current_date);
            }
            // set the data source for the table view
            ingredient_table.setItems(getInventoryList());
            
        }
//        } else {
//            System.out.println("ingredient_table is null!");
//        }
    }

}