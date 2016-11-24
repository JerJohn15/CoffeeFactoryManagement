/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgcommon;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

/**
 *
 * @author malware
 */
public class FXloginController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> mbUsertype;
    @FXML
    private Button btnLogIn;
    @FXML
    private Button btnCancel;
    private Functions functions;
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    public static String ActiveUser="";
    @FXML
    private Hyperlink linkReset;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate combobox with initial users
        ObservableList<String> userTypes = FXCollections.observableArrayList("Manager", "Clerk", "Admin");
        mbUsertype.setItems(userTypes);
        mbUsertype.getSelectionModel().selectFirst();
        //Initialise functions
        functions = new Functions();

    }

    private void nextStage(String fxml, String title, boolean resizable) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(resizable);
        stage.show();

    }

    @FXML
    private void logInAction(ActionEvent event) throws IOException {

        Stage current = (Stage) btnLogIn.getScene().getWindow();
        if (!emptyInput()) {
            if (isAdmin()) {
                nextStage(Functions.ADMINISTRATOR, "ADMIN PANEL", true);

            } else if (isUser()) {
                ActiveUser=txtUserName.getText().toUpperCase();
                // selecting new stage according to user type authenticated
                switch (mbUsertype.getValue().toUpperCase()) {
                    case "CLERK":
                        nextStage(Functions.CLERK, "CLERK PANEL", true);
                        break;
                    case "MANAGER":
                        nextStage(Functions.FARMERS, "MANAGER PANEL", true);
                        break;

                }
                // hiding previous log in stage
                current.hide();

            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("USER NOT RECOGNIZED");
                a.setContentText("Please check your log in details and try again \n If error persists,contact admin");
                a.show();

            }
        }

    }

    @FXML
    private void cancelAction(ActionEvent event) {
        TrayNotification tn = new TrayNotification();
        tn.setTitle("My Icon");
        tn.setMessage("The message content comes here");
        tn.setAnimationType(AnimationType.POPUP);
        tn.showAndDismiss(Duration.seconds(2));
    }

    boolean isUser() {
        try {
            String query = "SELECT * FROM mg_users WHERE username=? and password=? and `type`=?";
            conn = functions.connectDB();
            pst = conn.prepareStatement(query);
            pst.setString(1, txtUserName.getText());
            pst.setString(2, txtPassword.getText());
            pst.setString(3, mbUsertype.getValue());
            rs = pst.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    boolean isAdmin() {
        return txtUserName.getText().equalsIgnoreCase("admin") && txtPassword.getText().equalsIgnoreCase("adminPass") && mbUsertype.getValue().equalsIgnoreCase("admin");
    }

    boolean emptyInput() {
        return txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty();
    }

    @FXML
    private void resetPassword(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(Functions.RESET));
        Scene scene = new Scene(root);
        newStage.setTitle("RESET PASSWORD");
        newStage.setResizable(false);
        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.show();
    }

}
