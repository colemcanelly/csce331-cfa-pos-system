package menutable;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ManagerMenuController implements Initializable {

    private PostgreSQL psql = new PostgreSQL();
    private CFADataBase db = new CFADataBase();

    @FXML
    private Button back_button;
    @FXML
    private Button add_button;

    @FXML
    private TextField item_box;

    @FXML
    private TextField combo_box;

    @FXML
    private TextField category_box;

    @FXML
    private TableColumn<MenuItem, String> menu_category_col;

    @FXML
    private TableColumn<MenuItem, Boolean> menu_combo_col;

    @FXML
    private TableColumn<MenuItem, String> menu_item_col;

    @FXML
    private AnchorPane menu_pane;

    @FXML
    private AnchorPane menu_pane1;

    @FXML
    private TableColumn<MenuItem, Float> menu_price_col;

    @FXML
    private TableView<MenuItem> menu_table;

    @FXML
    private TextField price_box;

    @FXML
    private Button update_button;

    @FXML
    private Button remove_button;


    @FXML
    void handleButtonAction(ActionEvent event) {
        if(event.getSource() == add_button){
            addMenuItem();
        }else if (event.getSource() == update_button){
            updateMenuItem();
        }
        else if (event.getSource() ==remove_button){
            removeMenuItem();
        }
    }

    @FXML
    void handleMouseAction(MouseEvent event) {
        MenuItem item = menu_table.getSelectionModel().getSelectedItem();
        item_box.setText(""+ item.getMenu_item_name());
        price_box.setText(""+item.getItem_price());
        combo_box.setText(""+item.getItem_combo());
        category_box.setText(""+item.getItem_category());
    }


    public void initialize(URL url, ResourceBundle rb){
        menu_item_col.setCellValueFactory(new PropertyValueFactory<>("menu_item_name"));
        menu_price_col.setCellValueFactory(new PropertyValueFactory<>("item_price"));
        menu_combo_col.setCellValueFactory(new PropertyValueFactory<>("item_combo"));
        menu_category_col.setCellValueFactory(new PropertyValueFactory<>("item_category"));

        // add the columns to the table view
        menu_table.getColumns().add(menu_item_col);
        menu_table.getColumns().add(menu_price_col);
        menu_table.getColumns().add(menu_combo_col);
        menu_table.getColumns().add(menu_category_col);

        // set the data source for the table view
        menu_table.setItems(getMenuList());
    }

    public ObservableList<MenuItem> getMenuList(){
        ObservableList<MenuItem> menuList = FXCollections.observableArrayList();
        Map<String, Map<String, String>> menu = db.getMenu();
        for(Map<String, String> item : menu.values()){
            String menu_item = item.get("menu_item");
            String food_price = item.get("food_price");
            String combo = item.get("combo");
            String category = item.get("menu_cat");
            System.out.println("menu_item: " + menu_item + ", food_price: " + food_price + ", combo: " + combo + ", category: " + category);
            MenuItem menuItem = new MenuItem(menu_item, Float.parseFloat(food_price), Boolean.parseBoolean(combo), category);
            menuList.add(menuItem);
        }

        return menuList;
    }

    /**
     * This function is implemented whenever the add button is clicked on. This adds a menu item with its price as well as the name
     * of the menu item.
     * @author Ryan Paul
     */
    private void addMenuItem(){
        String query = "INSERT INTO menu (menu_item,food_price,combo,menu_cat) VALUES ('"+ item_box.getText() +"', "
                + Float.parseFloat(price_box.getText())+ ", '" + Boolean.parseBoolean(combo_box.getText()) + "', '"+ category_box.getText() +"');";
        psql.query(query);
        menu_table.setItems(getMenuList());
    }

    /**
     * This function is implemented whenever the update button is clicked on. This updates the menu given the current menu item with
     * its price.
     * @author Ryan Paul
     */
    private void updateMenuItem(){
//        String old_value = item_box.getText();
        String query = "UPDATE menu SET menu_item  = '" + item_box.getText() + "', food_price = " + Float.parseFloat(price_box.getText()) +
                ", combo =  '" + combo_box.getText() + "' , menu_cat =  '" + category_box.getText()
                + "' WHERE menu_item = '" + item_box.getText() + "';";
        psql.query(query);
        menu_table.setItems(getMenuList());
    }

    private void removeMenuItem(){
        String query = "DELETE FROM menu WHERE menu_item  = '" + item_box.getText() + "';";
        psql.query(query);
        menu_table.setItems(getMenuList());
    }

}