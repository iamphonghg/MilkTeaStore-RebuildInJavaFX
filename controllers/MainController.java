package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
    private AnchorPane pnAnchor;

    private HBox pnItem;
    private HBox pnOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader fxmlLoaderItem = new FXMLLoader();
            fxmlLoaderItem.setLocation(getClass().getResource("../fxml/Item.fxml"));
            pnItem = fxmlLoaderItem.load();

            FXMLLoader fxmlLoaderOrder = new FXMLLoader();
            fxmlLoaderOrder.setLocation(getClass().getResource("../fxml/Order.fxml"));
            pnOrder = fxmlLoaderOrder.load();

            pnAnchor.getChildren().add(pnItem);
            pnAnchor.getChildren().add(pnOrder);

            btnItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    pnItem.toFront();
                }
            });
            btnOrder.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    pnOrder.toFront();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
