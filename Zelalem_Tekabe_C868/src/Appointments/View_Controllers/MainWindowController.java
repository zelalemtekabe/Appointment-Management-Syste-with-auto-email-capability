/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;
 
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.DBConnection; 

/**
 * FXML Controller class
 *
 * @author zelal
 */
public class MainWindowController implements Initializable {

    private static int custToModIndex;
    @FXML
    private Button btnCustomersView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        hasAppttInFifteen() ;
    }

    @FXML
    void userLogData(ActionEvent event) throws IOException {
        ProcessBuilder prBuilder = new ProcessBuilder("Notepad.exe", "login info.txt");
        prBuilder.start();

    }

    @FXML
    public void Customers(ActionEvent e) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        Scene viewScene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.setTitle("Customers Detail");
        window.show();
    }

    @FXML
    public void Appointments(ActionEvent e) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene viewScene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.setTitle("Appointments Detail");
        window.show();
    }

    @FXML
    public void Calendars(ActionEvent e) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("Calendars.fxml"));
        Scene viewSene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewSene);
        window.setTitle("Calendar Detail");
        window.show();
    }

    @FXML
    public void Reports(ActionEvent e) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene viewSene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewSene);
        window.setTitle("Reports");
        window.show();
    }

    @FXML
    public void exit(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exiting the system ...");
        alert.setHeaderText("");
        alert.setContentText("Are you sure you want to sign out?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
//            System.exit(0);            
            Parent view = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            Scene viewScene = new Scene(view);
            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
            window.setScene(viewScene);
            window.setTitle("Appointments Scheduling System");
            window.show();
            DBConnection.closeConnection();

        }
//        else
//        backToMain(e);

    }

    public static int custToModifyIndex() {
        return custToModIndex;
    }

}
