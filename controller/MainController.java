package controller;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane pnMain;

    @FXML
    private Button btnOrder;

    @FXML
    private Button btnProcessing;

    @FXML
    private Button btnHistory;

    @FXML
    private Button btnItem;
    @FXML
    private MaterialDesignIconView iconItem;

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
    private AnchorPane pnTop;

    @FXML
    private MaterialDesignIconView btnClose;

    @FXML
    private MaterialDesignIconView btnMinimize;

    @FXML
    private AnchorPane pnStack;

    private VBox pnItem;
    private VBox pnOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader fxmlLoaderItem = new FXMLLoader();
            fxmlLoaderItem.setLocation(getClass().getResource("../fxml/Item.fxml"));
            pnItem = fxmlLoaderItem.load();

            FXMLLoader fxmlLoaderOrder = new FXMLLoader();
            fxmlLoaderOrder.setLocation(getClass().getResource("../fxml/Order.fxml"));
            pnOrder = fxmlLoaderOrder.load();

            pnStack.getChildren().add(pnItem);
            pnStack.getChildren().add(pnOrder);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleClose(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == btnClose) {
            System.exit(0);
        }
        if (mouseEvent.getSource() == btnMinimize) {
            Stage stage = (Stage) pnMain.getScene().getWindow();
            stage.setIconified(true);
        }
    }

    public void handleNavigation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == btnItem) {
            btnItem.setStyle("-fx-background-color: white");
            iconItem.setFill(Color.rgb(0, 176, 240));
            pnItem.toFront();
        } else if (mouseEvent.getSource() == btnOrder) {
            pnOrder.toFront();
        }
    }

}
