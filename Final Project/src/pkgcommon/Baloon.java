package pkgcommon;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.net.URL;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javax.swing.ImageIcon;

/**
 *
 * @author Dan Malware
 */
public class Baloon {

    private static URL imageUrl;
    private static TrayIcon trayIcon;
    private static SystemTray systemTray;

    public Baloon() {
        final PopupMenu popupMenu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem aboutItem = new MenuItem("About");

        exitItem.addActionListener((ActionEvent evt) -> {
            //Platform.exit();
            System.exit(0);
        });

        aboutItem.addActionListener((ActionEvent ActionEvent) -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Factory Management System");
            a.setContentText("Mugaga Factories Management System"
                    + "\n Version 2.0"
                    + "\n Developed by SpuxTechnologies"
                    + "\n Email:info@spuxtechnologies.com");
            a.show();
        });
        popupMenu.add(aboutItem);
        popupMenu.addSeparator();
        popupMenu.add(exitItem);

        systemTray = SystemTray.getSystemTray();
        imageUrl = getClass().getResource("/pkgimages/icon.png");
        trayIcon = new TrayIcon(createImage(imageUrl, "Tray Icon"));
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Management System");
        trayIcon.setPopupMenu(popupMenu);

        trayIcon.addActionListener((ActionEvent ActionEvent) -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Factory Management System");
            a.setContentText("Mugaga Factories Management System"
                    + "\n Version 2.0"
                    + "\n Developed by SpuxTechnologies"
                    + "\n Email:info@spuxtechnologies.com");
            a.showAndWait();
        });
        try {
            systemTray.add(trayIcon);
        } catch (AWTException awte) {
            System.err.println(awte.toString());
        }

    }

    private static Image createImage(URL path, String desc) {
        if (imageUrl == null) {
            System.err.println("Resource not found" + path);
            return null;
        } else {
            return new ImageIcon(imageUrl, desc).getImage();
        }
    }

}
