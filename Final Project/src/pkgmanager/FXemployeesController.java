/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import pkgcommon.Functions;
import pkgmodels.employee;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class FXemployeesController implements Initializable {

    @FXML
    private ChoiceBox chJobCat;
    @FXML
    private Spinner<?> spWage;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnExport;
    private Button btnView;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSuspend;
    @FXML
    private Button btnFire;
    private ObservableList<employee> data;
    private Functions functions;
    private Connection conn;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;
    @FXML
    private MenuItem clerkMI;
    @FXML
    private MenuItem quitMI;
    @FXML
    private MenuItem newClerkMI;
    @FXML
    private MenuItem changePassMI;
    @FXML
    private MenuItem farmersMI;
    @FXML
    private MenuItem employeesMI;
    @FXML
    private MenuItem attendanceMI;
    @FXML
    private MenuItem exportationMI;
    @FXML
    private MenuItem chemicalMI;
    @FXML
    private TextField txtEmpID;
    @FXML
    private TextField txtNatID;
    @FXML
    private TextField txtSurName;
    @FXML
    private TextField txtMidName;
    @FXML
    private TextField txtLastName;
    @FXML
    private DatePicker txtDateOfBirth;
    @FXML
    private RadioButton rdActive;
    @FXML
    private ToggleGroup status;
    @FXML
    private TextField txtMobile;
    private final String pattern = "yyyy-MM-dd";
    @FXML
    private TableColumn<employee, Integer> colEmpID;
    @FXML
    private TableColumn<employee, Integer> colNatID;
    @FXML
    private TableColumn<employee, String> colSurName;
    @FXML
    private TableColumn<employee, String> colMidName;
    @FXML
    private TableColumn<employee, String> colLastName;
    @FXML
    private TableColumn<employee, Integer> colPhoneNo;
    @FXML
    private TableColumn<employee, String> colcategory;
    @FXML
    private TableColumn<employee, Double> colWage;
    @FXML
    private TableColumn<employee, String> colStatus;
    @FXML
    private TableView<employee> empTable;

    static int editID;
    @FXML
    private Button btnActivate;
    @FXML
    private CheckBox chkPrint;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnClear;
    @FXML
    private MenuItem logoutMI;
    @FXML
    private MenuItem employeePaymentMI;
    @FXML
    private MenuItem farmerpaymentsMI;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Spinners
        SpinnerValueFactory svf = new SpinnerValueFactory.DoubleSpinnerValueFactory(1000, 15000, 6000, 100);
        spWage.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        spWage.setValueFactory(svf);

        //Buttons
        btnSave.disableProperty().bind(txtEmpID.textProperty().isEmpty());
        btnDelete.setStyle("-fx-base: red;");
        btnFire.setStyle("-fx-base: red;");
        btnSuspend.setStyle("-fx-base: red;");
        btnActivate.setStyle("-fx-base:blue;");

        //Radio Buttons
        rdActive.setSelected(true);

        //Comboboxes
        chJobCat.getItems().addAll("Driers", "Pumpers", "Sculptures", "Watchman");
        chJobCat.getSelectionModel().selectFirst();

        //Date Formatter
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);

                } else {
                    return null;
                }
            }

        };
        txtDateOfBirth.setConverter(converter);

        // class references
        functions = new Functions();

        //Methods
        EmployeeData();
        empTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends employee> observable,
                employee oldValue,
                employee newValue) -> {
            if (newValue == null) {
                editID = 0;
                txtSearch.setText(null);
                txtLastName.setText(null);
                txtMidName.setText(null);
                txtSurName.setText(null);
                txtMobile.setText(null);
                txtNatID.setText(null);
                chJobCat.getSelectionModel().selectFirst();
                return;
            }
            txtSearch.setText(String.valueOf(newValue.getEmployeeid()));
            editID = newValue.getEmployeeid();
            txtLastName.setText(newValue.getLastname());
            txtMidName.setText(newValue.getMidname());
            txtSurName.setText(newValue.getSurname());
            txtMobile.setText(String.valueOf(newValue.getMobileno()));
            txtNatID.setText(String.valueOf(newValue.getNationalid()));
            chJobCat.getSelectionModel().selectFirst();

        });

    }

    @FXML
    private void refreshAction(ActionEvent event) {
        //empTable.getItems().clear();
        EmployeeData();
    }

    @FXML
    private void printAction(ActionEvent event) throws JRException {

        String report = "src\\pkgreports\\employeeReport.jrxml";
        conn = functions.connectDB();

        JasperReport jr = JasperCompileManager.compileReport(report);
        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
        JasperViewer.viewReport(jp, false);
    }

    @FXML
    private void exportAction(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel Files(*.xls)", "*.xls");
        chooser.getExtensionFilters().add(filter);
        // Show save dialog
        File file = chooser.showSaveDialog(btnExport.getScene().getWindow());
        if (file != null) {
            saveXLSFile(file);

        }
    }

    @FXML
    private void newAction(ActionEvent event) throws SQLException {
        txtLastName.setText(null);
        txtMidName.setText(null);
        txtNatID.setText(null);
        txtSurName.setText(null);
        txtSearch.setText(null);
        txtEmpID.setText(String.valueOf(generateEmployeeID()));
    }

    @FXML
    private void editAction(ActionEvent event) {
        try {
            String edit = "UPDATE mg_employees SET national_id=?,surname=?,middlename=?,lastname=?,mobile_no=?,job_category=?,wage=? "
                    + " WHERE employee_id=?";
            conn = functions.connectDB();
            ps = conn.prepareStatement(edit);
            ps.setString(1, txtNatID.getText());
            ps.setString(2, txtSurName.getText());
            ps.setString(3, txtMidName.getText());
            ps.setString(4, txtLastName.getText());
            ps.setString(5, txtMobile.getText());
            ps.setString(6, chJobCat.getValue().toString());
            ps.setString(7, spWage.getValue().toString());
            ps.setInt(8, editID);
            if (functions.isValidInteger(txtNatID, 12)) {
                if (functions.isValidInteger(txtMobile, 10)) {
                    int success = ps.executeUpdate();
                    if (success == 1) {
                        TrayNotification tn = new TrayNotification("UPDATE", "Updates  successful", NotificationType.SUCCESS);
                        tn.setAnimationType(AnimationType.POPUP);
                        tn.showAndDismiss(Duration.seconds(2));
                        EmployeeData();
                    }

                } else {
                    txtMobile.requestFocus();
                }
            } else {
                txtNatID.requestFocus();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
            TrayNotification tn = new TrayNotification("UPDATE", "Updates Failed", NotificationType.SUCCESS);
            tn.setAnimationType(AnimationType.POPUP);
            tn.showAndDismiss(Duration.seconds(2));

        }
        reset();

    }

    @FXML
    private void saveAction(ActionEvent event) {
        String insert = "INSERT INTO mg_employees("
                + "employee_id,national_id,surname,middlename,lastname,mobile_no,"
                + "date_registered,date_of_birth,job_category,wage,emp_status)"
                + "VALUES(null,?,?,?,?,?,current_date,?,?,?,?);";

        try {
            conn = functions.connectDB();
            PreparedStatement pst = conn.prepareStatement(insert);

            pst.setString(1, txtNatID.getText());
            pst.setString(2, txtSurName.getText());
            pst.setString(3, txtMidName.getText());
            pst.setString(4, txtLastName.getText());
            pst.setString(5, txtMobile.getText());
            pst.setDate(6, java.sql.Date.valueOf(txtDateOfBirth.getValue()));
            pst.setString(7, chJobCat.getValue().toString());
            pst.setString(8, spWage.getValue().toString());
            pst.setString(9, _empStatus());

            if (functions.isValidInteger(txtNatID, 12)) {
                if (functions.isMobileField(txtEmpID)) {
                    int success = pst.executeUpdate();
                    if (success == 1) {
                        if (chkPrint.isSelected()) {
                            generateReceipt();
                        }
                        TrayNotification tn = new TrayNotification("New Employee", "New employee registered successfully", NotificationType.SUCCESS);
                        tn.setAnimationType(AnimationType.POPUP);
                        tn.showAndDismiss(Duration.seconds(2));
                        reset();
                        EmployeeData();
                    }
                }
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Check input fields for Either \n NATIONAL ID FIELD \n and/or \n MOBILE PHONE FIELD", ButtonType.OK);
                a.show();
                txtNatID.requestFocus();
                txtMobile.requestFocus();
            }

        } catch (SQLException | JRException e) {
            System.err.println(e.getMessage());
            Alert a = new Alert(Alert.AlertType.ERROR, "Registration Failed,please input all required data before retrying", ButtonType.OK);
            a.show();

        }

    }

    @FXML
    private void deleteAction(ActionEvent event) {

        try {
            conn = functions.connectDB();
            String del = "DELETE FROM mg_employees  WHERE employee_id=?";
            PreparedStatement ps = conn.prepareStatement(del);
            ps.setInt(1, editID);

            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirm Deletion of specified", ButtonType.OK);
            a.setTitle("Confirm Delete");
            a.showAndWait();
            if (a.getResult() == ButtonType.OK) {
                int res = ps.executeUpdate();
                if (res == 1) {
                    TrayNotification tn = new TrayNotification("DELETION STATUS", "Employee has been deleted from system", NotificationType.SUCCESS);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndWait();
                    EmployeeData();
                } else {
                    TrayNotification tn = new TrayNotification("DELETION STATUS", "Could not perform the action", NotificationType.ERROR);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndWait();

                }
                txtSearch.setText(null);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void suspendAction(ActionEvent event) {
        try {
            conn = functions.connectDB();
            String sus = "UPDATE mg_employees SET emp_status='suspended' WHERE employee_id='" + editID + "'";
            if (editID >= 1) {
                int success = conn.createStatement().executeUpdate(sus);
                if (success == 1) {
                    TrayNotification tn = new TrayNotification("EMPLOYEE STATUS", "SUSPENDED SUCCESSFULLY", NotificationType.INFORMATION);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndDismiss(Duration.seconds(2));
                    EmployeeData();
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void fireAction(ActionEvent event) {
        try {
            conn = functions.connectDB();
            String sus = "UPDATE mg_employees SET emp_status='fired' WHERE employee_id='" + editID + "'";
            if (editID >= 1) {
                int success = conn.createStatement().executeUpdate(sus);
                if (success == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "EMPLOYEE FIRING CAPTURED", ButtonType.OK);
                    alert.show();
                    EmployeeData();
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void EmployeeData() {
        try {
            conn = functions.connectDB();
            data = FXCollections.observableArrayList();
            String query = "SELECT * FROM  mg_employees";

            rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                int farmerid = rs.getInt(1);
                int natid = rs.getInt(2);
                String sname = rs.getString(3).toUpperCase();
                String midname = rs.getString(4).toUpperCase();
                String lastname = rs.getString(5).toUpperCase();
                int phoneno = rs.getInt(6);
                String jobCat = rs.getString(9).toUpperCase();
                Double wage = rs.getDouble(10);
                String stats = rs.getString(11).toUpperCase();

                data.add(new employee(farmerid, natid, sname, midname, lastname, phoneno, jobCat, wage, stats));

            }

        } catch (Exception e) {
        }
        colEmpID.setCellValueFactory(new PropertyValueFactory<>("employeeid"));
        colNatID.setCellValueFactory(new PropertyValueFactory<>("nationalid"));
        colSurName.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colMidName.setCellValueFactory(new PropertyValueFactory<>("midname"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("mobileno"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("jobCategory"));
        colWage.setCellValueFactory(new PropertyValueFactory<>("wage"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("empstatus"));
        empTable.setItems(null);
        empTable.setItems(data);

    }

    @FXML
    private void clerkView(ActionEvent event) {
    }

    @FXML
    private void quitOption(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void ActionNewClerk(ActionEvent event) {

    }

    @FXML
    private void ActionChangePass(ActionEvent event) {
    }

    @FXML
    private void FarmersView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnDelete.getScene().getWindow();
            functions.NewStage(Functions.FARMERS, "FARMERS MANAGEMENT", true);
            currentStage.hide();
        } catch (Exception ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void EmployeeView(ActionEvent event) {
    }

    @FXML
    private void AttendanceView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnDelete.getScene().getWindow();
            functions.NewStage(Functions.ATTENDANCE, "ATTENDANCE MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ExportationView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnDelete.getScene().getWindow();
            functions.NewStage(Functions.EXPORT, "EXPORTATION MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ChemicalsView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnDelete.getScene().getWindow();
            functions.NewStage(Functions.CHEMICALS, "CHEMICALS MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void PaymentsView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnDelete.getScene().getWindow();
            functions.NewStage(Functions.FARMERS, "FARMERS MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Integer generateEmployeeID() throws SQLException {
        conn = functions.connectDB();
        String query = "SELECT employee_id  FROM mg_employees ORDER BY employee_id asc";
        rs = conn.createStatement().executeQuery(query);
        rs.last();
        int EMP_ID = rs.getInt(1);
        return EMP_ID + 1;
    }

    private String _empStatus() {
        return "ACTIVE";
    }

    void reset() {
        txtEmpID.setText(null);
        txtLastName.setText(null);
        txtMidName.setText(null);
        txtMobile.setText(null);
        txtNatID.setText(null);
        txtSearch.setText(null);
        txtSurName.setText(null);
        txtDateOfBirth.setValue(LocalDate.now());
    }

    private void saveXLSFile(File file) {
        try {
            //System.out.println("Clicked,waiting to export....");
            conn = functions.connectDB();
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet workSheet = workbook.createSheet("sheet 0");
            Row row1 = workSheet.createRow((short) 0);
            row1.createCell(0).setCellValue("EMPLOYEE ID");
            row1.createCell(1).setCellValue("NATIONAL ID");
            row1.createCell(2).setCellValue("SUR NAME");
            row1.createCell(3).setCellValue("MID NAME");
            row1.createCell(4).setCellValue("LAST NAME");
            row1.createCell(5).setCellValue("MOBILE NO");
            row1.createCell(6).setCellValue("REG DATE");
            row1.createCell(7).setCellValue("BIRTH DATE");
            row1.createCell(8).setCellValue("CATEGORY");
            row1.createCell(9).setCellValue("WAGE");
            row1.createCell(10).setCellValue("STATUS");
            Row row2;

            rs = conn.createStatement().executeQuery("select * from mg_employees");
            while (rs.next()) {
                int a = rs.getRow();
                row2 = workSheet.createRow((short) a);
                row2.createCell(0).setCellValue(rs.getInt(1));
                row2.createCell(1).setCellValue(rs.getInt(2));
                row2.createCell(2).setCellValue(rs.getString(3));
                row2.createCell(3).setCellValue(rs.getString(4));
                row2.createCell(4).setCellValue(rs.getString(5));
                row2.createCell(5).setCellValue(rs.getInt(6));
                row2.createCell(6).setCellValue(rs.getDate(7));
                row2.createCell(7).setCellValue(rs.getDate(8));
                row2.createCell(8).setCellValue(rs.getString(9));
                row2.createCell(9).setCellValue(rs.getDouble(10));
                row2.createCell(10).setCellValue(rs.getString(11));
            }
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
            rs.close();

            conn.close();
            TrayNotification tn = new TrayNotification("NEW EXCEL FILE", "Specified excel file successfully generated", NotificationType.SUCCESS);
            tn.showAndWait();
        } catch (SQLException | IOException e) {
            TrayNotification tn = new TrayNotification("NEW EXCEL FILE", "Could not generate specified file", NotificationType.ERROR);
            tn.showAndWait();
            System.err.println(e);

        }
    }

    @FXML
    private void actionActivate(ActionEvent event) {
        try {
            conn = functions.connectDB();
            String sus = "UPDATE mg_employees SET emp_status='active' WHERE employee_id='" + editID + "'";
            if (editID >= 1) {
                int success = conn.createStatement().executeUpdate(sus);
                if (success == 1) {
                    TrayNotification tn = new TrayNotification("EMPLOYEE STATUS", "ACTIVATED SUCCESSFULLY", NotificationType.INFORMATION);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndDismiss(Duration.seconds(2));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void search(ActionEvent event) {
        try {
            boolean isFarmer = false;
            String query = "SELECT * FROM mg_employees WHERE employee_id=?";
            conn = functions.connectDB();
            ps = conn.prepareStatement(query);
            ps.setString(1, txtSearch.getText());
            rs = ps.executeQuery();
            while (rs.next()) {

                txtNatID.setText(String.valueOf(rs.getInt(2)));
                txtSurName.setText(rs.getString(3));
                txtMidName.setText(rs.getString(4));
                txtLastName.setText(rs.getString(5));
                txtMobile.setText(String.valueOf(rs.getInt(6)));
                chJobCat.getSelectionModel().selectFirst();
                isFarmer = true;
            }
            if (!isFarmer) {
                reset();
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void cleanUp(ActionEvent event) {
        reset();
        txtSearch.setText(null);
        txtDateOfBirth.setValue(null);
        chJobCat.getSelectionModel().selectFirst();
        chkPrint.setSelected(false);
    }

    private void generateReceipt() throws JRException {
        HashMap params = new HashMap();
        params.put("_empid", txtEmpID.getText());
        params.put("_sname", txtSurName.getText());
        params.put("_mmane", txtMidName.getText());
        params.put("_lname", txtLastName.getText());
        params.put("_mobno", txtMobile.getText());
        params.put("_natid", txtNatID.getText());
        params.put("_wage", spWage.getValue().toString());
        params.put("_job", chJobCat.getValue());
        params.put("_dob", txtDateOfBirth.getValue().toString());

        String report = "src\\pkgreports\\empReceipt.jrxml";
        JasperReport jr = JasperCompileManager.compileReport(report);
        JasperPrint jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
        JasperViewer.viewReport(jp, false);
    }

    @FXML
    private void logOut(ActionEvent event) throws IOException {
        Stage current = (Stage) btnClear.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(Functions.LOGIN));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        current.hide();
        stage.show();
    }

    @FXML
    private void EmployeePaymentsView(ActionEvent event) {
    }

    @FXML
    private void FarmerPaymentsView(ActionEvent event) {
    }

}
