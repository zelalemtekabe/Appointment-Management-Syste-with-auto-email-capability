/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Appointments.Models.Appointments;
import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import utils.DBConnection; 
 
 

//import static utils.HelperMethods.getAppointmentsData; 

/**
 * FXML Controller class
 *
 * @author zelal
 */
public class AppointmentsController extends utils.HelperMethods implements Initializable {

    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtURL;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtSearchAppt;


    @FXML
    private TableView<Appointments> apptView;

//    @FXML
//    private TextField txtStart;
//    @FXML
//    private TextField txtEnd;
    @FXML
    private TextField txtDate;
    @FXML
    private TextField txtStart;
    @FXML
    private TextField txtEnd;
    @FXML
    private Button btnDeleteApptt;
    @FXML
    private Button btnAddApptt;
    @FXML
    private Button btnUpdateApptt;
    @FXML
    private TableColumn<Appointments, String> col_CustName;
    @FXML
    private TableColumn<Appointments, String> col_PhoneNo;
    public static Appointments apptToMod;
    ObservableList<Appointments> listAppointments;

    private static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
    private static int appointmentsIndex;
    private final int apptModifyIndex = appointmentsToModifyIndex();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    private final DateTimeFormatter TimeFrmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private Appointments selAppt;

    public static int apptToModIndex() {
        return appointmentsIndex;
    }

    public static Appointments getApptToModify() {
        return apptToMod;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        col_CustName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        col_PhoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        appointmentData();
    }

    @FXML
    public void displayApptDetail() {

        if (apptView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Selection missing.");
            alert.setHeaderText("");
//            alert.setContentText("Select a customoer.");
            alert.setContentText("Please select a customer to see appointment details.");
            Optional<ButtonType> result = alert.showAndWait();

        } else {
            try {

                apptToMod = apptView.getSelectionModel().getSelectedItem();
                appointmentsIndex = getAppointmentsData().indexOf(apptToMod);

                DateTimeFormatter outputDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                DateTimeFormatter outputTime = DateTimeFormatter.ofPattern("h:mm a"); //.ofLocalizedTime(FormatStyle.SHORT);

                ZoneId znId = ZoneId.systemDefault();

                LocalDateTime localDate = apptToMod.getStart().toLocalDateTime();
                LocalDateTime startLDT = apptToMod.getStart().toLocalDateTime();
                LocalDateTime endLDT = apptToMod.getEnd().toLocalDateTime();

                ZonedDateTime zonedDate = ZonedDateTime.ofInstant(localDate.toInstant(ZoneOffset.UTC), znId);
                ZonedDateTime zonedStart = ZonedDateTime.ofInstant(startLDT.toInstant(ZoneOffset.UTC), znId);
                ZonedDateTime zonedEnd = ZonedDateTime.ofInstant(endLDT.toInstant(ZoneOffset.UTC), znId);

                String strDate = zonedDate.format(outputDate);
                String strStart = zonedStart.format(outputTime);
                String strEnd = zonedEnd.format(outputTime);

                txtTitle.setText(apptToMod.getTitle());
                txtURL.setText(apptToMod.getUrl());
                txtDescription.setText(apptToMod.getDescription());
                txtLocation.setText(apptToMod.getLocation());
                txtContact.setText(apptToMod.getContact());
                txtType.setText(apptToMod.getType());
                txtDate.setText(strDate);
                txtStart.setText(strStart);
                txtEnd.setText(strEnd);

            } catch (SQLException ex) {
                ex.toString();
            }
        }
    }

