package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnOrder;

    @FXML
    private Button btnProcessing;

    @FXML
    private Button btnHistory;

    @FXML
    private Button btnItem;

    @FXML
    private Button btnStaff;

    @FXML
    private Button btnShift;

    @FXML
    private Button btnPromo;

    @FXML
    private Button btnAttendance;

    @FXML
    private Button btnSalary;

    @FXML
    private Button btnStatistics;

    @FXML
    private StackPane stackPane;

    @FXML
    private VBox pnVBox;

    @FXML
    private HBox pnHBox;

    @FXML
    private StackPane pnStack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnOrder.setText("test");
    }
}
