/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;
 
import com.mysql.jdbc.Connection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import utils.DBConnection; 
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.DayOfWeek; 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import utils.HelperMethods; 
//import static Appointments.View_Controllers.AppointmentsController.apptToMod;
import static Appointments.View_Controllers.LoginPageController.cUser;
import static Appointments.View_Controllers.NewAppointmentController.recipientEmail;
import java.util.Properties;
import javafx.scene.control.CheckBox;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static utils.HelperMethods.validEmail;

/**
 * FXML Controller class
 *
 * @author zelal
 */
public class UpdateAppointmentController extends AppointmentsController   implements Initializable {

    @FXML
    private TextField txtCustomer;
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
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<String> cbxStart;
    @FXML
    private ComboBox<String> cbxEnd;
    
    @FXML
    private CheckBox chkBxUpdatedEmail;

    private final ObservableList<String> strType = FXCollections.observableArrayList();
    private final DateTimeFormatter TimeFrmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final ObservableList<String> sTime = FXCollections.observableArrayList();
    private final ObservableList<String> eTime = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbxType;

//    @FXML
//    private static Appointments apptToMod;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        strType.add(0, "New Patient Visit");
        strType.add(1, "Sick Visit");
        strType.add(2, "Annual Checkup");
        strType.add(3, "Telephone Consultation");
        strType.add(4, "Flu Vaccination");

        cbxType.setItems(strType);

        dpDate.setValue(LocalDate.now());
//        DateTimeFormatter TimeFrmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalTime lTime = LocalTime.of(8, 0);
//        final LocalDate parsed = LocalDate.parse(apptToMod.getStart());
        while (!lTime.equals(LocalTime.of(17, 15, 0))) {
            do {
                sTime.add(lTime.format(TimeFrmt));
                eTime.add(lTime.format(TimeFrmt));
                lTime = lTime.plusMinutes(15);
            } while (!lTime.equals(LocalTime.of(17, 15)));
            sTime.remove(sTime.size() - 1);
            eTime.remove(0);
        }

        cbxStart.setItems(sTime);
        cbxEnd.setItems(eTime);
        cbxStart.getSelectionModel().select(LocalTime.of(8, 0).format(TimeFrmt));
        cbxEnd.getSelectionModel().select(LocalTime.of(8, 15).format(TimeFrmt));

        //Populate fields with data
        txtCustomer.setText(apptToMod.getCustName());
        txtTitle.setText(apptToMod.getTitle());
        txtDescription.setText(apptToMod.getDescription());
        txtLocation.setText(apptToMod.getLocation());
        txtContact.setText(apptToMod.getContact());
//        txtType.setText(apptToMod.getType());

        txtURL.setText(apptToMod.getUrl());

