/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBConnection;

import Appointments.Models.Calendars; 
/**
 * FXML Controller class
 *
 * @author zelal
 */
public class CalendarsController implements Initializable {

    @FXML
    private TableView<Calendars> tblWeekly;
    @FXML
    private TableView<Calendars> tblMonthly;
    @FXML
    private TableColumn<Calendars, Integer> col_WeekNo;
    @FXML
    private TableColumn<Calendars, ZonedDateTime> col_WeekOf;
    @FXML
    private TableColumn<Calendars, String> col_CustName;
    @FXML
    private TableColumn<Calendars, String> col_ApptTitle;
    @FXML
    private TableColumn<Calendars, String> col_PhoneNo;
    @FXML
    private TableColumn<Calendars, String> col_Location;
    @FXML
    private TableColumn<Calendars, ZonedDateTime> col_Start;
    @FXML
    private TableColumn<Calendars, ZonedDateTime> col_End;

    ObservableList<Calendars> weeklyList;
    ObservableList<Calendars> monthlyList;
    @FXML
    private TableColumn<Calendars, String> col_mMonth;
    @FXML
    private TableColumn<Calendars, String> col_mCustomerName;
    @FXML
    private TableColumn<Calendars, String> col_mPhoneNumber;
    @FXML
    private TableColumn<Calendars, String> col_mApptTitle;
    @FXML
    private TableColumn<Calendars, String> col_mLocation;
    @FXML
    private TableColumn<Calendars, String> col_mStart;
    @FXML
    private TableColumn<Calendars, String> col_mEnd;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
                     
        //Weekly view        
        col_WeekNo.setCellValueFactory(new PropertyValueFactory<>("weekNo"));
        col_WeekOf.setCellValueFactory(new PropertyValueFactory<>("weekOf"));
        col_CustName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        col_ApptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_PhoneNo.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_Location.setCellValueFactory(new PropertyValueFactory<>("location"));
        col_Start.setCellValueFactory(new PropertyValueFactory<>("start"));
        col_End.setCellValueFactory(new PropertyValueFactory<>("end"));

        //Monthly view
        col_mMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        col_mCustomerName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        col_mApptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_mPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_mLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        col_mStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        col_mEnd.setCellValueFactory(new PropertyValueFactory<>("end"));

        displayCalendarData();
    }

    public void displayCalendarData() {
          
        weeklyList = calendarData();
        tblWeekly.setItems(weeklyList);

        monthlyList = calendarData();
        tblMonthly.setItems(monthlyList);

    }

    public  static Date firstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    public static ObservableList<Calendars> calendarData() {

        ObservableList<Calendars> cal = FXCollections.observableArrayList();
        String strCalData = "SELECT "
                + "c.customerId\n"
                + ",u.userName \n"
                + ",week(a.start) as WeekNo \n"
                +",subdate(a.start,weekday(a.start)) WeekOf\n"
                +",monthname(start) as  Month\n"                
                + ",c.customerName\n"
                + ",a.title\n"
                + ",ad.phone\n"
                + ",a.location \n"
                + ",a.start \n"
                + ",a.end \n"
                + "FROM U06vCi.appointment a\n"
                + "join customer c\n"
                + "on a.customerId = c.customerId\n"
                + "join address ad\n"
                + "on ad.addressId = c.addressId\n"
                +"join user u on u.userId = a.userId \n"
                + "where a.start >= now()\n"
                + "order by a.start";
        Connection wCon = DBConnection.startConnection();
        try {
            PreparedStatement calPr = wCon.prepareStatement(strCalData);

            ResultSet calRS = calPr.executeQuery();
            while (calRS.next()) {

                ZoneId znId = ZoneId.systemDefault();
                Timestamp start = calRS.getTimestamp("start");
                Timestamp end = calRS.getTimestamp("end");
                DateTimeFormatter frmtDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                DateTimeFormatter frmtDateTime = DateTimeFormatter.ofPattern("MM/dd/yyyy - h:mm a");
                LocalDateTime localStart = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
                LocalDateTime localEnd = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());

                //for the week of
                Date wkStartDt = firstDayOfWeek(start);
                LocalDateTime wkLocal = wkStartDt.toInstant().atZone(znId).toLocalDateTime();
                //ZonedDateTime wkZone = wkLocal.atZone(znId);
                ZonedDateTime wkZone = ZonedDateTime.ofInstant(wkLocal.toInstant(ZoneOffset.UTC), znId);
                String weekOf = wkZone.format(frmtDate);

                //start and end appt date times
                ZonedDateTime zonedStart = ZonedDateTime.ofInstant(localStart.toInstant(ZoneOffset.UTC), znId);
                ZonedDateTime zonedEnd = ZonedDateTime.ofInstant(localEnd.toInstant(ZoneOffset.UTC), znId);

                String startDateTime = zonedStart.format(frmtDateTime);
                String endDateTime = zonedEnd.format(frmtDateTime);

                //Constructor should be recreated
                cal.add(new Calendars(Integer.parseInt(calRS.getString("customerId")),
                        calRS.getString("userName"),
                        Integer.parseInt(calRS.getString("weekNo")),
                         weekOf, //weekOf
                        calRS.getString("Month"),    
                        calRS.getString("customerName"),
                        calRS.getString("title"),
                        calRS.getString("phone"),
                        calRS.getString("location"),
                        startDateTime,
                        endDateTime
                )
                );

            }

        } catch (SQLException e) {
            e.toString();

        }
        return cal;
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

}
