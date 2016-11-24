package pkgclerk;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import pkgcommon.FXloginController;
import pkgcommon.Functions;
import pkgmodels.inputs;
import pkgmodels.loans;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author malware
 */
public class FXclerkController implements Initializable {

    @FXML
    private MenuItem logoutMI;
    @FXML
    private MenuItem quitMI;
    @FXML
    private MenuItem accountMI;
    @FXML
    private MenuItem cgUsernameMI;
    @FXML
    private MenuItem aboutMI;
    @FXML
    private MenuItem doumentationMI;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtFID;
    @FXML
    private TextField txtSN;
    @FXML
    private TextField txtMDNM;
    @FXML
    private TextField txtLSTNM;
    @FXML
    private CheckBox boolPrint;
    @FXML
    private TextField txtAmountDelivered;
    @FXML
    private Button btnRecrd;
    @FXML
    private Button btnClear;
    @FXML
    private TextField stYear;
    @FXML
    private TextField stDate;
    @FXML
    private TextField stRegFarmers;
    @FXML
    private TextField stAmountToday;
    @FXML
    private TextField stNoDeliveredToday;
    @FXML
    private TextField stEstimate;
    @FXML
    private Button btnRefresh;
    private Functions functions;
    private PreparedStatement pst;
    private Connection conn;
    private ResultSet rs;
    @FXML
    private TextField txtSearchF;
    @FXML
    private TableView tableHistory;
    private ObservableList<ObservableList> data;
    @FXML
    private TableView<inputs> tableInputs;
    @FXML
    private RadioButton rdAll;
    @FXML
    private ToggleGroup VIEWS;
    @FXML
    private RadioButton rdChem;
    @FXML
    private RadioButton rdFert;
    @FXML
    private TableColumn<inputs, Integer> col_id;
    @FXML
    private TableColumn<inputs, String> col_name;
    @FXML
    private TableColumn<inputs, String> col_type;
    @FXML
    private TableColumn<inputs, Integer> col_remaining;
    private ObservableList<inputs> inputs_data;
    @FXML
    private TextField txxChemID;
    @FXML
    private TextField txtInputFarmerID;
    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtChemName;
    @FXML
    private Button btnAddToList;
    @FXML
    private TextField txtType;
    @FXML
    private TableView<loans> tableLoans;
    @FXML
    private TableColumn<loans, Integer> col_loan_inputid;
    @FXML
    private TableColumn<loans, Integer> col_loan_farmerid;
    @FXML
    private TableColumn<loans, Double> col_loan_quantity;
    private ObservableList<loans> loans_data;
    @FXML
    private Button btnRecordLoaning;
    @FXML
    private Button btnRemoveRow;
    @FXML
    private Button btnClearTable;
    @FXML
    private TextField txtCum;
    @FXML
    private TextField inputSname;
    @FXML
    private TextField inputMidname;
    @FXML
    private TextField inputLsname;
    @FXML
    private TextField inputCumKgs;
    @FXML
    private TextField inputStatus;
    @FXML
    private Button inputsSerchBtn;
    @FXML
    private Button btnClearSearchResults;
    @FXML
    private TextField searchInputTxt;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Instantiating functions 
        functions = new Functions();
        //Buttons;
        btnRecrd.disableProperty().bind(Bindings.isEmpty(txtFID.textProperty()));
        btnAddToList.setStyle("-fx-base: green;");
        btnRemoveRow.disableProperty().bind(tableLoans.getSelectionModel().selectedItemProperty().isNull());
        btnAddToList.disableProperty().bind(Bindings.isEmpty(txtQty.textProperty()).or(Bindings.isEmpty(txxChemID.textProperty()).or(Bindings.isEmpty(txtInputFarmerID.textProperty()))));
        btnRecordLoaning.disableProperty().bind(Bindings.size(tableLoans.getItems()).lessThan(1));
        //Methods
        refreshStats();
        tableInputs.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends inputs> observable,
                inputs oldValue,
                inputs newValue) -> {
            if (newValue == null) {
                txxChemID.setText(null);
                txtType.setText(null);
                txtChemName.setText(null);
                return;
            }
            txxChemID.setText(String.valueOf(newValue.getId()));
            txtType.setText(newValue.getType());
            txtChemName.setText(newValue.getName());
        });

    }

    @FXML
    private void logoutOperation(ActionEvent event) throws IOException {
        Stage current = (Stage) btnSearch.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(Functions.LOGIN));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        current.hide();
        stage.show();
    }

    @FXML
    private void closeOperation(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void viewMyAccount(ActionEvent event) {
    }

    @FXML
    private void changeUsernameOperation(ActionEvent event) {
    }

    @FXML
    private void viewDocumentation(ActionEvent event) {
    }

    @FXML
    private void recordDelivery(ActionEvent event) {
        if (functions.isFarmerRegistered(Integer.parseInt(txtSearchF.getText())) && !txtSearchF.getText().isEmpty()) {
            try {

                conn = functions.connectDB();
                String record = "INSERT INTO mg_sales VALUES (null,?,current_date(),?)";
                pst = conn.prepareStatement(record);
                pst.setString(1, txtFID.getText());
                pst.setString(2, txtAmountDelivered.getText());
                // check whether amount delivered is recorded
                if (functions.isDouble(txtAmountDelivered)) {
                    int OK = pst.executeUpdate();
                    if (OK == 1) {
                        if (boolPrint.isSelected()) {
                            generateSalesReceipt();
                        }
                        TrayNotification tn = new TrayNotification("RECORD STATUS", "Recording successful", NotificationType.SUCCESS);
                        tn.setAnimationType(AnimationType.POPUP);
                        tn.showAndDismiss(Duration.millis(100));
                        ClearTextfields();

                    }
                } // request focus while amount is not recorded
                else {

                    Alert a = new Alert(Alert.AlertType.WARNING, "Enter Valid kilograms", ButtonType.OK);
                    a.showAndWait();
                    txtAmountDelivered.requestFocus();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "SORRY,THE SPECIFIED FARMER IS NOT REGISTERED ", ButtonType.OK);
            alert.show();
            ClearTextfields();
        }

    }

    @FXML
    private void clearFields(ActionEvent event) {
        ClearTextfields();
        // Clear tableview data
        tableHistory.getItems().clear();
        tableHistory.getColumns().clear();
    }

    @FXML
    public void refreshStats() {

        int delivered_farmers = 0;

        Year currentYear = Year.now();
        stYear.setText(String.valueOf(currentYear));

        LocalDate today = LocalDate.now();
        stDate.setText(String.valueOf(today));

        String statQuery = "SELECT sum(amount_delivered),count(distinct farmer_id) as D_farmers from  mg_sales "
                + "WHERE date_delivered='" + String.valueOf(today) + "'";

        String allFarmers = "select count(farmer_ID) as alF from mg_farmers";
        try {

            conn = functions.connectDB();
            rs = conn.createStatement().executeQuery(statQuery);
            while (rs.next()) {
                double SUM = rs.getDouble(1);
                int D_FMS = rs.getInt(2);
                stAmountToday.setText(String.valueOf(SUM) + "\t Kgs");
                stNoDeliveredToday.setText(String.valueOf(D_FMS));

                delivered_farmers = D_FMS;
            }
            try (ResultSet rs2 = conn.createStatement().executeQuery(allFarmers)) {
                while (rs2.next()) {
                    int estimate = (rs2.getInt(1)) - delivered_farmers;
                    stRegFarmers.setText(String.valueOf(rs2.getInt(1)));
                    stEstimate.setText(String.valueOf(estimate));

                }
                conn.close();
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @FXML
    private void searchInfo(ActionEvent event) {
        boolean isMember = false;
        tableHistory.getItems().clear();
        tableHistory.getColumns().clear();
        txtAmountDelivered.setText(null);
        txtFID.setText(null);
        txtSN.setText(null);
        txtMDNM.setText(null);
        txtLSTNM.setText(null);
        txtCum.setText(null);

        String query = "SELECT farmer_id,sur_name,mid_name,last_name FROM mg_farmers WHERE farmer_id=?";
        try {
            conn = functions.connectDB();
            pst = conn.prepareStatement(query);
            pst.setString(1, txtSearchF.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                txtFID.setText(String.valueOf(rs.getInt(1)));
                txtSN.setText(rs.getString(2));
                txtMDNM.setText(rs.getString(3));
                txtLSTNM.setText(rs.getString(4));

                isMember = true;
            }
            if (!isMember) {
                TrayNotification tn = new TrayNotification("MEMBERSHIP STATUS", "Sorry,specified member not registered", NotificationType.WARNING);
                tn.setAnimationType(AnimationType.POPUP);
                tn.showAndDismiss(Duration.seconds(3));
            }
            if (isMember) {
                txtCum.setText(String.valueOf(functions.getFarmerCumulativeKilos(Integer.parseInt(txtSearchF.getText()))));
                HistoryData();
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println(e);
        }

        String query1 = "SELECT farmer_id,sur_name,middlename,last_name FROM mg_farmers WHERE farmer_id=?";
        String query2 = "";

    }

    public void ClearTextfields() {
        txtAmountDelivered.setText(null);
        txtFID.setText(null);
        txtSN.setText(null);
        txtMDNM.setText(null);
        txtLSTNM.setText(null);
        txtSearchF.setText(null);
        txtCum.setText(null);

        inputCumKgs.setText(null);
        inputLsname.setText(null);
        inputMidname.setText(null);
        inputSname.setText(null);
        inputStatus.setText(null);
        searchInputTxt.setText(null);
        txtInputFarmerID.setText(null);

    }

    private void HistoryData() {
        try {

            tableHistory.getItems().clear();

            data = FXCollections.observableArrayList();

            conn = functions.connectDB();

            //sql for selecting specific sales history
            String sql = "select * FROM mg_sales WHERE farmer_id='" + txtFID.getText() + "' ORDER BY date_delivered";

            //resultset
            rs = conn.createStatement().executeQuery(sql);

            /**
             * *******************
             * TABLE COLUMN ADDED DYNAMICALLY * ************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //am using non property style for creating dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                col.setPrefWidth(tableHistory.getWidth() / 4);
                tableHistory.getColumns().addAll(col);
                System.out.println("column[" + i + "]");
            }

            //data added to observable list
            while (rs.next()) {
                //iterate row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //iterate column
                    row.add(rs.getString(i));

                }
                System.out.println("row [1] added" + row);

                data.add(row);
            }

            //finally added to the tableview
            tableHistory.setItems(data);

        } catch (SQLException ex) {
            Logger.getLogger(FXclerkController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getAvailableInputs(String query) {
        try {
            conn = functions.connectDB();
            rs = conn.createStatement().executeQuery(query);
            inputs_data = FXCollections.observableArrayList();
            while (rs.next()) {
                inputs_data.add(new inputs(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_remaining.setCellValueFactory(new PropertyValueFactory<>("remaining"));

        tableInputs.setItems(null);
        tableInputs.setItems(inputs_data);

    }

    private String getQuery() {
        String query = "";
        if (rdAll.isSelected()) {
            query = "SELECT * FROM inputs";
        } else if (rdChem.isSelected()) {
            query = "SELECT * FROM inputs WHERE `TYPE`='herbicide'";
        } else if (rdFert.isSelected()) {
            query = "SELECT * FROM inputs WHERE `TYPE`='fertilizer'";
        }
        return query;
    }

    @FXML
    private void checkAll(ActionEvent event) {
        tableInputs.getItems().clear();
        getAvailableInputs(getQuery());

    }

    @FXML
    private void checkChemicals(ActionEvent event) {
        tableInputs.getItems().clear();
        getAvailableInputs(getQuery());

    }

    @FXML
    private void checkFertilizers(ActionEvent event) {
        tableInputs.getItems().clear();
        getAvailableInputs(getQuery());

    }

    private void loansData() {
        loans_data = FXCollections.observableArrayList();
        loans_data.add(new loans(Integer.parseInt(txxChemID.getText()), Integer.parseInt(txtInputFarmerID.getText()), Double.parseDouble(txtQty.getText())));
        //set column value factory
        col_loan_inputid.setCellValueFactory(new PropertyValueFactory<>("inputid"));
        col_loan_farmerid.setCellValueFactory(new PropertyValueFactory<>("farmerid"));
        col_loan_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //tableLoans.setItems(loans_data);
        tableLoans.getItems().addAll(loans_data);

    }

    @FXML
    private void addToList(ActionEvent event) {
        if (functions.isDouble(txtQty)) {
            loansData();
            txxChemID.setText(null);
            txtQty.setText(null);
            txtChemName.setText(null);
            txtType.setText(null);

        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "Input data not allowed,please enter valid amount then continue", ButtonType.OK);
            a.setTitle("Invalid Input Amount");
            a.showAndWait();
        }

    }

    @FXML
    private void commitLoans(ActionEvent event) throws SQLException {
        int success = 0;
        conn = functions.connectDB();
        String insert = "INSERT INTO mg_chemical_loans(id,farmer_id,chemical_id,quanity,date_borrowed) "
                + "VALUES (null,?,?,?,current_date())";

        pst = conn.prepareStatement(insert);
        ObservableList<loans> myList = tableLoans.getItems();
        for (loans object : myList) {
            double quanti = object.getQuantity();
            int farmer = object.getFarmerid();
            int chemd = object.getInputid();

            pst.setInt(1, farmer);
            pst.setInt(2, chemd);
            pst.setDouble(3, quanti);

            success = pst.executeUpdate();

        }
        if (success >= 1) {
            TrayNotification tn = new TrayNotification("STATUS", "Loans recorded successfully", NotificationType.SUCCESS);
            tn.setAnimationType(AnimationType.POPUP);
            tn.showAndDismiss(Duration.seconds(2));

        } else {
            TrayNotification tn = new TrayNotification("STATUS", "Loans recording failed", NotificationType.ERROR);
            tn.setAnimationType(AnimationType.POPUP);
            tn.showAndDismiss(Duration.seconds(2));
        }
        tableLoans.getItems().clear();

    }

    @FXML
    private void removeRow(ActionEvent event) {
        int selectedIndex = tableLoans.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tableLoans.getItems().remove(selectedIndex);
        }
    }

    @FXML
    private void clearTable(ActionEvent event) {
        tableLoans.getItems().clear();
    }

    @FXML
    private void inputsSearchInfo(ActionEvent event) {
        boolean isMember = false;
        inputCumKgs.setText(null);
        inputLsname.setText(null);
        inputMidname.setText(null);
        inputSname.setText(null);
        inputStatus.setText(null);

        String query = "SELECT farmer_id,sur_name,mid_name,last_name FROM mg_farmers WHERE farmer_id=?";
        try {
            conn = functions.connectDB();
            pst = conn.prepareStatement(query);
            pst.setString(1, searchInputTxt.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                txtInputFarmerID.setText(String.valueOf(rs.getInt(1)));
                inputSname.setText(rs.getString(2));
                inputMidname.setText(rs.getString(3));
                inputLsname.setText(rs.getString(4));
                inputCumKgs.setText(String.valueOf(functions.getFarmerCumulativeKilos(Integer.parseInt(txtInputFarmerID.getText()))));
                if (Integer.parseInt(txtInputFarmerID.getText()) > 0) {
                    inputStatus.setText("ACTIVE");
                } else if (Integer.parseInt(txtInputFarmerID.getText()) == 0) {
                    inputStatus.setText("DORMANT");
                } else {
                    inputStatus.setText(null);
                }

                isMember = true;
            }
            if (!isMember) {
                TrayNotification tn = new TrayNotification("MEMBERSHIP STATUS", "Sorry,specified member not registered", NotificationType.WARNING);
                tn.setAnimationType(AnimationType.POPUP);
                tn.showAndDismiss(Duration.seconds(3));
            }
        } catch (Exception e) {
        }

    }

    private void generateSalesReceipt() throws JRException {
        String reportLocale = "src\\pkgreports\\finalReceipt.jrxml";
        //Put parameters in hashmap

        HashMap params = new HashMap();
        params.put("_cum", txtCum.getText());
        params.put("_name", txtSN.getText() + " " + txtMDNM.getText() + " " + txtLSTNM.getText());
        params.put("_id", txtFID.getText());
        params.put("_amount", txtAmountDelivered.getText());
        params.put("_clerk",FXloginController.ActiveUser);
        //Compiling the final report
        JasperReport jr = JasperCompileManager.compileReport(reportLocale);
        JasperPrint jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
        JasperViewer.viewReport(jp, false);

    }
}
