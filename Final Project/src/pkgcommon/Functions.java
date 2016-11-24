package pkgcommon;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Functions {

    public static final String ADMINISTRATOR = "/admin/AdminConsole.fxml";
    public static final String FARMERS = "/pkgmanager/FXfarmers.fxml";
    public static final String EXPORT = "/pkgmanager/FXexport.fxml";
    public static final String CLERK = "/pkgclerk/FXclerk.fxml";
    public static final String LOGIN = "/pkgcommon/FXlogin.fxml";
    public static final String CHEMICALS = "/pkgmanager/FXchemicals.fxml";
    public static final String EMPLOYEES = "/pkgmanager/FXemployees.fxml";
    public static final String ATTENDANCE = "/pkgmanager/FXattendance.fxml";
    public static final String RESET = "/pkgcommon/FXreset.fxml";
    public static final String EMPPAYMENTS="/pkgmanager/FXpaymentsEmp.fxml";
    public static final String DETAILED="/pkgmanager/FXdetailed.fxml";
    
    public static String ACTIVEUSER = "";

    private PreparedStatement ps;
    private ResultSet rs;
    private Connection conn;
    private Statement st;

    public Functions() {
    }

    public Connection connectDB() {
        try {
            String host = "localhost";
            String username = "root";
            String password = "";
            String database = "mugaga";
            String port = "3306";

            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            System.out.println("Connection Okey");

            return connection;
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public boolean isUser(String u, String p, String s) {
        try {
            conn = connectDB();
            String query = "select * from mg_users where username=? and password=? and user_level=? ";
            ps = conn.prepareStatement(query);
            ps.setString(1, u);
            ps.setString(2, p);
            ps.setString(3, s);
            rs = ps.executeQuery();
            while (rs.next()) {
                ACTIVEUSER = u;
                return true;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public Double getFarmerCumulativeKilos(int farmerID) throws SQLException {
        double kgs = 0.0;
        conn = connectDB();
        ps = conn.prepareStatement("SELECT sum(amount_delivered) FROM mg_sales WHERE farmer_id=?");
        ps.setInt(1, farmerID);
        ResultSet rst = ps.executeQuery();
        while (rst.next()) {
            kgs = rst.getDouble(1);

        }
        conn.close();
        ps.close();
        return kgs;
    }

    public int getFarmerID() throws SQLException {

        String query = "select farmer_id  from mg_farmers ORDER BY farmer_id asc";
        conn = connectDB();
        st = conn.createStatement();
        rs = st.executeQuery(query);
        rs.last();
        return rs.getInt(1) + 1;

    }

    public boolean isFarmerRegistered(int id) {
        try {
            conn = connectDB();
            String query = "select * from mg_farmers where farmer_id=? ";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void NewStage(String fxml, String title, boolean resizable) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setMaximized(true);
        stage.show();
    }

    public void openHelp() {
        File pdfFile = new File("C:\\hai\\123.csv");
        if (pdfFile.exists()) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(pdfFile);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } else {
                System.out.println("Awt Desktop is not supported!");
            }
        } else {
            System.out.println("File is not exists!");
        }

    }

    public boolean isDouble(TextField tf) {
        boolean b = false;
        if (!(tf.getText() == null || tf.getText().length() == 0)) {
            try {
                // Do all the validation you need here such as
                double value = Double.parseDouble(tf.getText());
                if (value > 0.5) {
                    b = true;
                }
            } catch (NumberFormatException e) {
            }
        }
        return b;
    }

    public boolean isValidInteger(TextField tf, int i) {
        boolean b = false;
        if (!(tf.getText() == null || tf.getText().length() == 0)) {
            try {
                int value = Integer.parseInt(tf.getText());
                if ((tf.getText().length() <= i)) {
                    b = true;
                }
            } catch (NumberFormatException e) {
            }
        }
        return b;
    }

    public boolean isMobileField(TextField tf) {
        boolean b = false;
        if (tf.getText().isEmpty()) {
            b = true;
        }
        if (!(tf.getText() == null || tf.getText().length() == 0)) {
            try {
                int value = Integer.parseInt(tf.getText());
                if ((tf.getText().length() <= 10)) {
                    b = true;
                }
            } catch (NumberFormatException e) {
            }
        }
        return b;
    }

}
