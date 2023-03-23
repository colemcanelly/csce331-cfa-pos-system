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
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ManagerMenuController implements Initializable {

    private PostgreSQL psql = new PostgreSQL();
    private CFADataBase db = new CFADataBase();

    String[] ingredients = {"fried_batter", "bun", "chicken_patty", "pickle_chips", "lettuce", "tomato", "cheese", "spice", "chicken_nuggets",
            "chicken_strips", "flat_bread", "mixed_greens", "shredded_monterey_jack", "waffle_fries", "roasted_corn_kernels", "crumbled_bacon",
            "avacado_lime_dressing", "red_bell_pepers", "grape tomatoes", "hard_boiled_egg", "blue_cheese", "apples", "strawberries", "blueberries",
            "harvest_nut_granola", "zesty_apple_vinagrette", "chicken_tenders", "biscuit", "chicken_mini_bread", "butter", "bacon", "tortilla", "eggs",
            "sausage", "bagel", "oatmeal", "fruit", "cream_cheese", "hash_brown", "yogurt_parfait", "cinamon_cluster", "lemonade", "straw",
            "small_drink_cup", "medium_drink_cup", "large_drink_cup", "lemonade_diet", "ice_tea_unsweet", "ice_tea_sweet", "soft_drink",
            "dasani_water_bottle", "orange_juice_bottle", "apple_juice", "ice_coffee", "coffee", "Coffee_box", "milk_chocolate", "milk",
            "icecream", "vanilla_flavor", "cream_flavor", "chunk_cookie", "chocolate_flavor", "strawberry_flavor", "frosted_extract",
            "icecream_cone", "icecream_come", "icecream_topping", "to_go_bag", "napkins", "ketchup", "ranch", "buffalo_sauce", "bbq_sauce",
            "polynesian", "chickfila", "silverware"};


    private Stage stage;
    private Scene scene;
    private Parent rootParent;
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
    private ListView<String> ingredientView;

    @FXML
    private Button ingredient_button;
    
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


    @FXML
    void handleButtonAction(ActionEvent event) {
        if(event.getSource() == add_button){
            addMenuItem();
        }else if (event.getSource() == update_button){
            updateMenuItem();
        }
        else if (event.getSource() ==remove_button){
            removeMenuItem();
        } else if (event.getSource() == ingredient_button) {
            addIngredient();
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

        ingredientView.getItems().addAll(ingredients);
        // set the data source for the table view
        menu_table.setItems(getMenuList());
    }

    public ObservableList<MenuItem> getMenuList(){
        ObservableList<MenuItem> menuList = FXCollections.observableArrayList();
        Map<String, Map<String, String>> menu = db.get(CFADataBase.Table.MENU);
        for(Map<String, String> item : menu.values()){
            String menu_item = item.get("menu_item");
            String food_price = item.get("food_price");
            String combo = item.get("combo");
            String category = item.get("menu_cat");
//            System.out.println("menu_item: " + menu_item + ", food_price: " + food_price + ", combo: " + combo + ", category: " + category);
            MenuItem menuItem = new MenuItem(menu_item, Float.parseFloat(food_price), combo, category);
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
                + Float.parseFloat(price_box.getText())+ ", '" + combo_box.getText() + "', '"+ category_box.getText() +"');";
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
    /**
     * This function is implemented whenever the remove button is clicked on. This remove the menu item specified.
     * @author Ryan Paul
     */
    private void removeMenuItem(){
        String query1 = "DELETE FROM recipes WHERE menu_item = '" + item_box.getText() + "';";
        String query2 = "DELETE FROM menu WHERE menu_item  = '" + item_box.getText() + "';";
        psql.query(query1);
        psql.query(query2);
        menu_table.setItems(getMenuList());
    }
    /**
     * This function is implemented whenever the ingredient button is clicked on. This adds ingredient to a menu item.
     * @author Ryan Paul
     */
    private void addIngredient(){
        System.out.println(ingredientView.getSelectionModel().getSelectedItem());
        String query = "INSERT INTO recipes (menu_item, ingredient, portion_count) VALUES ('" + item_box.getText() +"', '" +
        ingredientView.getSelectionModel().getSelectedItem() +"', 1);";
        psql.query(query);
    }
    }