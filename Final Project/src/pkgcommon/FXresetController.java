/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgcommon;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author malware
 */
public class FXresetController implements Initializable {

    @FXML
    private TextField txtUname;
    @FXML
    private PasswordField txtPword1;
    @FXML
    private PasswordField txtPword2;
    @FXML
    private ComboBox<?> comLevel;
    @FXML
    private Button btnReset;
    private Functions functions;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        functions = new Functions();
    }

    @FXML
    private void resetPassword(ActionEvent event) {
    }

    private boolean isMatching() {
        return txtPword1.getText().equals(txtPword2.getText());
    }
}
