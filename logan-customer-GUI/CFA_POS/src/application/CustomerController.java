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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;



public class CustomerController implements Initializable {
	
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
				
				receiptGrid.add(receiptEntry, 0, 0);
				
				System.out.println(foodItem.getText() + " was added to order");

				//updating database
				// add combo to array list
				
			}
		};
		
		to_return.setOnMouseClicked(borderPaneClicked);
		
		return to_return;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		ArrayList<String> order_items = new ArrayList<String>();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String dt = dtf.format(now); 
		String[] dt_arr = dt.split("\\s");
		dateLbl.setText(dt_arr[0]);
		timeLbl.setText(dt_arr[1]);
		
		Image cfa_logo = new Image("cfa-logo.png");
		
		cfaLogoImg.setImage(cfa_logo);
		
		CFADataBase db = new CFADataBase();
		Map<String, Map<String, String>> menu = db.getMenu();
		
		int col = 0;
		int row = 0;
		
		System.out.println("entering for loop");
		for (Map<String, String> item : menu.values()) {
			String menu_item = item.get("menu_item");
			String food_price = item.get("food_price");
			String is_combo = item.get("combo");
			System.out.println(menu_item + " | " + food_price + " | " + is_combo);
			BorderPane newBorderPane = createMenuItem(menu_item, food_price);
			System.out.println("created BorderPane");
			
			if (is_combo.equals("t")) {
				comboTabGrid.add(newBorderPane, col, row);
				if (col == 2 && row == 3) {
					break;
				}
				if (col == 2) {
					col = 0;
				}
				else {
					col++;
				}
				
				if (row == 3) {
					row = 0;
				}
				else {
					row++;
				}
			}
			
		}
		
	}
	
    @FXML
    private BorderPane breakfastFoodItem;

    @FXML
    private Tab breakfastTab;
    
    @FXML
    private GridPane receiptGrid;

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
    private Label dateLbl;

    @FXML
    private BorderPane dessertFoodItem;
    
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

    }

    @FXML
    void submitItemBtnClick(MouseEvent event) {

    }

    @FXML
    void submitOrderBtnClick(MouseEvent event) {
    	System.out.println("order button clicked");
    	// newOrder(order_items)
    }

    @FXML
    void viewAnalysisBtnClick(MouseEvent event) {

    }

	
    
    

}
