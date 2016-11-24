package pkgmanager;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import pkgcommon.Functions;
import pkgmodels.paymentsE;

/**
 * FXML Controller class
 *
 * @author malware
 */
public class FXpaymentsEmpController implements Initializable {

    @FXML
    private CheckBox chkLate;
    @FXML
    private CheckBox chkUnspecified;
    @FXML
    private CheckBox chkExcused;
    @FXML
    private CheckBox chkSuspended;
    @FXML
    private CheckBox chkHealth;
    @FXML
    private ComboBox comboMonth;
    private Connection conn;
    private Functions functions;
    @FXML
    private Button btnCalculate;
    @FXML
    private TextField txtPenalty;
    @FXML
    private TableView<paymentsE> tablePayments;
    private ObservableList<paymentsE> p_data;

    static double init_penalty = 0.0;
    static double total_penalty = 0.0;
    static double total_wages = 0.0;
    static double total_net_wages = 0.0;
    static int abs = 0;

    @FXML
    private TableColumn<Integer, paymentsE> colEmpID;
    @FXML
    private TableColumn<Double, paymentsE> colTotalWage;
    @FXML
    private TableColumn<Double, paymentsE> colPenalty;
    @FXML
    private TableColumn<Integer, paymentsE> colDays;
    @FXML
    private TableColumn<Double, paymentsE> colNetWage;
    @FXML
    private TextField tSalary;
    @FXML
    private TextField tAbsent;
    @FXML
    private TextField tPenalty;
    @FXML
    private TextField tNetwage;
    @FXML
    private Button btnClear;
    @FXML
    private TextField tMonth;
    @FXML
    private Button btnReport;
    @FXML
    private MenuItem clerkMI;
    @FXML
    private MenuItem logoutMI;
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
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        String currentMonth = date.getMonth().toString().toUpperCase() + "-" + String.valueOf(year);
        String lastMonth = date.getMonth().minus(1).toString().toUpperCase() + "-" + String.valueOf(year);
        comboMonth.getItems().addAll(lastMonth, currentMonth);
        comboMonth.getSelectionModel().selectFirst();

        functions = new Functions();

