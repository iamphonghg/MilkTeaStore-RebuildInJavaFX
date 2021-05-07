package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import main.Main;
import model.Item;
import org.apache.commons.io.FileUtils;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    private Button btnAddImage;
    @FXML
    private Button btnRemoveImage;
    @FXML
    private Rectangle rectangle;
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

            btnAdd.setVisible(false);
            MainController.setDisable(true, tableItem, btnAdd, btnEdit, btnInStock, btnOutStock, btnAddImage,
                    btnRemoveImage);
            MainController.setDisable(false, btnConfirmAdd, btnCancelAdd);
            MainController.setVisible(true, btnConfirmAdd, btnCancelAdd);
        } else if (mouseEvent.getSource() == btnCancelAdd) {
            btnAdd.setVisible(true);
            MainController.setDisable(false, tableItem, btnAdd);
            MainController.setDisable(true, btnConfirmAdd, btnCancelAdd);
            MainController.setVisible(false, btnConfirmAdd, btnCancelAdd);
        } else if (mouseEvent.getSource() == btnConfirmAdd) {
            btnAdd.setVisible(true);
            MainController.setDisable(false, tableItem, btnAdd);
            MainController.setDisable(true, btnConfirmAdd, btnCancelAdd);
            MainController.setVisible(false, btnConfirmAdd, btnCancelAdd);

            String name = txtName.getText();
            String priceM = txtPriceM.getText();
            String priceL = txtPriceL.getText();
            String promo = txtPromo.getText();
            String type = cbxCategory2.getSelectionModel().getSelectedItem();
            if (name.isBlank() || priceM.isBlank() || (type.equals("TOPPING") && !priceL.isBlank())) {
                lblNoti.setText("Can not add, please check again!");
            } else {
                Integer promoInt = null;
                Integer priceMInt = Integer.parseInt(priceM);
                Integer priceLInt = null;
                if (!priceL.isBlank() && !priceL.equals("0")) {
                    priceLInt = Integer.parseInt(priceL);
                }
                if (!promo.isBlank() && !promo.equals("0")) {
                    promoInt = Integer.parseInt(promo);
                }
                insertItemIntoDB(name, type, priceMInt, priceLInt, promoInt);
            }
        } else if (mouseEvent.getSource() == btnEdit) {
            btnEdit.setVisible(false);
            MainController.setDisable(true, btnAdd, tableItem, btnInStock, btnOutStock, btnAddImage, btnRemoveImage, btnEdit);
            MainController.setVisible(true, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(false, btnConfirmEdit, btnCancelEdit);
        } else if (mouseEvent.getSource() == btnCancelEdit) {
            btnEdit.setVisible(true);
            MainController.setVisible(false, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(true, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(false, tableItem, btnAdd);
        } else if (mouseEvent.getSource() == btnConfirmEdit) {
            btnEdit.setVisible(true);
            MainController.setVisible(false, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(true, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(false, tableItem, btnAdd);

            String oldName = selectedItem.getName();
            String oldPriceM = selectedItem.getPriceM().toString();
            String oldPriceL = selectedItem.getPriceL().toString();
            String oldPromo = selectedItem.getPromo().toString();
            String oldType = selectedItem.getType();
            String oldValues = oldName + oldPriceM + oldPriceL + oldPromo + oldType;
            String newName = txtName.getText();
            String newPriceM = txtPriceM.getText();
            String newPriceL = txtPriceL.getText();
            String newPromo = txtPromo.getText();
            String newType = cbxCategory2.getSelectionModel().getSelectedItem();
            String newValues = newName + newPriceM + newPriceL + newPromo + newType;
            if (newName.isBlank() || newPriceM.isBlank() || (newType.equals("TOPPING") && !newPriceL.isBlank())) {
                lblNoti.setText("Can not update, please check again!");
            } else {
                if (oldValues.equals(newValues)) {
                    lblNoti.setText("Nothing changes!");
                } else {
                    Integer newPromoInt = null;
                    Integer newPriceMInt = Integer.parseInt(newPriceM);
                    Integer newPriceLInt = null;
                    if (!newPriceL.isBlank() && !newPriceL.equals("0")) {
                        newPriceLInt = Integer.parseInt(newPriceL);
                    }
                    if (!newPromo.isBlank() && !newPromo.equals("0")) {
                        newPromoInt = Integer.parseInt(newPromo);
                    }
                    updateItemIntoDB(oldName, newName, newType, newPriceMInt, newPriceLInt, newPromoInt);
                }
            }
        } else if (mouseEvent.getSource() == btnInStock) {
            updateStockStatus(selectedItem.getName(), selectedItem.getType(), "in stock");
        } else if (mouseEvent.getSource() == btnOutStock) {
            updateStockStatus(selectedItem.getName(), selectedItem.getType(), "out stock");
        }
    }

    private Item selectedItem;
    public static String pathToImageDirectory = "file:D:\\JavaProject\\MilkTeaStore-RebuildInJavaFX\\src\\image\\item\\";
    public void handleClickTable(MouseEvent mouseEvent) {
        Item i = tableItem.getSelectionModel().getSelectedItem();
        if (selectedItem != i) {
            rectangle.setFill(null);
            if (i != null) {
                MainController.setDisable(false, btnEdit, btnInStock, btnOutStock, btnAddImage, btnRemoveImage);
                selectedItem = i;
                if (!i.getImageName().equals("not valid")) {
                    String imageDirectory = "";
                    switch (selectedItem.getType()) {
                        case "MILK TEA": {
                            imageDirectory = "milk-tea\\";
                            break;
                        }
                        case "FRUIT TEA": {
                            imageDirectory = "fruit-tea\\";
                            break;
                        }
                        case "MACCHIATO": {
                            imageDirectory = "macchiato\\";
                            break;
                        }
                        case "TOPPING": {
                            imageDirectory = "topping\\";
                            break;
                        }
                        case "FOOD": {
                            imageDirectory = "food\\";
                            break;
                        }
                    }
                    rectangle.setFill(new ImagePattern(new Image(pathToImageDirectory + imageDirectory + i.getImageName())));
                }
                txtName.setText(i.getName());
                txtPriceM.setText(i.getPriceM().toString());
                txtPriceL.setText(i.getPriceL().toString());
                txtPromo.setText(i.getPromo().toString());
                cbxCategory2.getSelectionModel().select(i.getType());
            }
        }
    }

    public void handleAddAndRemoveImage(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == btnAddImage) {
            FileChooser fileChooser = new FileChooser();
            File selectFile = fileChooser.showOpenDialog(null);
            String[] splitImageName = selectFile.getAbsolutePath().split(Pattern.quote("\\"));
            String imageName = splitImageName[splitImageName.length - 1];
            selectedItem.setImageName(imageName);
            updateImageNameIntoDB(imageName, selectedItem.getName());
            rectangle.setFill(new ImagePattern(new Image("file:" + selectFile)));
            File destination;
            switch (selectedItem.getType()) {
                case "MILK TEA": {
                    destination = new File("src/image/item/milk-tea");
                    break;
                }
                case "FRUIT TEA": {
                    destination = new File("src/image/item/fruit-tea");
                    break;
                }
                case "MACCHIATO": {
                    destination = new File("src/image/item/macchiato");
                    break;
                }
                case "TOPPING": {
                    destination = new File("src/image/item/topping");
                    break;
                }
                case "FOOD": {
                    destination = new File("src/image/item/food");
                    break;
                }
                default:
                    destination = new File("src/image/item");
            }
            FileUtils.copyFileToDirectory(selectFile, destination);
        } else if (mouseEvent.getSource() == btnRemoveImage) {
            updateImageNameIntoDB("null", selectedItem.getName());
            rectangle.setFill(null);
            String imageDirectory = "";
            switch (selectedItem.getType()) {
                case "MILK TEA": {
                   imageDirectory = "milk-tea\\";
                    break;
                }
                case "FRUIT TEA": {
                    imageDirectory = "fruit-tea\\";
                    break;
                }
                case "MACCHIATO": {
                    imageDirectory = "macchiato\\";
                    break;
                }
                case "TOPPING": {
                    imageDirectory = "topping\\";
                    break;
                }
                case "FOOD": {
                    imageDirectory = "food\\";
                    break;
                }
            }
            FileUtils.forceDelete(new File("D:\\JavaProject\\MilkTeaStore-RebuildInJavaFX\\src\\image\\item\\" + imageDirectory + selectedItem.getImageName()));
            selectedItem.setImageName("not valid");
        }
    }

    public void updateImageNameIntoDB(String imageName, String itemName) {
        if (!imageName.equals("null")) {
            imageName = "'" + imageName + "'";
        }
        String query = "UPDATE item SET image_name = " + imageName + " WHERE item_name = '" + itemName + "'";
        boolean res = MainController.connect.excuteQueryUpdate(query);
        if (res) {
            System.out.println("Successfully updated.");
        }
    }

    public void updateStockStatus(String name, String type, String status) {
        String query = "UPDATE item " +
                "SET item_status = '" + status + "' " +
                "WHERE item_name = '" + name + "'";
        boolean res = MainController.connect.excuteQueryUpdate(query);
        loadItemListFromDBToTable(type);
    }

    public void updateItemIntoDB(String oldName, String newName, String newType, Integer newPriceM, Integer newPriceL, Integer newPromo) {
        String query = "UPDATE item " +
                "SET item_name = '" + newName + "', " +
                "item_type = '" + newType + "', " +
                "price_sizem = " + newPriceM + ", " +
                "price_sizel = " + newPriceL + ", " +
                "discount_promo = " + newPromo + " " +
                "WHERE item_name = '" + oldName + "'";
        boolean res = MainController.connect.excuteQueryUpdate(query);
        if (res) {
            loadItemListFromDBToTable(newType);
            lblNoti.setText("Successfully updated.");
        } else {
            lblNoti.setText("Update failed!");
        }
    }

    public void insertItemIntoDB(String name, String type, Integer priceM, Integer priceL, Integer promo) {
        String query = "INSERT INTO item VALUES ('" + name + "', '" + type + "', " + priceM + ", " + priceL
                + ", 'in stock', " + promo + ", null)";
        boolean res = MainController.connect.excuteQueryUpdate(query);
        if (res) {
            loadItemListFromDBToTable(type);
            lblNoti.setText("Successfully added.");
        } else {
            lblNoti.setText("Add failed!");
        }
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
                String imageName = resultSet.getString("image_name");
                Item item = new Item(++countIndex, name, itemType, priceM, priceL, status, promo);
                if (imageName != null) {
                    item.setImageName(imageName);
                }
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
