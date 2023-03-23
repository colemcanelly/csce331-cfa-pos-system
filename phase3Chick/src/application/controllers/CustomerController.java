package application.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.Float;



public class CustomerController implements Initializable {
	
	private ArrayList<String> order_items = new ArrayList<String>();
	private CFADataBase db = new CFADataBase();
	private int receiptIndex = 0;
	private Float totalPrice = (float) 0.0;
	
	public BorderPane createMenuItem(String foodItem, String price) {	// TODO: add image url argument
		
		BorderPane to_return = new BorderPane();
		
		// label for the title
		Label foodItemLbl = new Label(foodItem);
		foodItemLbl.setAlignment(Pos.CENTER);
		to_return.setTop(foodItemLbl);
		
		// label for the price
		Label priceLbl = new Label(price);
		priceLbl.setAlignment(Pos.CENTER);
		to_return.setBottom(priceLbl);
		
//		// image view for the picture
//		ImageView imageView = new ImageView(new Image(picture_url));
//		imageView.setFitWidth(100);
//		imageView.setFitHeight(100);
//		imageView.setPreserveRatio(true);
//		to_return.setCenter(imageView);
		
		
		EventHandler<MouseEvent> borderPaneClicked = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				BorderPane ClickedBorderPane = (BorderPane) event.getSource();
				// TODO: Add order to list
				Label foodItem = (Label) ClickedBorderPane.getTop();
				Label priceAmt = (Label) ClickedBorderPane.getBottom();
				
				Label receiptEntry = new Label(foodItem.getText() + " / " + priceAmt.getText());
				
				receiptGrid.add(receiptEntry, 0, receiptIndex);
				receiptIndex++;
				
				totalPrice = totalPrice + Float.parseFloat(priceAmt.getText());
				totalPriceLbl.setText(totalPrice.toString());
				
				System.out.println(foodItem.getText() + " was added to order");

				//updating database
				// add combo to array list
				order_items.add(foodItem.getText());
				
			}
		};
		
		to_return.setOnMouseClicked(borderPaneClicked);
		
		return to_return;
	}
	
	private Node oldNode;
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
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String dt = dtf.format(now); 
		String[] dt_arr = dt.split("\\s");
		dateLbl.setText(dt_arr[0]);
		timeLbl.setText(dt_arr[1]);
		
		//Image cfa_logo = new Image("../../img/cfa-logo.png");
		
		//cfaLogoImg.setImage(cfa_logo);
		
		Map<String, Map<String, String>> menu = db.get(CFADataBase.Table.MENU);
		
		
		VBox vboxCombos = new VBox();
		VBox vboxBreakfast = new VBox();
		VBox vboxDrinks = new VBox();
		VBox vboxDesserts = new VBox();
		VBox vboxCondiments = new VBox();
		VBox vboxSeasonal = new VBox();
		
		vboxCombos.setTranslateX(10.0);
		vboxCombos.setTranslateY(10.0);
		vboxBreakfast.setTranslateX(10.0);
		vboxBreakfast.setTranslateY(10.0);
		vboxDrinks.setTranslateX(10.0);
		vboxDrinks.setTranslateY(10.0);
		vboxDesserts.setTranslateX(10.0);
		vboxDesserts.setTranslateY(10.0);
		vboxCondiments.setTranslateX(10.0);
		vboxCondiments.setTranslateY(10.0);
		vboxSeasonal.setTranslateX(10.0);
		vboxSeasonal.setTranslateY(10.0);
		
		
		
		
		
		
		System.out.println("entering for loop");
		for (Map<String, String> item : menu.values()) {
			String menu_item = item.get("menu_item");
			String food_price = item.get("food_price");
			String is_combo = item.get("combo");
			String menu_cat = item.get("menu_cat");
			System.out.println(menu_item + " | " + food_price + " | " + is_combo);
			BorderPane newBorderPane = createMenuItem(menu_item, food_price);
			
			if (menu_cat.equals("main")) {
				vboxCombos.getChildren().add(newBorderPane);
			}
			else if (menu_cat.equals("breakfast")) {
				vboxBreakfast.getChildren().add(newBorderPane);
			}
			else if (menu_cat.equals("drink")) {
				vboxDrinks.getChildren().add(newBorderPane);
			}
			else if (menu_cat.equals("dessert")) {
				vboxDesserts.getChildren().add(newBorderPane);
			}
			else if (menu_cat.equals("condiment")) {
				vboxCondiments.getChildren().add(newBorderPane);
			}
			else if (menu_cat.equals("seasonal")) {
				vboxSeasonal.getChildren().add(newBorderPane);
			}
			else {
				System.out.println("not an existing menu item category");
			}
			
		}
		
		scrollPaneCombos.setContent(vboxCombos);
		scrollPaneBreakfast.setContent(vboxBreakfast);
		scrollPaneDrinks.setContent(vboxDrinks);
		scrollPaneDesserts.setContent(vboxDesserts);
		scrollPaneCondiments.setContent(vboxCondiments);
		scrollPaneSeasonal.setContent(vboxSeasonal);
		
		
	}
	
    @FXML
    private BorderPane breakfastFoodItem;

    @FXML
    private Tab breakfastTab;
    
    @FXML
    private GridPane receiptGrid;
    
    @FXML
    private GridPane menuGrid;

    @FXML
    private ImageView cfaLogoImg;

    @FXML
    private BorderPane comboFoodItem;

    @FXML
    private Tab comboTab;

    @FXML
    private GridPane comboTabGrid;
    
    @FXML
    private Label customerNameLbl;
    
    @FXML
    private ListView<String> menuList;

    @FXML
    private Label dateLbl;

    @FXML
    private BorderPane dessertFoodItem;
    
    @FXML
    private ScrollPane scrollPaneCombos;
    
    @FXML
    private ScrollPane scrollPaneBreakfast;
    
    @FXML
    private ScrollPane scrollPaneDrinks;
    
    @FXML
    private ScrollPane scrollPaneDesserts;
    
    @FXML
    private ScrollPane scrollPaneCondiments;
    
    @FXML
    private ScrollPane scrollPaneSeasonal;
    
    @FXML
    private ListView<String> receiptList;

    @FXML
    private Tab dessertTab;

    @FXML
    private Tab drinkTab;

    @FXML
    private BorderPane drinksFoodItem;

    @FXML
    private Button editItemBtn;

    @FXML
    private Button editNameBtn;

    @FXML
    private Label orderNumLbl;

    @FXML
    private Button printReceiptBtn;

    @FXML
    private Button removeItemBtn;

    @FXML
    private Button submitItem;

    @FXML
    private Button submitOrderBtn;

    @FXML
    private Label timeLbl;

    @FXML
    private Label totalPriceLbl;

    @FXML
    private Button viewAnalysisBtn;

    @FXML
    void breakfastFoodItemClick(MouseEvent event) {

    }

    @FXML
    void comboFoodItemClick(MouseEvent event) {

    }

    @FXML
    void dessertFoodItemClick(MouseEvent event) {

    }

    @FXML
    void drinksFoodItemBtn(MouseEvent event) {

    }

    @FXML
    void editItemBtnClick(MouseEvent event) {

    }

    @FXML
    void editNameBtnClick(MouseEvent event) {
    	
    }

    @FXML
    void printReceiptBtnClick(MouseEvent event) {
    	
    	
    }

    @FXML
    void removeItemBtnClick(MouseEvent event) {
    	order_items.clear();
    	receiptGrid.getChildren().clear();
    	receiptIndex = 0;
    	totalPrice = (float) 0;
    	totalPriceLbl.setText("0");
    }

    @FXML
    void submitItemBtnClick(MouseEvent event) {

    }

    @FXML
    void submitOrderBtnClick(MouseEvent event) {
    	System.out.println("order button clicked");
    	// newOrder(order_items)
    	
    	db.newOrder(order_items, "Cole McAnelly", 1, 1234);
    	receiptGrid.getChildren().clear();
    	
    	for (String s : order_items) {
    		System.out.println(s);
    	}
    	
    	totalPrice = (float) 0;
    	totalPriceLbl.setText("0");
    	order_items.clear();
    	receiptIndex = 0;
    }

    @FXML
    void viewAnalysisBtnClick(MouseEvent event) {

    }

	
    
    

}
