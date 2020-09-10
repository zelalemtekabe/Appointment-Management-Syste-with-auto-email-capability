/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import Appointments.Models.Appointments;
import Appointments.Models.Customer;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utils.DBConnection;
//import java.sql.Date;
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
import java.util.Properties;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import utils.HelperMethods;

/**
 * FXML Controller class
 *
 * @author zelal
 */
public class NewAppointmentController extends LoginPageController implements Initializable {

    @FXML
    private TextField newTitle;
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
    private ComboBox<Customer> cbxCustomers;
    @FXML
    private Button btnCancel;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<String> cbxStart;
    @FXML
    private ComboBox<String> cbxEnd;

    @FXML
    private CheckBox chkBxEmailAppt;

//    private final DateTimeFormatter DateFrmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
//    private final DateTimeFormatter TimeFrmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
//    private final DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //("yyyy-MM-dd'T'HH:mm:ss");
//
//     DateTimeFormatter DTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private Appointments apptToMod;
    private final ObservableList<String> strType = FXCollections.observableArrayList();
    private final ObservableList<String> sTime = FXCollections.observableArrayList();
    private final ObservableList<String> eTime = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbxType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            customersDropDown();

            strType.add(0, "New Patient Visit");
            strType.add(1, "Sick Visit");
            strType.add(2, "Annual Checkup");
            strType.add(3, "Telephone Consultation");
            strType.add(4, "Flu Vaccination");

            cbxType.setItems(strType);

            dpDate.setValue(LocalDate.now());

//            DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
            DateTimeFormatter DateFrmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
            DateTimeFormatter TimeFrmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

            LocalTime lTime = LocalTime.of(8, 0);

