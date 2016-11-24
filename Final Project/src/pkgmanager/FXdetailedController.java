/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgmanager;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import pkgclerk.FXclerkController;
import pkgcommon.Functions;

/**
 * FXML Controller class
 *
 * @author malware
 */
public class FXdetailedController implements Initializable {

    @FXML
    private TableView delivery;
    @FXML
    private TableView inputs;
    @FXML
    private Label farmerid;
    @FXML
    private Label sirname;
    @FXML
    private Label midname;
    @FXML
    private Label lastname;
    @FXML
    private Label mobileno;
    @FXML
    private Label nationalid;
    @FXML
    private Label accountno;
    @FXML
    private Label sacco;
    @FXML
    private Label date;
    @FXML
    private Label cumKgs;
    @FXML
    private Label countDelivery;
    @FXML
    private Label status;
    @FXML
    private Label debt;
    @FXML
    private Label countLoaned;
    private Functions functions;
    private Connection conn;
    private Statement stm;

    private final String queryHF = "SELECT i.input_name ,i.input_type ,l.quanity,l.date_borrowed  FROM mg_chemical_loans l,mg_inputs i "
            + "WHERE i.input_id=l.chemical_id and l.farmer_id='" + FXfarmersController.FARMER_ID + "'";

    private final String queryH = "SELECT amount_delivered as 'KGs Delivered',date_delivered 'DATE' FROM mg_sales "
            + "WHERE farmer_id='" + FXfarmersController.FARMER_ID + "'";

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
        getData();
        tableData(inputs, queryHF);
        tableDataII(delivery, queryH);

    }

    void getData() {
        try {
            conn = functions.connectDB();
            String query1 = "SELECT * FROM mg_farmers WHERE farmer_id='" + FXfarmersController.FARMER_ID + "'";

            String query2 = "select count(farmer_id),sum(mg_sales.amount_delivered) FROM mg_sales "
                    + "WHERE farmer_id='" + FXfarmersController.FARMER_ID + "'";

            String query3 = "select count(l.farmer_id),sum(l.quanity * i.price_per_kg) from mg_chemical_loans l,mg_inputs i "
                    + "WHERE i.input_id=l.chemical_id and l.farmer_id='" + FXfarmersController.FARMER_ID + "'";

            ResultSet rs1 = conn.createStatement().executeQuery(query1);
            while (rs1.next()) {
                farmerid.setText(String.valueOf(rs1.getInt(1)));
                sirname.setText(rs1.getString(2));
                midname.setText(rs1.getString(3));
                lastname.setText(rs1.getString(4));
                mobileno.setText(String.valueOf(rs1.getInt(5)));
                date.setText(rs1.getDate(6).toString());
                nationalid.setText(String.valueOf(rs1.getInt(7)));
                accountno.setText(String.valueOf(rs1.getInt(8)));
                sacco.setText(rs1.getString(9));

            }

            ResultSet rs2 = conn.createStatement().executeQuery(query2);
            while (rs2.next()) {
                countDelivery.setText(String.valueOf(rs2.getInt(1)) + " Times");
                cumKgs.setText(String.valueOf(rs2.getDouble(2)));

                if (rs2.getDouble(2) > 0.0) {
                    status.setText("ACTIVE");
                } else {
                    status.setText("DORMANT");
                }
            }

            ResultSet rs3 = conn.createStatement().executeQuery(query3);
            while (rs3.next()) {
                countLoaned.setText(String.valueOf(rs3.getInt(1)) + " Times");
                debt.setText(String.valueOf(rs3.getDouble(2)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXdetailedController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(FXdetailedController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    void tableData(TableView tableView, String Query) {
        System.out.println("Executing results -" + Query);
        try {
            conn = functions.connectDB();

            ResultSet rst = conn.createStatement().executeQuery(Query);
            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            tableView.getColumns().clear();
            int cols = rst.getMetaData().getColumnCount();

            for (int i = 0; i < cols; i++) {
                final int j = i;
                TableColumn col = new TableColumn(rst.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());

                    }
                });
                col.setPrefWidth(90);
                tableView.getColumns().addAll(col);
            }
            while (rst.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int k = 1; k <= rst.getMetaData().getColumnCount(); k++) {
                    row.add(rst.getString(k));
                }
                data.add(row);

            }
            tableView.setItems(data);

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    void tableDataII(TableView tableView, String Query) {
        try {
            conn = functions.connectDB();

            ResultSet rst = conn.createStatement().executeQuery(Query);
            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            tableView.getColumns().clear();
            int cols = rst.getMetaData().getColumnCount();

            for (int i = 0; i < cols; i++) {
                final int j = i;
                TableColumn col = new TableColumn(rst.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());

                    }
                });
                col.setPrefWidth(192);
                tableView.getColumns().addAll(col);
            }
            while (rst.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int k = 1; k <= rst.getMetaData().getColumnCount(); k++) {
                    row.add(rst.getString(k));
                }
                data.add(row);

            }
            tableView.setItems(data);

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
