package controller;

import connect.DBConnect;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private MaterialDesignIconView iconOrder;

    @FXML
    private Button btnProcessing;
    @FXML
    private MaterialDesignIconView iconProcessing;

    @FXML
    private Button btnHistory;
    @FXML
    private MaterialDesignIconView iconHistory;

    @FXML
    private Button btnItem;
    @FXML
    private MaterialDesignIconView iconItem;

    @FXML
    private Button btnStaff;
    @FXML
    private MaterialDesignIconView iconStaff;

    @FXML
    private Button btnShift;
    @FXML
    private MaterialDesignIconView iconShift;

    @FXML
    private Button btnPromo;
    @FXML
    private MaterialDesignIconView iconPromo;

    @FXML
    private Button btnAttendance;
    @FXML
    private MaterialDesignIconView iconAttendance;

    @FXML
    private Button btnSalary;
    @FXML
    private MaterialDesignIconView iconSalary;

    @FXML
    private Button btnStatistics;
    @FXML
    private MaterialDesignIconView iconStatistics;

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

    private AnchorPane pnItem;
    private AnchorPane pnStaff;
    private AnchorPane pnOrder;

    public static DBConnect connect = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader fxmlLoaderItem = new FXMLLoader();
            fxmlLoaderItem.setLocation(getClass().getResource("../fxml/item2.fxml"));
            pnItem = fxmlLoaderItem.load();

            FXMLLoader fxmlLoaderOrder = new FXMLLoader();
            fxmlLoaderOrder.setLocation(getClass().getResource("../fxml/order.fxml"));
            pnOrder = fxmlLoaderOrder.load();

            FXMLLoader fxmlLoaderStaff = new FXMLLoader();
            fxmlLoaderStaff.setLocation(getClass().getResource("../fxml/staff.fxml"));
            pnStaff = fxmlLoaderStaff.load();

            pnStack.getChildren().add(pnStaff);
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
        if (mouseEvent.getSource() == btnOrder) {
            pnOrder.toFront();
        } else if (mouseEvent.getSource() == btnProcessing) {
        } else if (mouseEvent.getSource() == btnHistory) {
        } else if (mouseEvent.getSource() == btnItem) {
            pnItem.toFront();
        } else if (mouseEvent.getSource() == btnStaff) {
            pnStaff.toFront();
        } else if (mouseEvent.getSource() == btnShift) {
        } else if (mouseEvent.getSource() == btnPromo) {
        } else if (mouseEvent.getSource() == btnAttendance) {
        } else if (mouseEvent.getSource() == btnSalary) {
        } else if (mouseEvent.getSource() == btnStatistics) {
        }
    }



}
