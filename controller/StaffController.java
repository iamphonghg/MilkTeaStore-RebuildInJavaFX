package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import model.Staff;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class StaffController implements Initializable {
    @FXML
    private ComboBox<String> cbxYear;

    @FXML
    private ComboBox<String> cbxMonth;

    @FXML
    private ComboBox<String> cbxDay;

    @FXML
    private ComboBox<String> cbxPosition;

    @FXML
    private TableView<Staff> tableStaff;

    @FXML
    private TableColumn<Staff, String> colID;

    @FXML
    private TableColumn<Staff, String> colName;

    @FXML
    private TableColumn<Staff, String> colGender;

    @FXML
    private TableColumn<Staff, String> colBirthday;

    @FXML
    private TableColumn<Staff, String> colPosition;

    @FXML
    private TableColumn<Staff, String> colStatus;

    @FXML
    private TextField txtID;

    @FXML
    private ComboBox<String> cbxGender;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnRetire;

    @FXML
    private Button btnConfirmAdd;

    @FXML
    private Button btnCancelAdd;

    @FXML
    private Button btnCancelEdit;

    @FXML
    private Button btnConfirmEdit;

    @FXML
    private Label lblNoti;

    @FXML
    private Button btnAddImage;

    @FXML
    private Button btnRemoveImage;

    @FXML
    private ImageView staffImage;

    @FXML
    private Circle circle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");
        cbxGender.setItems(FXCollections.observableArrayList(genderList));

        List<String> positionList = new ArrayList<>();
        positionList.add("Waiter");
        positionList.add("Manager");
        cbxPosition.setItems(FXCollections.observableArrayList(positionList));

        addItemToCbxBirthday();
        loadStaffListFromDBToTable();
    }

    public List<Staff> currentStaffList;

    public boolean checkDuplicateID(String id) {
        for (Staff staff : currentStaffList) {
            if (staff.getId().equals(id)) {
                return false;
            }
        }
        return true;   //true la khong trung
    }

    public String randomGenID() {
        Random random = new Random();
        String newId = null;
        do {
            newId = "NV" + (random.nextInt(9999 - 1000 + 1) + 1000);
        } while (!checkDuplicateID(newId));
        return newId;
    }

    public void eventButton(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == btnAdd) {
            txtID.setText(randomGenID());
            txtName.setText("");
            cbxPosition.getSelectionModel().select(0);
            txtPhone.setText("");
            cbxGender.getSelectionModel().select(0);
            cbxYear.getSelectionModel().select("2000");
            cbxMonth.getSelectionModel().select("1");
            cbxDay.getSelectionModel().select("1");

            btnAdd.setVisible(false);
            MainController.setVisible(true, btnConfirmAdd, btnCancelAdd);
            MainController.setDisable(false, btnConfirmAdd, btnCancelAdd);
            MainController.setDisable(true, btnAdd, btnEdit, btnRetire, tableStaff, btnAddImage, btnRemoveImage);
        } else if (mouseEvent.getSource() == btnCancelAdd) {
            btnAdd.setVisible(true);
            MainController.setVisible(false, btnConfirmAdd, btnCancelAdd);
            MainController.setDisable(false, btnAdd, tableStaff);
            MainController.setDisable(true, btnConfirmAdd, btnCancelAdd);
        } else if (mouseEvent.getSource() == btnConfirmAdd) {
            btnAdd.setVisible(true);
            MainController.setDisable(false, btnAdd, tableStaff);
            MainController.setVisible(false, btnConfirmAdd, btnCancelAdd);
            MainController.setDisable(true, btnConfirmAdd, btnCancelAdd);

            String id = txtID.getText();
            String name = txtName.getText();
            String position = cbxPosition.getSelectionModel().getSelectedItem();
            String phone = txtPhone.getText();
            Character gender = cbxGender.getSelectionModel().getSelectedItem().charAt(0);
            String year = cbxYear.getSelectionModel().getSelectedItem();
            String month = cbxMonth.getSelectionModel().getSelectedItem();
            String day = cbxDay.getSelectionModel().getSelectedItem();
            String birthday = year + "-" + month + "-" + day;
            if (name.isBlank()) {
                lblNoti.setText("Do not leave the name blank!");
            } else {
                insertStaffIntoDB(id, name, gender, birthday, position, phone);
            }
        } else if (mouseEvent.getSource() == btnEdit) {
            btnEdit.setVisible(false);
            MainController.setDisable(true, btnAdd, tableStaff, btnRetire, btnAddImage, btnRemoveImage, btnEdit);
            MainController.setVisible(true, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(false, btnConfirmEdit, btnCancelEdit);
        } else if (mouseEvent.getSource() == btnCancelEdit) {
            btnEdit.setVisible(true);
            MainController.setVisible(false, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(true, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(false, tableStaff, btnAdd);
        } else if (mouseEvent.getSource() == btnConfirmEdit) {
            btnEdit.setVisible(true);
            MainController.setVisible(false, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(true, btnConfirmEdit, btnCancelEdit);
            MainController.setDisable(false, tableStaff, btnAdd);

            String id = txtID.getText();
            String newName = txtName.getText();
            char newGender = cbxGender.getSelectionModel().getSelectedItem().charAt(0);
            String newPosition = cbxPosition.getSelectionModel().getSelectedItem();
            String newPhone = txtPhone.getText();
            String year = cbxYear.getSelectionModel().getSelectedItem();
            String month = cbxMonth.getSelectionModel().getSelectedItem();
            String day = cbxDay.getSelectionModel().getSelectedItem();
            String newBirthday = year + "-" + month + "-" + day;
            String newValues = newName + newGender + newPosition + newPhone + newBirthday;

            String oldName = selectedStaff.getName();
            String oldGender = selectedStaff.getGender() + "";
            String oldPosition = selectedStaff.getPosition();
            String oldPhone = selectedStaff.getPhoneNumber();
            String oldBirthday = selectedStaff.getBirthday();
            String oldValues = oldName + oldGender + oldPosition + oldPhone + oldBirthday;

            System.out.println(newValues);
            System.out.println(oldValues);
            if (newValues.equals(oldValues)) {
                lblNoti.setText("Nothing changes!");
            } else {
                updateStaffIntoDB(id, newName, newGender + "", newBirthday, newPosition, newPhone);
            }
        } else if (mouseEvent.getSource() == btnRetire) {
            updateStaffStatusIntoDB(selectedStaff.getId());
        }
    }

    public void updateStaffIntoDB(String id, String newName, String newGender, String newBirthday, String newPosition, String newPhoneNumber) {
        if (newPhoneNumber != null) {
            newPhoneNumber = "'" + newPhoneNumber + "'";
        }
        String query = "UPDATE staff " +
                "SET staff_name = '" + newName + "', " +
                "gender = '" + newGender + "', " +
                "birthday = '" + newBirthday + "', " +
                "position = '" + newPosition + "', " +
                "phone_number = " + newPhoneNumber + " " +
                "WHERE staff_id = '" + id + "'";
        boolean res = MainController.connect.excuteQueryUpdate(query);
        if (res) {
            loadStaffListFromDBToTable();
            lblNoti.setText("Successfully updated.");
        } else {
            lblNoti.setText("Update failed!");
        }
    }

    public void insertStaffIntoDB(String id, String name, Character gender, String birthday, String position,
                                  String phoneNumber) {
        if (phoneNumber.isBlank()) {
            phoneNumber = null;
        } else {
            phoneNumber = "'" + phoneNumber + "'";
        }
        String query = "INSERT INTO staff VALUES ('" + id + "', '" + name + "', '" + gender + "', '" + birthday
                + "', '" + position + "', " + phoneNumber + ", 'working', null)";
        boolean res = MainController.connect.excuteQueryUpdate(query);
        if (res) {
            loadStaffListFromDBToTable();
            lblNoti.setText("Successfully inserted.");
        } else {
            lblNoti.setText("Insert failed!");
        }
    }

    public void updateStaffStatusIntoDB(String id) {
        String query = "UPDATE staff SET staff_status = 'retired' " +
                "WHERE staff_id = '" + id + "'";
        boolean res = MainController.connect.excuteQueryUpdate(query);
        if (res) {
            loadStaffListFromDBToTable();
            lblNoti.setText("Successfully updated.");
        } else {
            lblNoti.setText("Update failed!");
        }
    }

    public void handleAddAndRemoveImage(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == btnAddImage) {
            FileChooser fileChooser = new FileChooser();
            File selectFile = fileChooser.showOpenDialog(null);
            String[] splitImageName = selectFile.getAbsolutePath().split(Pattern.quote("\\"));
            String imageName = splitImageName[splitImageName.length - 1];
            selectedStaff.setImageName(imageName);
            updateImageNameIntoDB(imageName, selectedStaff.getId());
            circle.setFill(new ImagePattern(new Image("file:" + selectFile)));
            File destination = new File("src/image/staff");
            FileUtils.copyFileToDirectory(selectFile, destination);
        } else if (mouseEvent.getSource() == btnRemoveImage) {
            updateImageNameIntoDB("null", selectedStaff.getId());
            circle.setFill(null);
            FileUtils.forceDelete(new File("D:\\JavaProject\\MilkTeaStore-RebuildInJavaFX\\src\\image\\staff\\" + selectedStaff.getImageName()));
            selectedStaff.setImageName("not valid");
        }
    }

    public void updateImageNameIntoDB(String imageName, String id) {
        if (!imageName.equals("null")) {
            imageName = "'" + imageName + "'";
        }
        String query = "UPDATE staff SET image_name = " + imageName + " WHERE staff_id = '" + id + "'";
        MainController.connect.excuteQueryUpdate(query);
        System.out.println("Successfully updated.");
    }

    public void addItemToCbxBirthday() {
        List<String> yearList = new ArrayList<>();
        for (int i = 1980; i <= 2006; i++) {
            yearList.add(i + "");
        }
        cbxYear.setItems(FXCollections.observableArrayList(yearList));

        List<String> monthList = new ArrayList<>();
        for (int i = 1; i <= 12 ; i++) {
            monthList.add(i + "");
        }
        cbxMonth.setItems(FXCollections.observableArrayList(monthList));

        List<String> dayList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            dayList.add(i + "");
        }
        cbxDay.setItems(FXCollections.observableArrayList(dayList));
    }

    private boolean checkLeapYear(int year) {
        return (((year % 4 == 0) && (year % 100 != 0)) ||
                (year % 400 == 0));
    }

    private void addItemCbxDay(int maxDay) {
    }

    public Staff selectedStaff;
    public Integer selectedYear, selectedMonth, selectedDay;
    public static String pathToImageDirectory = "file:D:\\JavaProject\\MilkTeaStore-RebuildInJavaFX\\src\\image\\staff\\";
    public void handleClickTable(MouseEvent mouseEvent) {
        Staff s = tableStaff.getSelectionModel().getSelectedItem();
        if (selectedStaff != s) {
            circle.setFill(null);
            if (s != null) {
                MainController.setDisable(false, btnEdit, btnAddImage, btnRemoveImage, btnRetire);
                selectedStaff = s;
                if (!s.getImageName().equals("not valid")) {
                    circle.setFill(new ImagePattern(new Image(pathToImageDirectory + s.getImageName())));
                }
                txtID.setText(s.getId());
                txtName.setText(s.getName());
                cbxPosition.getSelectionModel().select(s.getPosition());
                txtPhone.setText(s.getPhoneNumber());
                if (s.getGender().equals('M')) {
                    cbxGender.getSelectionModel().select("Male");
                } else if (s.getGender() == 'F') {
                    cbxGender.getSelectionModel().select("Female");
                } else {
                    cbxGender.getSelectionModel().select("Other");
                }
                String[] selectedBirthday = selectedStaff.getBirthday().split("-");
                selectedYear = Integer.parseInt(selectedBirthday[0]);
                selectedMonth = Integer.parseInt(selectedBirthday[1]);
                selectedDay = Integer.parseInt(selectedBirthday[2]);
                cbxYear.getSelectionModel().select(selectedYear + "");
                cbxMonth.getSelectionModel().select(selectedMonth + "");
                cbxDay.getSelectionModel().select(selectedDay + "");
            }
        }
    }


    public List<Staff> loadStaffListFromDBToList() {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM staff ORDER BY staff_name ASC";
        ResultSet resultSet = MainController.connect.excuteQuerySelect(query);
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("staff_id");
                String name = resultSet.getString("staff_name");
                Character gender = resultSet.getString("gender").charAt(0);
                String birthday = resultSet.getString("birthday");
                String position = resultSet.getString("position");
                String phoneNumber = resultSet.getString("phone_number");
                String status = resultSet.getString("staff_status");
                String imageName = resultSet.getString("image_name");
                Staff staff = new Staff(id, name, gender, birthday, position, phoneNumber, status);
                if (imageName != null) {
                    staff.setImageName(imageName);
                }
                staffList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        currentStaffList = staffList;
        return staffList;
    }

    public void loadStaffListFromDBToTable() {
        List<Staff> staffList = loadStaffListFromDBToList();
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colBirthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList<Staff> staffs = FXCollections.observableArrayList(staffList);
        tableStaff.setItems(staffs);
    }


}
