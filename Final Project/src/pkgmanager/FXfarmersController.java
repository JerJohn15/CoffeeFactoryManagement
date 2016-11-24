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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
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
import pkgmodels.farmer;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author malware
 */
public class FXfarmersController implements Initializable {

    @FXML
    private TextField txtarmerID;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtMidname;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtMobileNo;
    @FXML
    private TextField txtNationalID;
    @FXML
    private TextField txtAccNo;
    @FXML
    private ComboBox<String> cmbSacco;
    @FXML
    private CheckBox checkPrint;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnExport;
    private Functions functions;
    private Connection conn;
    private PreparedStatement ps;
    private Statement statement;
    private ResultSet rs;
    private ObservableList<farmer> data;
    @FXML
    private TableColumn<farmer, Integer> colFarmerID;
    @FXML
    private TableColumn<farmer, String> colSurname;
    @FXML
    private TableColumn<farmer, String> colMidname;
    @FXML
    private TableColumn<farmer, String> colLastname;
    @FXML
    private TableColumn<farmer, Integer> colMobNo;
    @FXML
    private TableColumn<farmer, Integer> colNatID;
    @FXML
    private TableColumn<farmer, Integer> colAccNo;
    @FXML
    private TableColumn<farmer, String> colSacco;
    @FXML
    private TableView<farmer> tableFarmer;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtSearch;
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
    static int deleteID;
    @FXML
    private MenuItem logoutMI;
    @FXML
    private MenuItem employeePaymentMI;
    @FXML
    private MenuItem farmerpaymentsMI;
    public static int FARMER_ID;
    @FXML
    private Button btnDetails;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        functions = new Functions();
        //Populate Combo with Saccos
        ObservableList<String> SACCOS = FXCollections.observableArrayList("Baraka", "Mathira", "Faulu", "Mwanzia");
        cmbSacco.setItems(SACCOS);
        cmbSacco.getSelectionModel().selectFirst();
        btnDelete.setStyle("-fx-base: red");
        btnClear.setStyle("-fx-base: red");
        btnSave.setStyle("-fx-base: #27ae60");
        btnDetails.setDisable(true);

        /**
         * Some validation properties bindings*****
         */
        btnSave.disableProperty().bind(txtarmerID.textProperty().isEmpty());

        //Set tiptool text on Export Button
        Tooltip tExport = new Tooltip("Export to Excel");
        btnExport.setTooltip(tExport);

        //Set tiptool text on Export Button
        Tooltip tPrint = new Tooltip("Print Table Data");
        btnPrint.setTooltip(tPrint);

        //Set tiptool text on Export Button
        Tooltip tRefresh = new Tooltip("Refresh Tableview");
        btnRefresh.setTooltip(tRefresh);

