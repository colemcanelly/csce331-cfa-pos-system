package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String dt = dtf.format(now); 
		String[] dt_arr = dt.split("\\s");
		dateLbl.setText(dt_arr[0]);
		timeLbl.setText(dt_arr[1]);
		
		Image cfa_logo = new Image("cfa-logo.png");
		
		cfaLogoImg.setImage(cfa_logo);
		
		Map<String, Map<String, String>> menu = db.getMenu();
		
		
		VBox vbox = new VBox();
		
		vbox.setTranslateX(10.0);
		vbox.setTranslateY(10.0);
		
		
		
		
		
		
		System.out.println("entering for loop");
		for (Map<String, String> item : menu.values()) {
			String menu_item = item.get("menu_item");
			String food_price = item.get("food_price");
			String is_combo = item.get("combo");
			System.out.println(menu_item + " | " + food_price + " | " + is_combo);
			BorderPane newBorderPane = createMenuItem(menu_item, food_price);
			
			vbox.getChildren().add(newBorderPane);
			
		}
		
		scrollPane.setContent(vbox);
		
		
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
    private ScrollPane scrollPane;
    
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
