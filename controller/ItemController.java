package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import main.Main;
import model.Item;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ItemController implements Initializable {
    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> cbxCategory2;

    @FXML
    private TextField txtPriceM;

    @FXML
    private TextField txtPriceL;

    @FXML
    private TextField txtPromo;

    @FXML
    private ComboBox<String> cbxCategory;

    @FXML
    private TableView<Item> tableItem;

    @FXML
    private TableColumn<Item, Integer> colNo;

    @FXML
    private TableColumn<Item, String> colName;

    @FXML
    private TableColumn<Item, Integer> colPriceM;

    @FXML
    private TableColumn<Item, Integer> colPriceL;

    @FXML
    private TableColumn<Item, String> colStatus;

    @FXML
    private TableColumn<Item, Integer> colPromo;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnOutStock;

    @FXML
    private Button btnInStock;

    @FXML
    private Button btnConfirmEdit;

    @FXML
    private Button btnCancelEdit;

    @FXML
    private Button btnConfirmAdd;

    @FXML
    private Button btnCancelAdd;

    @FXML
    private Label lblNoti;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxCategory.setItems(FXCollections.observableArrayList("MILK TEA", "FRUIT TEA", "MACCHIATO",
                "TOPPING", "FOOD"));
        cbxCategory.setFocusTraversable(false);
        cbxCategory2.setItems(FXCollections.observableArrayList("MILK TEA", "FRUIT TEA", "MACCHIATO",
                "TOPPING", "FOOD"));
        cbxCategory2.setFocusTraversable(false);
        loadItemListFromDBToTable("MILK TEA");
        handleCombobox();
    }

    public void handleCombobox() {
        cbxCategory.getSelectionModel().select(0);
        cbxCategory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selected = cbxCategory.getValue();
                switch (selected) {
                    case "MILK TEA":
                        loadItemListFromDBToTable("MILK TEA");
                        break;
                    case "MACCHIATO":
                        loadItemListFromDBToTable("MACCHIATO");
                        break;
                    case "FRUIT TEA":
                        loadItemListFromDBToTable("FRUIT TEA");
                        break;
                    case "TOPPING":
                        loadItemListFromDBToTable("TOPPING");
                        break;
                    case "FOOD":
                        loadItemListFromDBToTable("FOOD");
                        break;
                }
            }
        });
    }



    public void eventButton(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == btnAdd) {
            txtName.setText("");
            txtPriceM.setText("");
            txtPriceL.setText("");
            txtPromo.setText("");
            cbxCategory2.getSelectionModel().select(cbxCategory.getSelectionModel().getSelectedItem());
            btnConfirmAdd.setVisible(true);
            btnConfirmAdd.setDisable(false);
            btnCancelAdd.setVisible(true);
            btnCancelAdd.setDisable(false);
            btnAdd.setVisible(false);
            btnAdd.setDisable(true);
            btnEdit.setDisable(true);
            btnInStock.setDisable(true);
            btnOutStock.setDisable(true);
        } else if (mouseEvent.getSource() == btnCancelAdd) {
            btnConfirmAdd.setVisible(false);
            btnConfirmAdd.setDisable(true);
            btnCancelAdd.setVisible(false);
            btnCancelAdd.setDisable(true);
            btnAdd.setVisible(true);
            btnAdd.setDisable(false);
            btnEdit.setDisable(false);
            btnInStock.setDisable(false);
            btnOutStock.setDisable(false);
        } else if (mouseEvent.getSource() == btnConfirmAdd) {
            String name = txtName.getText();
            String priceM = txtPriceM.getText();
            String priceL = txtPriceL.getText();
            String promo = txtPromo.getText();
            String type = cbxCategory2.getSelectionModel().getSelectedItem();
            if (name.isBlank() || priceM.isBlank() || priceL.isBlank()) {
                lblNoti.setText("Can not add, please check again!");
            } else {
                int promoInt = 0;
                int priceMInt = Integer.parseInt(priceM);
                int priceLInt = Integer.parseInt(priceL);
                if (!promo.isBlank() && !promo.equals("0")) {
                    promoInt = Integer.parseInt(promo);
                }
                Item i = new Item(0, name, type, priceMInt, priceLInt, "in stock", promoInt);
                lblNoti.setText("Successfully added.");
                System.out.println(i);
            }
            btnConfirmAdd.setVisible(false);
            btnConfirmAdd.setDisable(true);
            btnCancelAdd.setVisible(false);
            btnCancelAdd.setDisable(true);
            btnAdd.setVisible(true);
            btnAdd.setDisable(false);
            btnEdit.setDisable(false);
            btnInStock.setDisable(false);
            btnOutStock.setDisable(false);
        } else if (mouseEvent.getSource() == btnEdit) {
            btnConfirmEdit.setVisible(true);
            btnConfirmEdit.setDisable(false);
            btnCancelEdit.setVisible(true);
            btnCancelEdit.setDisable(false);
            btnEdit.setVisible(false);
            btnEdit.setDisable(true);
            btnAdd.setDisable(true);
            btnInStock.setDisable(true);
            btnOutStock.setDisable(true);
        } else if (mouseEvent.getSource() == btnCancelEdit) {
            btnConfirmEdit.setVisible(false);
            btnConfirmEdit.setDisable(true);
            btnCancelEdit.setVisible(false);
            btnCancelEdit.setDisable(true);
            btnEdit.setVisible(true);
            btnEdit.setDisable(false);
            btnAdd.setDisable(false);
            btnInStock.setDisable(false);
            btnOutStock.setDisable(false);
        } else if (mouseEvent.getSource() == btnConfirmEdit) {
            String newName = txtName.getText();
            String newPriceM = txtPriceM.getText();
            String newPriceL = txtPriceL.getText();
            String newPromo = txtPromo.getText();
            String newType = cbxCategory2.getSelectionModel().getSelectedItem();
            String oldName = selectedItem.getName();
            String oldPriceM = selectedItem.getPriceM().toString();
            String oldPriceL = selectedItem.getPriceL().toString();
            String oldPromo = selectedItem.getPromo().toString();
            String oldType = selectedItem.getType();
            String oldValues = oldName + oldPriceM + oldPriceL + oldPromo + oldType;
            String newValues = newName + newPriceM + newPriceL + newPromo + newType;
            System.out.println(oldValues);
            System.out.println(newValues);
            if (oldValues.equals(newValues)) {
                lblNoti.setText("Nothing changes!");
            } else {
                if (newPromo.isBlank()) {
                    newPromo = "0";
                }
                Item newItem = new Item(0, newName, newType, Integer.parseInt(newPriceM), Integer.parseInt(newPriceL)
                        , "in stock", Integer.parseInt(newPromo));
                System.out.println(newItem);
            }
            btnConfirmEdit.setVisible(false);
            btnConfirmEdit.setDisable(true);
            btnCancelEdit.setVisible(false);
            btnCancelEdit.setDisable(true);
            btnEdit.setVisible(true);
            btnEdit.setDisable(false);
            btnAdd.setDisable(false);
            btnInStock.setDisable(false);
            btnOutStock.setDisable(false);
        }
    }

    private Item selectedItem;
    public void handleClickTable(MouseEvent mouseEvent) {
        Item i = tableItem.getSelectionModel().getSelectedItem();
        selectedItem = i;
        btnEdit.setDisable(i == null);
        txtName.setText(i.getName());
        txtPriceM.setText(i.getPriceM().toString());
        txtPriceL.setText(i.getPriceL().toString());
        txtPromo.setText(i.getPromo().toString());
        cbxCategory2.getSelectionModel().select(i.getType());
    }

    public static List<Item> loadItemListFromDBToList(String itemType) {
        List<Item> itemList = new ArrayList<>();
        String query = "SELECT * FROM item WHERE item_type = '" + itemType + "' ORDER BY item_name ASC";
        ResultSet resultSet = MainController.connect.excuteQuerySelect(query);
        int countIndex = 0;
        try {
            while (resultSet.next()) {
                String name = resultSet.getString("item_name");
                Integer priceM = resultSet.getInt("price_sizem");
                Integer priceL = resultSet.getInt("price_sizel");
                String status = resultSet.getString("item_status");
                Integer promo = resultSet.getInt("discount_promo");
                Item item = new Item(++countIndex, name, itemType, priceM, priceL, status, promo);
                itemList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }

    public void loadItemListFromDBToTable(String itemType) {
        tableItem.setItems(null);
        List<Item> itemList = loadItemListFromDBToList(itemType);
        colNo.setCellValueFactory(new PropertyValueFactory<>("noInList"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPriceM.setCellValueFactory(new PropertyValueFactory<>("priceM"));
        colPriceL.setCellValueFactory(new PropertyValueFactory<>("priceL"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPromo.setCellValueFactory(new PropertyValueFactory<>("promo"));
        ObservableList<Item> items = FXCollections.observableArrayList(itemList);
        tableItem.setItems(items);
    }
}
