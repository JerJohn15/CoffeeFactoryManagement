package pkgmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import pkgmodels.chemicals;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author malware
 */
public class FXchemicalsController implements Initializable {

    @FXML
    private TextField txtInputName;
    @FXML
    private ComboBox comInputTypes;
    private Spinner spinAmount;
    private Spinner spinPrice;
    @FXML
    private TableView<chemicals> tableView;
    @FXML
    private TableColumn<chemicals, Integer> colID;
    @FXML
    private TableColumn<chemicals, String> colName;
    @FXML
    private TableColumn<chemicals, String> colType;
    @FXML
    private TableColumn<chemicals, Double> colInitial;
    @FXML
    private TableColumn<chemicals, Double> colRemaining;
    @FXML
    private TableColumn<chemicals, Double> colPrice;
    @FXML
    private TableColumn<chemicals, String> colDate;
    @FXML
    private TableColumn<chemicals, String> colAvailability;
    @FXML
    private RadioButton rdDefault;
    @FXML
    private RadioButton rdAvail;
    @FXML
    private RadioButton rdUnavail;
    @FXML
    private RadioButton rdHerb;
    @FXML
    private RadioButton rdFert;
    @FXML
    private RadioButton rdAvailFert;
    @FXML
    private RadioButton rdAvailHerb;
    private ObservableList<chemicals> data;
    private Functions functions;

