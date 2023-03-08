package menutable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ManagerMenuController {

    @FXML
    private Button add_button;

    @FXML
    private TextField item_box;

    @FXML
    private TableColumn<MenuItem, String> menu_item;

    @FXML
    private AnchorPane menu_pane;

    @FXML
    private AnchorPane menu_pane1;

    @FXML
    private TableColumn<MenuItem,Float> menu_price;

    @FXML
    private TableView<MenuItem> menu_table;

    @FXML
    private TextField price_box;

    @FXML
    private Button remove_button;

    @FXML
    private VBox vBox;

//    @FXML
//    void addMenuItem(MouseEvent event) {
//
//    }
//
//    @FXML
//    void removeMenuItem(MouseEvent event) {
//
//    }
//
//    @FXML
//    void rowSelected(MouseEvent event) {
//        MenuItem clickItem = menu_table.getSelectionModel().getSelectedItem();
//        menu_item.setText(String.valueOf(clickItem.getMenu_item_name()));
//        menu_price.setText(String.valueOf(clickItem.getItem_price()));
//    }

}