        //??
//       dpDate.setValue(LocalDate.parse(apptToMod.getStart(), TimeFrmt));
//        dpDate.setValue(parsed);
    }

    //??
    @FXML
    public boolean appttConflictExists(ZonedDateTime startConf, ZonedDateTime endConf) throws SQLException {

        int userId = HelperMethods.userId(cUser.getUserName());
        int apptId = apptToMod.getApptId();

        String strConflict = "SELECT * FROM appointment \n"
                + "WHERE (? BETWEEN start AND end - INTERVAL 1 MINUTE \n"
                + "OR ? BETWEEN start + INTERVAL 1 MINUTE AND end \n"
                + "OR ? < start AND ? > end) \n"
                + "AND (userId = ? and appointmentId != ? )";

        Connection confCon = DBConnection.startConnection();
        PreparedStatement conSt = confCon.prepareStatement(strConflict);

        conSt.setTimestamp(1, Timestamp.valueOf(startConf.toLocalDateTime()));
        conSt.setTimestamp(2, Timestamp.valueOf(endConf.toLocalDateTime()));
        conSt.setTimestamp(3, Timestamp.valueOf(startConf.toLocalDateTime()));
        conSt.setTimestamp(4, Timestamp.valueOf(endConf.toLocalDateTime()));
        conSt.setInt(5, userId);
        conSt.setInt(6, apptId);

        ResultSet idSet = conSt.executeQuery();
        return idSet.next();
    }

    //  public void updateAppointmentData(Appointments appt) throws SQLException {
    
    
    @FXML
    private void updateAppointmentData (ActionEvent e) throws IOException, SQLException {
        int apId = apptToMod.getApptId();

        /*
        String updAppt = "UPDATE appointment set title = ?, description = ?\n"
                + ",location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?\n"
                + "lastUpdate = Now(), lastUpdateBy = ? where appointmentId = ?";
         */
        String updAppt = "UPDATE appointment set title = ?, description = ?\n"
                + ",location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?\n"
                + ",lastUpdate = Now(), lastUpdateBy = ? where appointmentId = ?";

        Connection updCon = DBConnection.startConnection();
        PreparedStatement upSt = updCon.prepareStatement(updAppt);

        // DateTimeFormatter frmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        DateTimeFormatter frmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        ZoneId znId = ZoneId.systemDefault();

        LocalDate selDate = dpDate.getValue();
//                LocalDate endLd = dpEnd.getValue();

        LocalTime startLt = LocalTime.parse((CharSequence) cbxStart.getSelectionModel().getSelectedItem(), frmt);
        LocalTime endLt = LocalTime.parse((CharSequence) cbxEnd.getSelectionModel().getSelectedItem(), frmt);

        LocalDateTime startDT = LocalDateTime.of(selDate, startLt);
        LocalDateTime endDT = LocalDateTime.of(selDate, endLt);

        ZonedDateTime startUTC = startDT.atZone(znId).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(znId).withZoneSameInstant(ZoneId.of("UTC"));

        Timestamp startTS = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp endTS = Timestamp.valueOf(endUTC.toLocalDateTime());

        //DayOfWeek dpSelected = selDate.getDayOfWeek();//.SATURDAY;
        DayOfWeek dpSelected = DayOfWeek.of(selDate.get(ChronoField.DAY_OF_WEEK));
        int dateValue = dpSelected.getValue();
        //add
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Updating an appointment.");
        a.setHeaderText("");
        a.setContentText("Are you sure you want to update the appointment?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {

            if (startDT.isBefore(LocalDateTime.now())
                    || endDT.isBefore(LocalDateTime.now())
                    || dateValue == 6//Calendar.SATURDAY
                    || dateValue == 7 //Calendar.SUNDAY
                    ) {
                Alert add = new Alert(Alert.AlertType.WARNING);
                add.setTitle("Datetime issue.");
                add.setHeaderText("");
                add.setContentText("Date entered, Start or End time cannot be in the past, or the Date cannot be in the weekends, please correct.");
                Optional<ButtonType> res = add.showAndWait();
            } else {

                if (startDT.isAfter(endDT) || startDT.isEqual(endDT)) {
                    Alert add = new Alert(Alert.AlertType.WARNING);
                    add.setTitle("Datetime issue.");
                    add.setHeaderText("");
                    add.setContentText("End time cannot be less than or equal to Start time, please correct.");
                    Optional<ButtonType> res = add.showAndWait();
                } else {

                    if (txtTitle.getText().trim().isEmpty()
                            || txtDescription.getText().isEmpty()
                            || txtLocation.getText().trim().isEmpty()
                            || txtContact.getText().trim().isEmpty()
                            || cbxType.getSelectionModel().isEmpty()
                            || txtURL.getText().trim().isEmpty()) {

                        Alert warn = new Alert(Alert.AlertType.ERROR);
                        warn.setTitle("Incorrect Entry.");
                        warn.setHeaderText("");
                        warn.setContentText("One or more fields are entered incorrectly, please correct.");
                        Optional<ButtonType> res = warn.showAndWait();

                    } else {

                        //Check for conflicting appointments:
                        if (appttConflictExists(startUTC, endUTC)) {

                            Alert add = new Alert(Alert.AlertType.WARNING);
                            add.setTitle("Appointment Conflict.");
                            add.setHeaderText("");
                            add.setContentText("There is an appointment conflict for the date or time you selected, please choose a different date or start and end times.");
                            Optional<ButtonType> res = add.showAndWait();

                        } else {

                            upSt.setString(1, txtTitle.getText());
                            upSt.setString(2, txtDescription.getText());
                            upSt.setString(3, txtLocation.getText());
                            upSt.setString(4, txtContact.getText());
//                    upSt.setString(5, txtType.getText());
                            upSt.setString(5, cbxType.getValue());
                            upSt.setString(6, txtURL.getText());

                            upSt.setTimestamp(7, startTS);
                            upSt.setTimestamp(8, endTS);

                            upSt.setString(9, cUser.getUserName());
//            upSt.setInt(10, appt.getApptId());
                            upSt.setInt(10, apId);

//            upSt.setInt(10, apptToMod.getApptId());
                            upSt.executeUpdate();
                             
                            
                            sendEmail();

                            //back to Main Window
                            Parent view = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
                            Scene viewScene = new Scene(view);
                            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                            window.setScene(viewScene);
                            window.setTitle("Appointments");

                        }

                    }
                }
            }

        }
    }
    
    
    
    
    
        public  void sendEmail() throws SQLException {
            
            

        int cusId = apptToMod.getCustId(); 

        String rEmail = recipientEmail(cusId);

        if (chkBxUpdatedEmail.isSelected()) {                      //if checkbox is selected, send appointment detail email
            if (validEmail(rEmail)) {                           // check if the email is in a valid email format

                final String username = "AppointmentSchedule2020@gmail.com";
                final String password = "LetUsScheduleYou";

                Properties props = new Properties();
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                try {

                    Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("your_user_name@gmail.com"));
                    message.setFrom(new InternetAddress("AppointmentScheduler2020@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(rEmail));
                    message.setSubject("Updating Your Appointment");
                    message.setText("Dear " + txtCustomer.getText()+" here is your updated appointment detail: \n\n" 
                            +" Type of visit: "+ cbxType.getValue()+ " \n" 
//                            +" Date of visit: "+ dpDate.getValue().format(Date.ofPattern("MM/dd/yyyy")) +" \n" 
                            +" Date of visit: "+ dpDate.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) +" \n" 
                            +" Start time: "+ cbxStart.getValue()+" \n" 
                            +" End time: "+ cbxEnd.getValue() +" \n\n"
                            + "Please bring your insurance card with you.\n\n"
                            + "Looking forward to see you!\n\n"
                            + "Your medical team at ChrisBet Medical Center.");
                    

                    Transport.send(message);

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
    
    
    
  

    @FXML
    private void cancelAppt(ActionEvent ev) throws IOException {

        Parent view = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene viewScene = new Scene(view);
        Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.setTitle("Appointments");
        window.show();
    }

}
