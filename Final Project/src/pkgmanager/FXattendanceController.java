/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgmanager;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import pkgcommon.Functions;
import pkgmodels.absentees;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author malware
 */
public class FXattendanceController implements Initializable {

    @FXML
    private Button btnClearStats;
    @FXML
    private RadioButton rdPresent;
    @FXML
    private ToggleGroup attendance;
    @FXML
    private RadioButton rdAbsent;
    @FXML
    private Button btnRecord;
    @FXML
    private ComboBox cbReason;
    @FXML
    private TextField txtLastMonth;
    @FXML
    private TextField txtEmpsAbsent;
    @FXML
    private TextField txtAbsReason;
    @FXML
    private TextField txtCurMonth;
    @FXML
    private TextField txtDate;
    @FXML
    private TextField txtEmpID;
    private Functions functions;
    private ResultSet rs;
    private Connection conn;
    private ObservableList<absentees> data;
    @FXML
    private TableView<absentees> tableAttendance;
    @FXML
    private TableColumn<absentees, Integer> colEmpID;
    @FXML
    private TableColumn<absentees, String> colNames;
    @FXML
    private TableColumn<absentees, String> colJob;
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
        // Initialise functions class
        functions = new Functions();
        //set values to combo

        cbReason.getItems().addAll("Health", "Late", "Unspecified", "Excused", "Suspended");
        cbReason.getSelectionModel().selectFirst();

        getPresentEmplyees();
        //button styles
        btnClearStats.setStyle("-fx-base: red;");
        btnRecord.setStyle("-fx-base: green;");

        cbReason.disableProperty().bind(rdAbsent.selectedProperty().not());
        btnRecord.disableProperty().bind(rdAbsent.selectedProperty().not().or(txtEmpID.textProperty().isEmpty()));
        //Dates 
        monthlyStats();

        //tableview listener
        tableAttendance.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                absentees abs = (absentees) newValue;
                if (!tableAttendance.getSelectionModel().isEmpty()) {
                    txtEmpID.setText(String.valueOf(abs.toInt()));
                }

            }
        });
    }

    @FXML
    private void clearLastMonthStats(ActionEvent event) {
        LocalDate date = LocalDate.now();
        try {
            conn = functions.connectDB();
            Statement statement = conn.createStatement();
            String query = "DELETE FROM mg_absentees WHERE calender_month='" + date.getMonth().minus(1).toString() + "'";
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("CLEAR LAST MONTH DATA ?");
            a.setContentText("Applying this action will clear all absentism records. \n"
                    + "Please apply this action if you ae sure you are done with the data\n"
                    + "If you wish to continue,proceed by clicking okey,else cancel.");
            a.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        try {
                            int success = statement.executeUpdate(query);
                            TrayNotification tn = new TrayNotification("CLEARED ", "Statistics for last month fully cleared", NotificationType.SUCCESS);
                            tn.setAnimationType(AnimationType.FADE);
                            tn.showAndDismiss(Duration.seconds(1));
                        } catch (SQLException ex) {
                            Logger.getLogger(FXattendanceController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        monthlyStats();
                    });
        } catch (SQLException ex) {
            Logger.getLogger(FXattendanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getPresentEmplyees() {
        try {
            conn = functions.connectDB();
            data = FXCollections.observableArrayList();
            String query = "SELECT employee_id,CONCAT_WS(\"  \",surname,middlename,lastname ) as emp_names,job_category "
                    + "FROM mg_employees WHERE emp_status='active' and "
                    + "employee_id not in (SELECT emp_id FROM mg_absentees WHERE dates_absent=current_date())";

//            String query = "select employee_id,surname,lastname from mg_employees";
            rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                data.add(new absentees(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }

        } catch (Exception e) {
            System.err.println("Error building date");
            System.err.println(e);
        }
        colEmpID.setCellValueFactory(new PropertyValueFactory<>("empid"));
        colNames.setCellValueFactory(new PropertyValueFactory<>("names"));
        colJob.setCellValueFactory(new PropertyValueFactory<>("job"));

        tableAttendance.setItems(null);
        tableAttendance.setItems(data);

    }

    @FXML
    private void recordAttendance(ActionEvent event) {
        try {
            conn = functions.connectDB();
            String insert = "INSERT INTO mg_absentees(abs_id,emp_id,dates_absent,calender_month,reason) VALUES (null,?,current_date(),?,?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setString(1, txtEmpID.getText());
            ps.setString(2, txtCurMonth.getText());
            ps.setString(3, cbReason.getValue().toString());
            ps.execute();
            System.out.println("inserted");
        } catch (Exception e) {
            System.err.println(e);
        }

        data.removeAll(tableAttendance.getSelectionModel().getSelectedItems());
        getPresentEmplyees();
        txtEmpID.setText(null);

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
            Stage currentWindow = (Stage) btnClearStats.getScene().getWindow();
            functions.NewStage(Functions.FARMERS, "FARMERS MANAGEMENT", true);
            currentWindow.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXattendanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void EmployeeView(ActionEvent event) {
        try {
            Stage currentWindow = (Stage) btnClearStats.getScene().getWindow();
            functions.NewStage(Functions.EMPLOYEES, "EMPLOYEES MANAGEMENT", true);
            currentWindow.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXattendanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void AttendanceView(ActionEvent event) {

    }

    @FXML
    private void ExportationView(ActionEvent event) {
        try {
            Stage currentWindow = (Stage) btnClearStats.getScene().getWindow();
            functions.NewStage(Functions.EXPORT, "EXPORTATION MANAGEMENT", true);
            currentWindow.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXattendanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ChemicalsView(ActionEvent event) {
        try {
            Stage currentWindow = (Stage) btnClearStats.getScene().getWindow();
            functions.NewStage(Functions.CHEMICALS, "CHEMICALS MANAGEMENT", true);
            currentWindow.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXattendanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void monthlyStats() {
        try {
            LocalDate date = LocalDate.now();
            txtLastMonth.setText(date.getMonth().minus(1).toString());
            txtCurMonth.setText(date.getMonth().toString());
            txtDate.setText(date.toString());
            conn = functions.connectDB();
            String query = "SELECT reason,count(reason) as frequency FROM mg_absentees WHERE calender_month= '" + date.getMonth().minus(1).toString() + "'GROUP BY reason order BY frequency limit 1";
            rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                txtAbsReason.setText(rs.getString(1));

            }
            rs.close();
            String query2 = "SELECT count(emp_id) FROM mg_absentees WHERE calender_month='" + date.getMonth().minus(1).toString() + "'";
            rs = conn.createStatement().executeQuery(query2);
            while (rs.next()) {
                txtEmpsAbsent.setText(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXattendanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void logOut(ActionEvent event) throws IOException {
        Stage current = (Stage) btnRecord.getScene().getWindow();
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
