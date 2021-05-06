package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
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
        addItemToCbxBirthday();
        loadStaffListFromDBToTable();
    }



    public void eventButton(MouseEvent mouseEvent) {

    }
    public void handleAddAndRemoveImage(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() == btnAddImage) {
            FileChooser fileChooser = new FileChooser();
            File selectFile = fileChooser.showOpenDialog(null);
            String[] splitImageName = selectFile.getAbsolutePath().split(Pattern.quote("\\"));
            String imageName = splitImageName[splitImageName.length - 1];
            selectedStaff.setImageName(imageName);
            File destination = new File("src/image/staff");
            FileUtils.copyFileToDirectory(selectFile, destination);
            circle.setFill(new ImagePattern(new Image(pathToImageDirectory + imageName)));
            updateImageNameIntoDB(imageName, selectedStaff.getId());
        }
        if (mouseEvent.getSource() == btnRemoveImage) {
            updateImageNameIntoDB("null", selectedStaff.getId());
            circle.setFill(null);
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
            selectedStaff = s;
            btnEdit.setDisable(s == null);
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