        // Populate farmers table info with initial data
        buildTableviewData();

    }

    @FXML
    private void ActionNew(ActionEvent event) {
        clearFields();
        try {
            txtarmerID.setText(String.valueOf(functions.getFarmerID()));

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void ActionEdit(ActionEvent event) throws SQLException {
        if (deleteID == 0) {
            return;
        }
        conn = functions.connectDB();
        String edit = "UPDATE mg_farmers SET sur_name=?,mid_name=?,last_name=?,mobile_no=?,national_id=?,account_no=?,sacco_enrolled=?"
                + " WHERE farmer_id=?";
        ps = conn.prepareStatement(edit);
        ps.setString(1, txtSurname.getText().toUpperCase());
        ps.setString(2, txtMidname.getText().toUpperCase());
        ps.setString(3, txtLastName.getText().toUpperCase());
        ps.setString(4, txtMobileNo.getText());
        ps.setString(5, txtNationalID.getText());
        ps.setString(6, txtAccNo.getText());
        ps.setString(7, cmbSacco.getValue());
        ps.setInt(8, deleteID);
        if (functions.isValidInteger(txtMobileNo, 10)) {
            if (functions.isValidInteger(txtNationalID, 12)) {
                if (functions.isValidInteger(txtAccNo, 20)) {
                    int success = ps.executeUpdate();
                    if (success == 1) {
                        TrayNotification tn = new TrayNotification();
                        tn.setTitle("EDIT STATUS");
                        tn.setMessage("Farmer has been edited successfully");
                        tn.setNotificationType(NotificationType.SUCCESS);
                        tn.setAnimationType(AnimationType.SLIDE);
                        tn.showAndWait();
                        buildTableviewData();
                    }
                }

            } else {
                Alert a = new Alert(AlertType.ERROR, "Check National ID field input,should be integer not exceeding 12 digits", ButtonType.OK);
                a.show();
                txtNationalID.requestFocus();
            }

        } else {
            Alert a = new Alert(AlertType.ERROR, "Check  Phone number field inputs,should be integer not exceeding 10 digits", ButtonType.OK);
            a.show();
            txtMobileNo.requestFocus();
        }

    }

    @FXML
    private void ActionDelete(ActionEvent event) {
        if (deleteID == 0) {
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("DELETE");
        alert.setHeaderText("FARMER ID :" + deleteID);
        alert.setContentText("Are you sure you want to delete the specified farmer ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                String del = "delete from mg_farmers where farmer_id=?";
                conn = functions.connectDB();
                ps = conn.prepareStatement(del);
                ps.setInt(1, deleteID);
                int success = ps.executeUpdate();
                if (success == 1) {
                    TrayNotification tn = new TrayNotification("STATUS", "Farmer is deleted", NotificationType.SUCCESS);
                    tn.setAnimationType(AnimationType.POPUP);
                    tn.showAndDismiss(Duration.seconds(1));
                    buildTableviewData();
                }

            } catch (Exception e) {
                TrayNotification tn = new TrayNotification("STATUS", "Could not delete specified farmer", NotificationType.ERROR);
                tn.showAndDismiss(Duration.seconds(1));
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    @FXML
    private void ActionSave(ActionEvent event) {
        // My damn variables
        String farmerid = txtarmerID.getText();
        String surname = txtSurname.getText();
        String midname = txtMidname.getText();
        String lastname = txtLastName.getText();
        String mobileno = txtMobileNo.getText();
        String NationalID = txtNationalID.getText();
        String AccNo = txtAccNo.getText();
        String sacco = cmbSacco.getValue();

        String query = "INSERT INTO mg_farmers("
                + "farmer_id,sur_name,mid_name,last_name,mobile_no,date_registered,national_id,account_no,sacco_enrolled)"
                + " VALUES(null,?,?,?,?,current_date(),?,?,?)";

        try {
            conn = functions.connectDB();
            ps = conn.prepareStatement(query);
            ps.setString(1, surname);
            ps.setString(2, midname);
            ps.setString(3, lastname);
            ps.setString(4, mobileno);
            ps.setString(5, NationalID);
            ps.setString(6, AccNo);
            ps.setString(7, sacco);
            if (functions.isValidInteger(txtMobileNo, 10)) {
                if (functions.isValidInteger(txtNationalID, 12)) {
                    if (functions.isValidInteger(txtAccNo, 20)) {
                        int success = ps.executeUpdate();
                        if (success == 1) {
                            if (checkPrint.isSelected()) {
                                printRegistration();
                            }
                            clearFields();
                            TrayNotification tn = new TrayNotification();
                            tn.setTitle("SAVING STATUS");
                            tn.setMessage("New farmer has been registered into the system successfully");
                            tn.setNotificationType(NotificationType.SUCCESS);
                            tn.setAnimationType(AnimationType.SLIDE);
                            tn.showAndWait();
                        }
                    } else {
                        Alert a = new Alert(AlertType.ERROR, "Check Account Number field input,should not contain characters", ButtonType.OK);
                        a.show();
                        txtNationalID.requestFocus();
                    }
                } else {
                    Alert a = new Alert(AlertType.ERROR, "Check National ID field input,should be integer not exceeding 12 digits", ButtonType.OK);
                    a.show();
                    txtNationalID.requestFocus();
                }
            } else {
                Alert a = new Alert(AlertType.ERROR, "Check  Phone number field inputs,should be integer not exceeding 10 digits", ButtonType.OK);
                a.show();
                txtMobileNo.requestFocus();
            }
            conn.close();
            ps.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            TrayNotification tn = new TrayNotification();
            tn.setTitle("SAVING STATUS");
            tn.setMessage("Registration Failed \n Fill all the fields before saving");
            tn.setNotificationType(NotificationType.ERROR);
            tn.setAnimationType(AnimationType.POPUP);
            tn.showAndWait();
        }

    }

    @FXML
    private void ActionClear(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void PrintAction(ActionEvent event) throws JRException {
        String report = "src\\pkgreports\\finalFarmers.jrxml";
        conn = functions.connectDB();

        JasperReport jr = JasperCompileManager.compileReport(report);
        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
        JasperViewer.viewReport(jp, false);

    }

    @FXML
    private void RefreshAction(ActionEvent event) {
        tableFarmer.getItems().clear();
        buildTableviewData();
    }

    @FXML
    private void ExportAction(ActionEvent event) {
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

    private void clearFields() {
        txtAccNo.setText(null);
        txtLastName.setText(null);
        txtMidname.setText(null);
        txtMobileNo.setText(null);
        txtNationalID.setText(null);
        txtSurname.setText(null);
        txtarmerID.setText(null);
        cmbSacco.getSelectionModel().selectFirst();

    }

    private void buildTableviewData() {
        try {
            conn = functions.connectDB();
            data = FXCollections.observableArrayList();
            statement = conn.createStatement();
            rs = statement.executeQuery("SELECT * FROM mg_farmers");
            while (rs.next()) {
                int FarmerID = rs.getInt(1);
                String Sname = rs.getString(2).toUpperCase();
                String MName = rs.getString(3).toUpperCase();
                String LName = rs.getString(4).toUpperCase();
                int Mobile = rs.getInt(5);
                int National = rs.getInt(7);
                int Acount = rs.getInt(8);
                String sacco = rs.getString(9).toUpperCase();

                data.add(new farmer(FarmerID, Sname, MName, LName, Mobile, National, Acount, sacco));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        colFarmerID.setCellValueFactory(new PropertyValueFactory<>("farmerid"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colMidname.setCellValueFactory(new PropertyValueFactory<>("midname"));
        colLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        colMobNo.setCellValueFactory(new PropertyValueFactory<>("mobileno"));
        colNatID.setCellValueFactory(new PropertyValueFactory<>("nationalidno"));
        colAccNo.setCellValueFactory(new PropertyValueFactory<>("accountno"));
        colSacco.setCellValueFactory(new PropertyValueFactory<>("sacco"));

        tableFarmer.setItems(null);
        tableFarmer.setItems(data);

    }

    @FXML
    private void search(ActionEvent event) {
        
        clearFields();
        if (!txtSearch.getText().isEmpty() && txtSearch.getText() != null) {
            setInfo(txtSearch.getText());
            
        }

    }

    //Method to query database and display info to textfields
    private void setInfo(String farmer_id) {
        try {
            btnDetails.setDisable(true);
            boolean exists = false;
            conn = functions.connectDB();
            String query = "select * from mg_farmers where farmer_id=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, farmer_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                //txtarmerID.setText(String.valueOf());
                txtSurname.setText(rs.getString(2));
                txtMidname.setText(rs.getString(3));
                txtLastName.setText(rs.getString(4));
                txtMobileNo.setText(String.valueOf(rs.getInt(5)));
                txtAccNo.setText(String.valueOf(rs.getInt(7)));
                txtNationalID.setText(String.valueOf(rs.getInt(8)));
                cmbSacco.setId(rs.getString(9));
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
                deleteID = rs.getInt(1);
                FARMER_ID=Integer.parseInt(farmer_id);
                exists = true;
                btnDetails.setDisable(false);

            }
            if (!exists) {
                TrayNotification tn = new TrayNotification("SEARCH RESULTS", "Specified farmer does not exist", NotificationType.NOTICE);
                tn.setAnimationType(AnimationType.POPUP);
                tn.showAndDismiss(Duration.seconds(1));
                btnDetails.setDisable(true);
                deleteID = 0;
            }
            conn.close();
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void clerkView(ActionEvent event) throws IOException {
        Stage window = (Stage) btnClear.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(Functions.CLERK));
        Stage s = new Stage();
        Scene scene = new Scene(root);
        s.setScene(scene);
        s.show();
        window.hide();
    }

    @FXML
    private void quitOption(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void ActionNewClerk(ActionEvent event) {
        //custonm fx dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("NEW CLERK");
        dialog.setHeaderText("Create new clerk here !");
        //setting button types
        ButtonType createButtonType = new ButtonType("CREATE", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        //Creating username password and full name labels
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        TextField fullname = new TextField();
        fullname.setPromptText("Full Names");
        //Adding fields to the grid
        grid.add(new Label("Username"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("Full Names:"), 0, 2);
        grid.add(fullname, 1, 2);

        //enable/disable button depending on whether username was inputed
        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);
        //Validation using java 8 lambda syntax
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        //Request focus on the username field by default
        Platform.runLater(() -> username.requestFocus());
        //convert the result to username -password pair when the log in button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Username :" + username.getText());
            System.out.println("Password :" + password.getText());
        }

    }

    @FXML
    private void ActionChangePass(ActionEvent event) {
    }

    @FXML
    private void FarmersView(ActionEvent event) {
    }

    @FXML
    private void EmployeeView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.EMPLOYEES, "EMPLOYEES MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void AttendanceView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.ATTENDANCE, "ATTENDANCE MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ExportationView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.EXPORT, "EXPORTATION MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ChemicalsView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.CHEMICALS, "CHEMICALS MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void PaymentsView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.FARMERS, "FARMERS MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            row1.createCell(0).setCellValue("Farmer ID");
            row1.createCell(1).setCellValue("Sur Name");
            Row row2;

            rs = conn.createStatement().executeQuery("SELECT mg_farmers.farmer_id,mg_farmers.sur_name FROM mg_farmers");
            while (rs.next()) {
                int a = rs.getRow();
                row2 = workSheet.createRow((short) a);
                row2.createCell(0).setCellValue(rs.getString(1));
                row2.createCell(1).setCellValue(rs.getString(2));
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

    private void printRegistration() throws JRException {
        Map params = new HashMap();
        params.put("farmerid", txtarmerID.getText());
        params.put("surname", txtSurname.getText());
        params.put("midname", txtMidname.getText());
        params.put("lastname", txtLastName.getText());
        params.put("mobile", txtMobileNo.getText());
        params.put("national", txtNationalID.getText());
        params.put("account", txtAccNo.getText());
        params.put("sacco", cmbSacco.getValue());

        String report = "src\\pkgreports\\regFarmer.jrxml";
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

    @FXML
    private void detailedView(ActionEvent event) throws IOException {
        Stage stage=new Stage();
        Parent root=FXMLLoader.load(getClass().getResource(Functions.DETAILED));
        stage.setTitle("DETAILED FARMER VIEW");
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        
    }
}
