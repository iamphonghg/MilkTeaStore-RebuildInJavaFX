package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private VBox pnItemList;

    @FXML
    private VBox pnDetailItem;

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

    /*public static List<Item> itemList = loadListItemFromDBToList();

    public static List<Item> loadListItemFromDBToList() {
        List<Item> itemList = new ArrayList<>();
        String query = "SELECT * FROM item ORDER BY item_name ASC";
        ResultSet resultSet = MainController.connect.excuteQuerySelect(query);
        try {
            while (resultSet.next()) {
                Item item = new Item(1, resultSet.getString("item_name"), resultSet.getString("item_type").trim(),
                        resultSet.getInt("price_sizem"), resultSet.getInt("price_sizel"),
                        resultSet.getString("item_status"), resultSet.getInt("discount_promo"));
                itemList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxCategory.setItems(FXCollections.observableArrayList("MILK TEA", "FRUIT TEA", "MACCHIATO",
                "TOPPING", "FOOD"));
        cbxCategory.setFocusTraversable(false);
        loadItemListFromDBToTable("TRÀ SỮA");
        handleCombobox();
    }

    public void handleCombobox() {
        cbxCategory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selected = cbxCategory.getValue();
                switch (selected) {
                    case "MILK TEA":
                        loadItemListFromDBToTable("TRÀ SỮA");
                        break;
                    case "MACCHIATO":
                        loadItemListFromDBToTable("MACCHIATO");
                        break;
                    case "FRUIT TEA":
                        loadItemListFromDBToTable("TRÀ HOA QUẢ");
                        break;
                    case "TOPPING":
                        loadItemListFromDBToTable("TOPPING");
                        break;
                    case "FOOD":
                        loadItemListFromDBToTable("ĐỒ ĂN VẶT");
                        break;
                }
            }
        });
    }

    public void loadItemListFromDBToTable(String itemType) {
        tableItem.setItems(null);
        List<Item> itemList = new ArrayList<>();
        String query = "SELECT * FROM item WHERE item_type = '" + itemType + "' ORDER BY item_name ASC";
        ResultSet resultSet = MainController.connect.excuteQuerySelect(query);
        int countIndex = 0;
        try {
            while (resultSet.next()) {
                String name = resultSet.getString("item_name");
                Integer priceM = resultSet.getInt("price_sizem");
                Integer priceL = resultSet.getInt("price_sizel");
                String finalPriceL = null;
                if (priceL != 0) {
                    finalPriceL = String.valueOf(priceL);
                }
                String status = resultSet.getString("item_status");
                Integer promo = resultSet.getInt("discount_promo");
                String finalPromo = null;
                if (promo != 0) {
                    finalPromo = String.valueOf(promo);
                }
                Item item = new Item(++countIndex, name, itemType, priceM, priceL, status, promo);
                itemList.add(item);
                colNo.setCellValueFactory(new PropertyValueFactory<>("noInList"));
                colName.setCellValueFactory(new PropertyValueFactory<>("name"));
                colPriceM.setCellValueFactory(new PropertyValueFactory<>("priceM"));
                colPriceL.setCellValueFactory(new PropertyValueFactory<>("priceL"));
                colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
                colPromo.setCellValueFactory(new PropertyValueFactory<>("promo"));
                ObservableList<Item> items = FXCollections.observableArrayList(itemList);
                tableItem.setItems(items);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