    private static String query = "";
    @FXML
    private ToggleGroup viewsToogle;
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
    private Button btnNew;
    private PreparedStatement pst;
    private Connection conn;
    private ResultSet rs;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtPrice;
    @FXML
    private Button btnSave;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnEdit;
    private Button btnDelete;
    @FXML
    private Button btnClear;
    private int inp;
    @FXML
    private Button btnToExcel;
    @FXML
    private Button btnRefreshTable;
    @FXML
    private Button btnPrint;
    @FXML
    private MenuItem logoutMI;
    @FXML
    private MenuItem employeePaymentMI;
    @FXML
    private MenuItem farmerpaymentsMI;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Buttons
        btnSearch.disableProperty().bind(txtSearch.textProperty().isEmpty());
        btnEdit.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));

        //set up combo values
        comInputTypes.getItems().addAll("Herbicide", "Fertilizer");
        comInputTypes.getSelectionModel().selectFirst();
        //Methods
        getInputID();
        prepareEditing();

        functions = new Functions();
    }

    private void BuildTableData(String query) {
        try {
            data = FXCollections.observableArrayList();
            conn = functions.connectDB();
            ResultSet set = conn.createStatement().executeQuery(query);
            while (set.next()) {
                int inputid = set.getInt(1);
                String inputname = set.getString(2).toUpperCase();
                String inputtype = set.getString(3);
                Double initamount = set.getDouble(4);
                Double remamount = set.getDouble(5);
                Double price = set.getDouble(6);
                String date = set.getDate(7).toString();
                String avail = set.getString(8);

                data.add(new chemicals(inputid, inputname, inputtype, initamount, remamount, price, date, avail));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        colID.setCellValueFactory(new PropertyValueFactory<>("inputid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("inputname"));
        colType.setCellValueFactory(new PropertyValueFactory<>("inputtype"));
        colInitial.setCellValueFactory(new PropertyValueFactory<>("initamount"));
        colRemaining.setCellValueFactory(new PropertyValueFactory<>("remamount"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));

        tableView.setItems(null);
        tableView.setItems(data);

    }

    @FXML
    private void viewDefault(ActionEvent event) {
        //data.clear();
        query = "select * from mg_inputs";
        BuildTableData(query);
    }

    @FXML
    private void viewAvailable(ActionEvent event) {
        query = "select * from mg_inputs where availability='available'";
        BuildTableData(query);
    }

    @FXML
    private void viewUnavailable(ActionEvent event) {
        query = "select * from mg_inputs where availability='unavailable'";
        BuildTableData(query);
    }

    @FXML
    private void viewHerbicides(ActionEvent event) {
        query = "SELECT * FROM mg_inputs WHERE input_type='herbicide'";
        BuildTableData(query);
    }

    @FXML
    private void viewFertilizers(ActionEvent event) {
        query = "SELECT * FROM mg_inputs WHERE input_type='fertilizer'";
        BuildTableData(query);
    }

    @FXML
    private void viewAvailFert(ActionEvent event) {
        query = "SELECT * FROM mg_inputs WHERE input_type='fertilizer' and availability='available'";
        BuildTableData(query);
    }

    @FXML
    private void viewAvailHerbs(ActionEvent event) {
        query = "SELECT * FROM mg_inputs WHERE input_type='herbicide' and availability='available'";
        BuildTableData(query);
    }

    @FXML
    private void clerkView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) txtInputName.getScene().getWindow();
            functions.NewStage(Functions.CLERK, "CLERK PANEL", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXchemicalsController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Stage currentStage = (Stage) txtInputName.getScene().getWindow();
            functions.NewStage(Functions.FARMERS, "FARMERS MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXchemicalsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void EmployeeView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) txtInputName.getScene().getWindow();
            functions.NewStage(Functions.EMPLOYEES, "EMPLOYEES MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXchemicalsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void AttendanceView(ActionEvent event) {

        try {
            Stage currentStage = (Stage) txtInputName.getScene().getWindow();
            functions.NewStage(Functions.ATTENDANCE, "ATTENDANCE MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXchemicalsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ExportationView(ActionEvent event) {
        try {
            Stage currentStage = (Stage) txtInputName.getScene().getWindow();
            functions.NewStage(Functions.EXPORT, "EXPORTATION MANAGEMENT", true);
            currentStage.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXchemicalsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ChemicalsView(ActionEvent event) {

    }


    @FXML
    private void newAction(ActionEvent event) {

    }

    @FXML
    private void saveInput(ActionEvent event) {

        String insert = "INSERT INTO mg_inputs(input_id,input_name,input_type,initial_amount,remaining_amount,price_per_kg,date_recorded,availability) "
                + "values(null,?,?,?,?,?,CURRENT_DATE(), 'AVAILABLE')";
        try {
            conn = functions.connectDB();
            pst = conn.prepareStatement(insert);
            pst.setString(1, txtInputName.getText());
            pst.setString(2, comInputTypes.getValue().toString());
            pst.setString(3, txtAmount.getText());
            pst.setString(4, txtAmount.getText());
            pst.setString(5, txtPrice.getText());
            if (noEmpty()) {

                if (functions.isDouble(txtPrice)) {
                    if (functions.isDouble(txtAmount)) {
                        int success = pst.executeUpdate();
                        if (success == 1) {
                            TrayNotification tn = new TrayNotification("STATUS", "Recorded successfully", NotificationType.SUCCESS);
                            tn.setAnimationType(AnimationType.POPUP);
                            tn.showAndDismiss(Duration.seconds(2));
                            txtAmount.setText(null);
                            txtInputName.setText(null);
                            txtPrice.setText(null);
                        }
                    } else {
                        txtAmount.requestFocus();
                    }
                } else {
                    txtPrice.requestFocus();
                }

            } else {
                TrayNotification tn = new TrayNotification("EMPTY FIELDS", "Empty inputs not allowed", NotificationType.WARNING);
                tn.setAnimationType(AnimationType.POPUP);
                tn.showAndDismiss(Duration.seconds(2));
            }

        } catch (Exception e) {
            System.err.println("Error inserting" + e);

            TrayNotification tn = new TrayNotification("ERROR", "Recording error occured", NotificationType.ERROR);
            tn.setAnimationType(AnimationType.POPUP);
            tn.showAndDismiss(Duration.seconds(2));

        }

    }

    boolean noEmpty() {
        return ((!txtAmount.getText().isEmpty() || !txtAmount.getText().equals("")) && (!txtPrice.getText().isEmpty() || !txtPrice.getText().equals("")) && (!txtInputName.getText().isEmpty()));
    }

    @FXML
    private void searchCommand(ActionEvent event) {
        query = "SELECT * FROM mg_inputs WHERE input_name like '%" + txtSearch.getText().trim() + "%'";
        BuildTableData(query);
    }

    @FXML
    private void editInput(ActionEvent event) throws SQLException {
        String edit = "UPDATE mg_inputs SET input_name=?,input_type=?,price_per_kg=? WHERE input_id=?";
        conn = functions.connectDB();
        pst = conn.prepareStatement(edit);
        pst.setString(1, txtInputName.getText());
        pst.setString(2, comInputTypes.getValue().toString());
        pst.setString(3, txtPrice.getText());
        pst.setInt(4, getInputID());
        int sucess = pst.executeUpdate();
        if (sucess == 1) {
            TrayNotification tn = new TrayNotification("UPDATE", "Updated successfully", NotificationType.SUCCESS);
            tn.setAnimationType(AnimationType.POPUP);
            tn.showAndDismiss(Duration.seconds(2));
            tableView.getItems().clear();
            txtAmount.setText(null);
            txtInputName.setText(null);
        } else {
            TrayNotification tn = new TrayNotification("UPDATE", "Update failed", NotificationType.NOTICE);
            tn.setAnimationType(AnimationType.POPUP);
            tn.showAndDismiss(Duration.seconds(2));
            txtAmount.setText(null);
            txtInputName.setText(null);
        }

    }

    private Integer getInputID() {

        tableView.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
            if (nv == null) {
                inp = ov.getInputid();
            } else {
                inp = nv.getInputid();
            }

        });
        return inp;
    }

    @FXML
    private void clearInputs(ActionEvent event) {
        txtSearch.setText(null);
        txtInputName.setText(null);
        txtPrice.setText(null);
        txtPrice.setText(null);
        comInputTypes.getSelectionModel().selectFirst();
        tableView.getItems().clear();
        txtAmount.setText(null);
    }

    void prepareEditing() {
        tableView.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
            if (nv == null) {
                txtInputName.setText(null);
                txtPrice.setText(null);
                return;
            }
            txtInputName.setText(nv.getInputname());
            txtPrice.setText(String.valueOf(nv.getPrice()));
        });

    }

    @FXML
    private void toExcel(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel Files(*.xls)", "*.xls");
        chooser.getExtensionFilters().add(filter);
        // Show save dialog
        File file = chooser.showSaveDialog(btnToExcel.getScene().getWindow());
        if (file != null) {
            saveXLSFile(file);

        }
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        tableView.getItems().clear();
        query = "select * from mg_inputs";
        BuildTableData(query);

    }

    @FXML
    private void generateReport(ActionEvent event) throws JRException {
        conn = functions.connectDB();
        String report = "src\\pkgreports\\chemicalsReport.jrxml";
        JasperReport jr = JasperCompileManager.compileReport(report);
        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
        JasperViewer.viewReport(jp, false);
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
            row1.createCell(0).setCellValue("INPUT ID");
            row1.createCell(1).setCellValue("INPUT NAME");
            row1.createCell(2).setCellValue(" TYPE");
            row1.createCell(3).setCellValue("INITIAL AMOUNT");
            row1.createCell(4).setCellValue("REMAINING AMT");
            row1.createCell(5).setCellValue("PRICE");
            //row1.createCell(6).setCellValue("REG DATE");
            row1.createCell(6).setCellValue("AVAILABILITY");
            Row row2;

            rs = conn.createStatement().executeQuery("select * from mg_inputs");
            while (rs.next()) {
                int a = rs.getRow();
                row2 = workSheet.createRow((short) a);
                row2.createCell(0).setCellValue(rs.getInt(1));
                row2.createCell(1).setCellValue(rs.getString(2));
                row2.createCell(2).setCellValue(rs.getString(3));
                row2.createCell(3).setCellValue(rs.getDouble(4));
                row2.createCell(4).setCellValue(rs.getDouble(5));
                row2.createCell(5).setCellValue(rs.getDouble(6));
                // row2.createCell(6).setCellValue(rs.getDate(7));
                row2.createCell(6).setCellValue(rs.getString(8));

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
    private void EmployeePaymentsView(ActionEvent event) {
    }

    @FXML
    private void FarmerPaymentsView(ActionEvent event) {
    }
}