            while (!lTime.equals(LocalTime.of(17, 15, 0))) {
                do {
                    sTime.add(lTime.format(TimeFrmt));
                    eTime.add(lTime.format(TimeFrmt));
                    lTime = lTime.plusMinutes(15);

                } while (!lTime.equals(LocalTime.of(17, 15)));

                //16-19
                eTime.remove(0);

                sTime.remove(16, 20);
                eTime.remove(16, 20);
                sTime.remove(sTime.size() - 1);

            }
            cbxStart.setItems(sTime);
            cbxEnd.setItems(eTime);
            cbxStart.getSelectionModel().select(LocalTime.of(8, 0).format(TimeFrmt));
            cbxEnd.getSelectionModel().select(LocalTime.of(8, 15).format(TimeFrmt));

        } catch (SQLException ex) {
            Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void customersDropDown() throws SQLException {

        ObservableList<Customer> custmr = FXCollections.observableArrayList();

        String ctrSel = "SELECT c.customerId, c.customerName, a.Phone\n"
                + "                FROM U06vCi.customer c\n"
                + "                JOIN address a\n"
                + "                 ON c.addressId = a.addressId order by customerName";

        Connection cn;
        cn = DBConnection.startConnection();
        PreparedStatement pr = cn.prepareStatement(ctrSel);
//    
        ResultSet rs = pr.executeQuery();
        try {
            while (rs.next()) {
                custmr.add(new Customer(rs.getInt("customerId"), rs.getString("customerName"), rs.getString("Phone")));

            }
        } catch (SQLException e) {
            e.toString();
        }
        cbxCustomers.setItems(custmr);

        cbxCustomers.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer object) {
                return object.getName();
            }

            @Override
            public Customer fromString(String string) {
                return cbxCustomers.getItems().stream().filter(cst -> cst.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    // public static Appointments newAppointment(Appointments apt) {
    public void addAppointment(ActionEvent e) throws IOException, SQLException {

        insertNewAppt();
        sendEmail();
        Parent view = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene viewScene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.setTitle("Appointments");
        window.show();
    }

    @FXML
    public void insertNewAppt() throws IOException {
        int appttId = newApptId();

        Alert addAlert = new Alert(Alert.AlertType.CONFIRMATION);
        addAlert.setTitle("Adding an appointment.");
        addAlert.setHeaderText("");
        addAlert.setContentText("Are you sure you want to add a new appointment?");
        Optional<ButtonType> result = addAlert.showAndWait();
        if (result.get() == ButtonType.OK) {

            if (cbxCustomers.getSelectionModel().isEmpty()
                    || newTitle.getText().trim().isEmpty()
                    || txtDescription.getText().trim().isEmpty()
                    || txtLocation.getText().trim().isEmpty()
                    || txtContact.getText().trim().isEmpty()
                    || cbxType.getSelectionModel().isEmpty()
                    //                    || txtType.getText().trim().isEmpty()
                    || txtURL.getText().trim().isEmpty()) {
                Alert adAlert = new Alert(Alert.AlertType.ERROR);
                adAlert.setHeaderText("");
                adAlert.setContentText("One or more fields is empty or entered incorrectly, please correct.");
                Optional<ButtonType> res = adAlert.showAndWait();
            } else {

                try {

                    Connection apConn = DBConnection.startConnection();
//                String aptStr = "INSERT INTO appointment VALUES \n"
//                        + "(7,11,?,?,?,?,?,?,?,?,?,Now(),?,Now(),?);";

                    String aptStr = "INSERT INTO appointment (appointmentId,customerId"
                            + ",userId,title,description,location,contact,type,url"
                            + ",start,end,createDate,createdBy,lastUpdate,lastUpdateBy)"
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,Now(),?,Now(),?)";

                    PreparedStatement apPr = apConn.prepareStatement(aptStr);

                    // int ctest = cUser.getUserId();
                    int usrId = HelperMethods.userId(cUser.getUserName());
                    ZoneId defaultZID = ZoneId.systemDefault();

                    // DateTimeFormatter frmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
                    DateTimeFormatter frmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
                    ZoneId znId = ZoneId.systemDefault();

                    LocalDate selectedDate = dpDate.getValue();
//                LocalDate endLd = dpEnd.getValue();

                    LocalTime startLt = LocalTime.parse((CharSequence) cbxStart.getSelectionModel().getSelectedItem(), frmt);
                    LocalTime endLt = LocalTime.parse((CharSequence) cbxEnd.getSelectionModel().getSelectedItem(), frmt);

                    LocalDateTime startDT = LocalDateTime.of(selectedDate, startLt);
                    LocalDateTime endDT = LocalDateTime.of(selectedDate, endLt);

                    ZonedDateTime startUTC = startDT.atZone(znId).withZoneSameInstant(ZoneId.of("UTC"));
                    ZonedDateTime endUTC = endDT.atZone(znId).withZoneSameInstant(ZoneId.of("UTC"));

                    Timestamp startTS = Timestamp.valueOf(startUTC.toLocalDateTime());
                    Timestamp endTS = Timestamp.valueOf(endUTC.toLocalDateTime());

                    apPr.setInt(1, appttId);
//               apPr.setInt(1,1);
                    apPr.setInt(2, cbxCustomers.getValue().getCustID());
                    // apPr.setInt(3, cUser.getUserId());
                    apPr.setInt(3, usrId);
                    apPr.setString(4, newTitle.getText());
                    apPr.setString(5, txtDescription.getText());
                    apPr.setString(6, txtLocation.getText());
                    apPr.setString(7, txtContact.getText());

//                    apPr.setString(8, txtType.getText());
                    apPr.setString(8, cbxType.getValue());

                    apPr.setString(9, txtURL.getText());

                    LocalDate selDate = dpDate.getValue();
//                    selDate.getDayOfWeek();

                    //DayOfWeek dpSelected = selDate.getDayOfWeek();//.SATURDAY;
                    DayOfWeek dpSelected = DayOfWeek.of(selDate.get(ChronoField.DAY_OF_WEEK));
//                    DayOfWeek sun = selDate.getDayOfWeek();//.SUNDAY;
                    int dateValue = dpSelected.getValue();
//                    int su = sun.getValue();

                    if (startDT.isBefore(LocalDateTime.now())
                            || endDT.isBefore(LocalDateTime.now())
                            || dateValue == 6//Calendar.SATURDAY
                            || dateValue == 7 //Calendar.SUNDAY 
                            ) {
                        Alert add = new Alert(Alert.AlertType.WARNING);
                        add.setTitle("Datetime issue.");
                        add.setHeaderText("");
                        add.setContentText("Date entered, Start time or End time cannot be in the past or the Date cannot be in the weekends, please correct.");
                        Optional<ButtonType> resea = add.showAndWait();
                    } else {

                        if (startDT.isAfter(endDT) || startDT.isEqual(endDT)) {
                            Alert add = new Alert(Alert.AlertType.WARNING);
                            add.setTitle("Datetime issue.");
                            add.setHeaderText("");
                            add.setContentText("End time cannot be less than or equal to Start time, please correct.");
                            Optional<ButtonType> res = add.showAndWait();
                        } else {

                            //Check for conflicting appointments:
                            if (appttConflictExists(startUTC, endUTC)) {

                                Alert add = new Alert(Alert.AlertType.WARNING);
                                add.setTitle("Appointment Conflict.");
                                add.setHeaderText("");
                                add.setContentText("There is an appointment conflict for the date or time you selected, please choose a different date and/or start and end times.");
                                Optional<ButtonType> re = add.showAndWait();
                            

                            } else {
                                apPr.setTimestamp(10, startTS);
                                apPr.setTimestamp(11, endTS);
                            }
                        }

                    }

                    apPr.setString(12, cUser.getUserName());
                    apPr.setString(13, cUser.getUserName());

                    apPr.execute();

                } catch (SQLException ex) {
                    ex.toString();

                }

            }
        }
    }

    public static int newApptId() {
        int newApId = 0;
        try {
            String newApptId = "select max(appointmentId + 1) from appointment;";
            Connection apCon = DBConnection.startConnection();
            Statement apSt = apCon.createStatement();

            ResultSet idSet = apSt.executeQuery(newApptId);

            if (idSet.next()) {

                newApId = idSet.getInt(1);
            }

        } catch (SQLException e) {
            e.toString();
        }
        return newApId;
    }

    @FXML
    private void cancelNewAppt(ActionEvent e) throws IOException {

        Parent view = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene viewScene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.setTitle("Appointments");
        window.show();
    }

    @FXML
    public boolean appttConflictExists(ZonedDateTime startConf, ZonedDateTime endConf) throws SQLException {

        int userId = HelperMethods.userId(cUser.getUserName());
//        int apptId = apptToMod.getApptId();

        String strConflict = "SELECT * FROM appointment \n"
                + "WHERE (? BETWEEN start AND end - INTERVAL 1 MINUTE \n"
                + "OR ? BETWEEN start + INTERVAL 1 MINUTE AND end \n"
                + "OR ? < start AND ? > end) \n"
                + "AND (userId = ? )";

        Connection confCon = DBConnection.startConnection();
        PreparedStatement conSt = confCon.prepareStatement(strConflict);

        conSt.setTimestamp(1, Timestamp.valueOf(startConf.toLocalDateTime()));
        conSt.setTimestamp(2, Timestamp.valueOf(endConf.toLocalDateTime()));
        conSt.setTimestamp(3, Timestamp.valueOf(startConf.toLocalDateTime()));
        conSt.setTimestamp(4, Timestamp.valueOf(endConf.toLocalDateTime()));
        conSt.setInt(5, userId);

        ResultSet idSet = conSt.executeQuery();
        return idSet.next();
    }

    public  void sendEmail() throws SQLException {

        int cusId = cbxCustomers.getValue().getCustID();

        String rEmail = recipientEmail(cusId);

        if (chkBxEmailAppt.isSelected()) {                      //if checkbox is selected, send appointment detail email
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
                    message.setSubject("Your Appointment Details");
                    
                     
                    message.setText("Dear " + cbxCustomers.getValue().getName()+" here is your appointment detail: \n\n" 
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

    public static String recipientEmail(int custId) {
        String custEmail = "";

        try {
            String emailQry = "SELECT email FROM address a join customer c on a.addressId = c.addressId where customerId = ?";
            Connection apCon = DBConnection.startConnection();
            PreparedStatement apSt = apCon.prepareStatement(emailQry);
            apSt.setInt(1, custId);
            ResultSet eSet = apSt.executeQuery();

            if (eSet.next()) {

                custEmail = eSet.getString("email");
            }

        } catch (SQLException e) {
            e.toString();
        }

        return custEmail;

    }

//    public  String messageBody() {
//
//        String message = "";
//
//        try {
//            String sql = "SELECT   concat(concat(' Your appointment date and time: ',start) , concat(', Reason of visit: ',type)) as info\n"
//                    + "FROM U06vCi.customer c\n"
//                    + "join appointment a\n"
//                    + "on c.customerId = a.customerId\n"
//                    + "where a.appointmentId = " + newApptId() + " and " + "c.customerId = " + cbxCustomers.getValue().getCustID();
//
//            Connection apCon = DBConnection.startConnection();
//            PreparedStatement pn = apCon.prepareStatement(sql);
//
//            ResultSet r = pn.executeQuery();
//            if (r.next()) {
//                message = r.getString(1);
//            }
//        } catch (SQLException ex) {
//            ex.toString();
//
//        }
//
//        return message;
//    }

}
