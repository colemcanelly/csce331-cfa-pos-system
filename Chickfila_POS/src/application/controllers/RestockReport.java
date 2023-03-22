package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.print.Printable;
import java.net.URL;
import java.sql.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.time.LocalTime;  

public class RestockReport implements Initializable {

    private CFADataBase db = new CFADataBase();
    private Stage stage;
	private Scene scene;
	private Parent rootParent;
	private Node oldNode;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Button back_button;

    @FXML
    private TableColumn<RestockItem, ?> col_ingredient;

    @FXML
    private TableColumn<RestockItem, ?> col_qty_curr;

    @FXML
    private TableColumn<RestockItem, ?> col_threshold;

    @FXML
    private Button refresh_button;

    @FXML
    private TableView<RestockItem> restock_table;

    @FXML
    private Text time_refreshed;

    @FXML
    void GoBack(ActionEvent event)
    {
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
    
    @FXML
    void refreshButton(ActionEvent event)
    {
        refreshTable();
    }
    
    private void refreshTable()
    {
        restock_table.setItems(getLowItemsList());
        time_refreshed.setText(LocalTime.now().toString());
        System.out.println("refreshed");
    }
    
    public ObservableList<RestockItem> getLowItemsList()
    {
        ObservableList<RestockItem> low_items_list = FXCollections.observableArrayList();
        Map<String, Map<String, String>> low_items = db.getRestockReport();
        for(Map<String, String> item : low_items.values()){
            String ingredient = item.get("ingredient");
            String quantity = item.get("quantity");
            String threshold = item.get("threshold");

            System.out.println("Ingredient: " + ingredient + ", quantity: " + quantity + ", threshold: " + threshold);
            
            RestockItem low_item = new RestockItem(ingredient, quantity, threshold);
            low_items_list.add(low_item);
        }
        return low_items_list;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("initializing");
    	col_ingredient.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
    	col_qty_curr.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    	col_threshold.setCellValueFactory(new PropertyValueFactory<>("threshold"));
        
        if (restock_table != null) {
            // check if the columns already exist
            if (!restock_table.getColumns().contains(col_ingredient)) {
                restock_table.getColumns().add(col_ingredient);
            }
            if (!restock_table.getColumns().contains(col_qty_curr)) {
                restock_table.getColumns().add(col_qty_curr);
            }
            if (!restock_table.getColumns().contains(col_threshold)) {
                restock_table.getColumns().add(col_threshold);
            }
            refreshTable();
            System.out.println("initialized");
        }
        else {
            System.out.println("restock_table is null!");
        }
    }

}
