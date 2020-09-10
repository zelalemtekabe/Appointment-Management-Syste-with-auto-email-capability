/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import Appointments.Models.User;
import com.mysql.jdbc.Connection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import java.util.Locale;

import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.DBConnection; 
import java.io.IOException; 
import java.time.ZonedDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
//import static Appointments.View_Controllers.AppointmentsController.hasAppttInFifteen;

/**
 *
 * @author zelal
 */
public class LoginPageController extends AppointmentsController implements Initializable {

    Locale locale;
    ResourceBundle rBundle;
    private final String WRITETO = "login info.txt";
    Logger logger = Logger.getLogger(WRITETO);

    @FXML
    private Text welcome;
    @FXML
    private Text usrNameLabel;
    @FXML
    private Text pwLabel;

    @FXML
    private TextField usrNameTxtBx;
    @FXML
    private PasswordField pwTxtBx;
    @FXML
    private Label loginMsgBox;

    @FXML
    private Button btnLogin_Submit;
    @FXML
    private Button btnLogin_Cancel;

    Connection myConn;
    User newUser = new User();
    public static User cUser = new User();

    @FXML
    void userInfo(ActionEvent e) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        locale = Locale.getDefault();
        rBundle = ResourceBundle.getBundle("Appointments/login", locale);
        welcome.setText(rBundle.getString("AppTitle"));
        usrNameLabel.setText(rBundle.getString("Username"));
        pwLabel.setText(rBundle.getString("Password"));
        btnLogin_Submit.setText(rBundle.getString("Login"));

//        btnLogin_Cancel.setText(rBundle.getString("Cancel"));
        // loginMsgBox.setText(rBundle.getString("Error"));
    }

    public String loginUser() {
        return usrNameLabel.getText();
    }

    @FXML
    public void cancelLogin(ActionEvent e) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().backgroundProperty();
        alert.setTitle(this.rBundle.getString("ConfirmText"));
        alert.setHeaderText(this.rBundle.getString("ConfirmTitle"));

//alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    public void loginApp(ActionEvent e) throws IOException, SQLException {
//        String uName = usrNameTxtBx.getText();
//        String pWord = pwTxtBx.getText();

        String uName = usrNameTxtBx.getText();
        String pWord = pwTxtBx.getText();
        int uId = 0;

        cUser.setUserName(uName);
        cUser.setPassWord(pWord);
        cUser.setUserId(uId);

        String pwString = "SELECT password FROM user WHERE userName=? AND password=?";
        myConn = DBConnection.startConnection();
        PreparedStatement prSt = myConn.prepareStatement(pwString);

        //prSt = myConn.prepareStatement(pwString);
//           prSt = DBConnection.startConnection().prepareStatement(pwString);
//        prSt = DBConnection.startConnection().prepareStatement(pwString);
        prSt.setString(1, usrNameTxtBx.getText());
        prSt.setString(2, pwTxtBx.getText());

        //prSt.setString(0, pwString);
        ResultSet rSet = prSt.executeQuery();

        if (rSet.next()) {

            Parent view = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
            Scene viewScene = new Scene(view);
            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
            window.setScene(viewScene);
            window.setTitle("Main Window");
            window.show();

            hasAppttInFifteen();

            getLoginInfo(true);
//              logger.log(Level.INFO, cUser.getUserName() + " successfully logged in on "+ZonedDateTime.now());

        } else {

//            
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.initModality(Modality.NONE);
//        alert.setTitle("Exiting the system ...");
//        alert.setContentText("Username and/password error.");
//        Optional<ButtonType> result = alert.showAndWait();
            usrNameTxtBx.clear();
            pwTxtBx.clear();
            DBConnection.closeConnection();

            loginMsgBox.setText(rBundle.getString("Error"));
            getLoginInfo(false);
//            logger.log(Level.WARNING, cUser.getUserName() + " failed to log in on "+ZonedDateTime.now());

        }

//        Parent view = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
//        Scene viewScene = new Scene(view);
//        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
//        window.setScene(viewScene);
//        window.setTitle("Main Window");
//        window.show();
//        String userName = loginSubmit.getText();
//        String passWord = loginPW.getText();
//    
    }

//    public static void getLoginInfo(String user, boolean status) throws IOException{
    public void getLoginInfo(boolean status) {

        try {

            FileHandler fh = new FileHandler("login info.txt", true);
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            logger.addHandler(fh);
            logger.setLevel(Level.INFO);  
            if (status == true) {
                logger.log(Level.INFO,"User " + cUser.getUserName() + " successfully loggedin on " + ZonedDateTime.now());
            } else {
                logger.log(Level.WARNING, "User " + cUser.getUserName() + " failed to login on " + ZonedDateTime.now());
            } 
        } catch (Exception ex) { 
            ex.toString();

        }
    }
}