    public void appointmentData() {
        try {
            listAppointments = getAppointmentsData();
            apptView.setItems(listAppointments);

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static int appointmentsToModifyIndex() {
        return appointmentsIndex;
    }

    public static ObservableList<Appointments> getAllAppointments() {
        return allAppointments;
    }

    public void clearApptt() {

        txtTitle.clear();
        txtURL.clear();
        txtDescription.clear();
        txtLocation.clear();
        txtContact.clear();
        txtType.clear();
        txtDate.clear();
        txtStart.clear();
        txtEnd.clear();

    }

//    public void deleteAppointment(Appointments delAppt) {
    public void clearApptt(Appointments delAppt) {
        Appointments delAp = apptView.getSelectionModel().getSelectedItem();
        int apId = delAp.getApptId();

        //String cst = delCust.getName();
        //Optional<ButtonType> result = alert.showAndWait();
        try {
            String dAptt = "DELETE  FROM appointment WHERE appointmentId = ?";
            Connection delCon = DBConnection.startConnection();
            PreparedStatement dSt = delCon.prepareStatement(dAptt);
            // dSt.setInt(1, 0);
            // dSt.setInt(1, cusToMod.getCustID());
            dSt.setInt(1, apId);
            //dSt.setInt(1, delCust.getCustID());
            dSt.executeUpdate();
            appointmentData();
            clearApptt();

        } catch (SQLException ex) {
            ex.toString();
        }
    }

    @FXML
    public void delApptAction(ActionEvent e) {
        if (apptView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Selection missing.");
            alert.setHeaderText("");
//        alert.setContentText(apptToMod.getCustName()+ "Are you sure you want to delete {0} the appointment?");
            alert.setContentText("Please select an appointment to Delete.");
            Optional<ButtonType> result = alert.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
//        alert.setTitle("Deleting an appointment.");
            alert.setTitle("Deleting an appointment.");
            alert.setHeaderText("");
            alert.setContentText("Are you sure you want to delete the appointment?");
//        alert.setContentText("Are you sure you want to delete the appointment of " + "'" + apptToMod.getCustName() + "'" + " ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
//                deleteAppointment(apptToMod);
                    clearApptt(apptToMod);
            }
        }

    }

    public static boolean hasAppttInFifteen() {

        boolean hasApptt = false;
//        LocalDateTime locStart = LocalDateTime.now(Clock.systemUTC());
//        LocalDateTime locEnd = locStart.plusMinutes(15);
//        LocalDateTime locEnd = locStart(Clock.systemUTC());


          DateTimeFormatter frmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
                    ZoneId znId = ZoneId.systemDefault();
                            
                     LocalDateTime startDT = LocalDateTime.now();
                    LocalDateTime endDT = startDT.plusMinutes(15);

                    ZonedDateTime startUTC = startDT.atZone(znId).withZoneSameInstant(ZoneId.of("UTC"));
                    ZonedDateTime endUTC = endDT.atZone(znId).withZoneSameInstant(ZoneId.of("UTC"));

                    Timestamp startTS = Timestamp.valueOf(startUTC.toLocalDateTime());
                    Timestamp endTS = Timestamp.valueOf(endUTC.toLocalDateTime());
                    

        String strAp = "SELECT a.appointmentId, c.customerName, a.start  \n"
                + "FROM appointment a\n"
                + "join customer c\n"
                + "on a.customerId = c.customerId\n"
                //+ "where start between now() and  ADDTIME(NOW(), '00:15:00') ";
                + "where start between " + "'" + startTS + "'" +  "and" +"'" + endTS +"'" ;
        Connection hasCon = DBConnection.startConnection();
        try {

            PreparedStatement hasPs = hasCon.prepareStatement(strAp);
            ResultSet hasRS = hasPs.executeQuery();

            if (hasRS.next()) {
                String cusName = hasRS.getString("customerName");
                Alert Al = new Alert(Alert.AlertType.INFORMATION);
                Al.setTitle("Appointment.");
                Al.setHeaderText("");
//            Al.setContentText("You have an appointment with " + apptToMod.getCustName()+ " within the next 15 minutes." );

                Al.setContentText("You have an appointment with " + cusName + " within the next 15 minutes.");

                Optional<ButtonType> resu = Al.showAndWait();
                hasApptt = true;
            } else {
                hasApptt = false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasApptt;

    }

    @FXML
    public void backToMain(ActionEvent e) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Scene viewScene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.setTitle("Main Window");
        window.show();

    }

    @FXML
    public void newAppointment(ActionEvent e) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("New Appointment.fxml"));
        Scene viewScene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.setTitle("New Appointment");
        window.show();

    }

    @FXML
    public void updateAppointment(ActionEvent e) throws IOException {

        if (apptView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Missing selection.");
            alert.setHeaderText("");
//        alert.setContentText(apptToMod.getCustName()+ "Are you sure you want to delete {0} the appointment?");
            alert.setContentText("Please select an appointment to Update.");
            Optional<ButtonType> result = alert.showAndWait();

        } else {

            Parent view = FXMLLoader.load(getClass().getResource("Update Appointment.fxml"));
            Scene viewScene = new Scene(view);
            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
            window.setScene(viewScene);
            window.setTitle("Update Appointment");
            window.show();
        }

    }
    
    
   public void searchAppt(ActionEvent eve) throws SQLException {
       
        String st = txtSearchAppt.getText().trim();
        
        try {
            listAppointments = getAppointmentsData(st);
            apptView.setItems(listAppointments);
            clearApptt();

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
   
   }
   
    public void clearSearchApt(MouseEvent clear) throws SQLException{
    
         txtSearchAppt.clear();
         clearApptt();
         appointmentData();
    }
    
    
    
    
     

}
