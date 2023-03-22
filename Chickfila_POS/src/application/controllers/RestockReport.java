package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class RestockReport {

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Button back_button;

    @FXML
    private TableColumn<?, ?> col_ingredient;

    @FXML
    private TableColumn<?, ?> col_qty_curr;

    @FXML
    private TableColumn<?, ?> col_threshold;

    @FXML
    private Button refresh_button;

    @FXML
    private TableView<?> restock_table;

    @FXML
    private Text time_refreshed;

    @FXML
    void GoBack(ActionEvent event) {

    }

    @FXML
    void handleButtonAction(ActionEvent event) {

    }

    @FXML
    void handleMouseAction(MouseEvent event) {

    }

}