        txtPenalty.setText(String.valueOf(init_penalty));

    }

    @FXML
    private void calculatePayments(ActionEvent event) throws SQLException, JRException {

        if (isInputAllowed()) {
            populateData(selectQuery(txtPenalty.getText()));
            calcTotals();
        }

    }

    void populateData(String query) throws SQLException {
        conn = functions.connectDB();
        p_data = FXCollections.observableArrayList();

        ResultSet rs = conn.createStatement().executeQuery(query);
        while (rs.next()) {
            p_data.add(new paymentsE(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getInt(4), rs.getDouble(5)));
        }
        tablePayments.getItems().clear();
        colEmpID.setCellValueFactory(new PropertyValueFactory<>("empid"));
        colTotalWage.setCellValueFactory(new PropertyValueFactory<>("totalwage"));
        colPenalty.setCellValueFactory(new PropertyValueFactory<>("penalty"));
        colDays.setCellValueFactory(new PropertyValueFactory<>("days"));
        colNetWage.setCellValueFactory(new PropertyValueFactory<>("netwage"));

        tablePayments.setItems(p_data);
    }

    private String selectQuery(String penal) {
        String query = "";
        String A = "";
        String B = "";
        String C = "";
        String D = "";
        String E = "";

        String month = comboMonth.getValue().toString();
        String trim = month.substring(0, month.length() - 5);
        if (chkExcused.isSelected()) {
            A = "excused";
        }
        if (chkHealth.isSelected()) {
            B = "health";
        }
        if (chkLate.isSelected()) {
            C = "late";
        }
        if (chkSuspended.isSelected()) {
            D = "suspended";
        }
        if (chkUnspecified.isSelected()) {
            E = "unspecified";
        }
        return "SELECT e.employee_id,e.wage as 'TOTAL',(count(a.emp_id)*'" + penal + "') as 'PENALTY',"
                + "count(a.emp_id) as 'DAYS ABSENT',"
                + "(e.wage -(count(a.emp_id)*'" + penal + "')) as 'NET' "
                + "FROM mg_employees e left join mg_absentees a "
                + "on e.employee_id=a.emp_id and a.reason in "
                + "('" + A + "','" + B + "','" + C + "','" + D + "','" + E + "') "
                + "and a.calender_month='" + trim + "' GROUP BY e.employee_id";
    }

    private boolean isInputAllowed() {
        if (!(txtPenalty.getText() == null || txtPenalty.getText().length() == 0)) {
            try {
                double pen = Double.parseDouble(txtPenalty.getText());
                if (pen >= 0.0) {
                    return true;
                }
            } catch (NumberFormatException nfe) {
            }
        }
        txtPenalty.requestFocus();
        return false;
    }

    private void calcTotals() {
        total_penalty = 0.0;
        total_wages = 0.0;
        total_net_wages = 0.0;
        abs = 0;

        ObservableList<paymentsE> myTotals = tablePayments.getItems();
        myTotals.stream().map((myTotal) -> {
            total_wages += myTotal.getTotalwage();
            return myTotal;
        }).map((myTotal) -> {
            total_net_wages += myTotal.getNetwage();
            return myTotal;
        }).map((myTotal) -> {
            total_penalty += myTotal.getPenalty();
            return myTotal;
        }).forEach((myTotal) -> {
            abs += myTotal.getDays();
        });
        tPenalty.setText(String.valueOf(total_penalty));
        tSalary.setText(String.valueOf(total_wages));
        tNetwage.setText(String.valueOf(total_net_wages));
        tAbsent.setText(String.valueOf(abs));
        tMonth.setText(comboMonth.getValue().toString().toUpperCase());

    }

    @FXML
    private void clearFields(ActionEvent event) {
        txtPenalty.setText(String.valueOf(init_penalty));
        chkExcused.setSelected(false);
        chkHealth.setSelected(false);
        chkLate.setSelected(false);
        chkSuspended.setSelected(false);
        chkUnspecified.setSelected(false);

        tAbsent.setText(null);
        tNetwage.setText(null);
        tPenalty.setText(null);
        tSalary.setText(null);
        tMonth.setText(null);
        tablePayments.getItems().clear();
    }

    private void createPDF() throws JRException {
        conn = functions.connectDB();
        String report = "src\\pkgreports\\empWages.jrxml";
        JasperDesign jd = JRXmlLoader.load(report);
        JRDesignQuery query = new JRDesignQuery();
        query.setText(selectQuery(txtPenalty.getText()));
        jd.setQuery(query);

        HashMap hashMap = new HashMap();
        hashMap.put("month", comboMonth.getValue().toString().toUpperCase());
        JasperReport jr = JasperCompileManager.compileReport(jd);
        JasperPrint jp = JasperFillManager.fillReport(jr, hashMap, conn);
        JasperViewer.viewReport(jp, false);

    }

    @FXML
    private void report(ActionEvent event) throws JRException {
        createPDF();
    }

    @FXML
    private void clerkView(ActionEvent event) {
         try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.CLERK, "CLERK CONSOLE", true);
            currentStage.hide();
        } catch (Exception ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
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
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.FARMERS, "FARMERS MANAGEMENT", true);
            currentStage.hide();
        } catch (Exception ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void EmployeeView(ActionEvent event) {
         try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.EMPLOYEES, "EMPLOYEES MANAGEMENT", true);
            currentStage.hide();
        } catch (Exception ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void AttendanceView(ActionEvent event) {
         try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.ATTENDANCE, "ATTENDANCE MANAGEMENT", true);
            currentStage.hide();
        } catch (Exception ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ExportationView(ActionEvent event) {
         try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.EXPORT, "EXPORT MANAGEMENT", true);
            currentStage.hide();
        } catch (Exception ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ChemicalsView(ActionEvent event) {
         try {
            Stage currentStage = (Stage) btnClear.getScene().getWindow();
            functions.NewStage(Functions.CHEMICALS, "CHEMICALS MANAGEMENT", true);
            currentStage.hide();
        } catch (Exception ex) {
            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void EmployeePaymentsView(ActionEvent event) {
//         try {
//            Stage currentStage = (Stage) btnClear.getScene().getWindow();
//            functions.NewStage(Functions.FARMERS, "FARMERS MANAGEMENT", true);
//            currentStage.hide();
//        } catch (Exception ex) {
//            Logger.getLogger(FXemployeesController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @FXML
    private void FarmerPaymentsView(ActionEvent event) {
        
    }
}
