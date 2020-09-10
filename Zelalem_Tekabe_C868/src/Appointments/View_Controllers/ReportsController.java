/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import Appointments.Models.Calendars; 
import Appointments.Models.CustomerLocation;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger; 
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.DBConnection; 

/**
 * FXML Controller class
 *
 * @author zelal
 */
public class ReportsController extends CalendarsController implements Initializable {

    @FXML
    private NumberAxis yNoOfCustomers;
    @FXML
    private CategoryAxis xMonth;
    @FXML
    private BarChart barCrtByMonth;
    @FXML
    private Button rptExit;
    
  ObservableList<Calendars> consultantsList;
    @FXML
    private TableView<Calendars> viewByConsultant;
    @FXML
    private TableColumn<Calendars, String> col_Consultant;
    @FXML
    private TableColumn<Calendars, String> col_Customer;
    @FXML
    private TableColumn<Calendars, String> col_title;
    @FXML
    private TableColumn<Calendars, String> col_start;
    @FXML
    private TableColumn<Calendars, String> col_end;
    
    @FXML
    private Text dtTime; 

  
    ObservableList<CustomerLocation> locationList;
    @FXML
    private TableView<CustomerLocation> viewByLocation;
    @FXML
    private TableColumn<CustomerLocation, String> col_city;
    @FXML
    private TableColumn<CustomerLocation, String> col_country;
    @FXML
    private TableColumn<CustomerLocation, String> col_NumberOfCustomers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            apptByMonth();

            //Displaying consultant report with Lambda expression for efficiency.
            col_Consultant.setCellValueFactory(con -> new SimpleStringProperty(con.getValue().getUserName()));
            col_Customer.setCellValueFactory(con -> new SimpleStringProperty(con.getValue().getCustName()));
            col_title.setCellValueFactory(con -> new SimpleStringProperty(con.getValue().getTitle()));
            col_start.setCellValueFactory(con -> new SimpleStringProperty(con.getValue().getStart()));
            col_end.setCellValueFactory(con -> new SimpleStringProperty(con.getValue().getEnd()));
            
            
            
            //Location report
            col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
            col_country.setCellValueFactory(new PropertyValueFactory<>("country"));
            col_NumberOfCustomers.setCellValueFactory(new PropertyValueFactory<>("numberOfCustomers"));
//            col_city.setCellValueFactory(con -> new SimpleStringProperty(con.getValue().getCity()));
//            col_country.setCellValueFactory(con -> new SimpleStringProperty(con.getValue().getCountry()));
//            col_NumberOfCustomers.setCellValueFactory(con -> new SimpleIntegerProperty(con.getValue().getCustID()).asObject());
            

            displayConsltData();
            displayLocationData();
            
            //date time stamp of the report
            SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("MMM dd, yyyy @ hh:mm:ss aa");
	 
//            dateTimeInGMT.setTimeZone(TimeZone.getTimeZone("");
             dateTimeInGMT.setTimeZone(TimeZone.getDefault());
            String dtNow = dateTimeInGMT.format(new Date());
            dtTime.setText(dtNow);
            

        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void apptByMonth() throws SQLException {
        ObservableList<XYChart.Data<String, Integer>> apttData = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();

        String appMonth = "SELECT count(monthname(start)) as NoOfCustomers, monthname(start) as month  \n"
                + "FROM U06vCi.appointment a\n"
                + "where a.start >= now()\n"
                + "group by monthname(start)  \n"
                + "order by monthname(start);";

        Connection conMon = DBConnection.startConnection();

        PreparedStatement pByMonth = conMon.prepareStatement(appMonth);

        ResultSet rByMonth = pByMonth.executeQuery();

        while (rByMonth.next()) {
            String month = rByMonth.getString("month");
            Integer count = rByMonth.getInt("NoOfCustomers");
            apttData.add(new Data<>(month, count));

        }
        dataSeries.getData().addAll(apttData);
        barCrtByMonth.getData().add(dataSeries);

    }

    public static ObservableList<CustomerLocation> locationReport() {

        ObservableList<CustomerLocation> cal = FXCollections.observableArrayList();
        String strCalData = "select count(c.customerId) as NoOfCustomers , ct.city, cr.country\n"
                + " from customer c\n"
                + " join appointment a\n"
                + " on c.customerId = a.customerId\n"
                + " join address ad\n"
                + " on ad.addressId = c.addressId\n"
                + " join city ct\n"
                + " on ct.cityId = ad.cityId\n"
                + " join country cr\n"
                + " on cr.countryId = ct.countryId\n"
                + " where a.start >= now()\n"
                + " group by ct.city,cr.country";
        Connection wCon = DBConnection.startConnection();
        try {
            PreparedStatement calPr = wCon.prepareStatement(strCalData);

            ResultSet calRS = calPr.executeQuery();
            while (calRS.next()) {

                //Constructor should be recreated
                cal.add(new CustomerLocation(
                        Integer.parseInt(calRS.getString("NoOfCustomers")),
                        calRS.getString("city"),
                        calRS.getString("country")
                )
                );

            }

        } catch (SQLException e) {
            e.toString();

        }
        return cal;
    }

    public void displayConsltData() {
        consultantsList = calendarData();
        viewByConsultant.setItems(consultantsList);
    }

    public void displayLocationData() {
        locationList  = locationReport();
        viewByLocation.setItems(locationList);
    }

    /*
    public static ObservableList<Calendars> apptByConsultant() {

        ObservableList<Calendars> consultants = FXCollections.observableArrayList();
        String strCalData = "SELECT u.userId, u.userName as Consultant, a.appointmentId\n"
                + ", c.customerId, c.customerName, a.title, a.start, a.end\n"
                + " FROM U06vCi.appointment a\n"
                + "Join user u\n"
                + "on a.userId = u.userId\n"
                + "join customer c\n"
                + "on c.customerId = a.customerId";
        Connection conCon = DBConnection.startConnection();
        try {
            PreparedStatement conPr = conCon.prepareStatement(strCalData);

            ResultSet conRS = conPr.executeQuery();
            while (conRS.next()) {

                ZoneId znId = ZoneId.systemDefault();
                Timestamp start = conRS.getTimestamp("start");
                Timestamp end = conRS.getTimestamp("end");
                DateTimeFormatter frmtDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                DateTimeFormatter frmtDateTime = DateTimeFormatter.ofPattern("MM/dd/yyyy - h:mm a");
                LocalDateTime localStart = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
                LocalDateTime localEnd = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());

                //for the week of
                Date wkStartDt = firstDayOfWeek(start);
                LocalDateTime wkLocal = wkStartDt.toInstant().atZone(znId).toLocalDateTime();
                ZonedDateTime wkZone = wkLocal.atZone(znId);
//                ZonedDateTime wkZone = ZonedDateTime.ofInstant(wkLocal.toInstant(ZoneOffset.UTC), znId);
                String weekOf = wkZone.format(frmtDate);
                //start and end appt date times
                ZonedDateTime zonedStart = ZonedDateTime.ofInstant(localStart.toInstant(ZoneOffset.UTC), znId);
                ZonedDateTime zonedEnd = ZonedDateTime.ofInstant(localEnd.toInstant(ZoneOffset.UTC), znId);

                String startDateTime = zonedStart.format(frmtDateTime);
                String endDateTime = zonedEnd.format(frmtDateTime);

                //Constructor should be recreated
                consultants.add(new Calendars(Integer.parseInt(conRS.getString("customerId")),
                        conRS.getString("Consultant"),
                        Integer.parseInt(conRS.getString("weekNo")),
                        weekOf, //weekOf
                         conRS.getString("Month"),
                        conRS.getString("customerName"),
                        conRS.getString("title"),
                        conRS.getString("phone"),
                        conRS.getString("location"),
                        startDateTime,
                        endDateTime
                )
                );

            }

        } catch (SQLException e) {

        }
        return consultants;
    }
    
    
     */
    public void apptByLocation(Stage pStage) throws SQLException {
        String appByCt = " select count(c.customerId) as NoOfCustomers , ct.city\n"
                + " from customer c\n"
                + " join appointment a\n"
                + " on c.customerId = a.customerId\n"
                + " join address ad\n"
                + " on ad.addressId = c.addressId\n"
                + " join city ct\n"
                + " on ct.cityId = ad.cityId\n"
                + " where a.start >= now()\n"
                + " group by ct.city";

        Connection conMon = DBConnection.startConnection();

        PreparedStatement rByCt = conMon.prepareStatement(appByCt);

        ResultSet rsByCt = rByCt.executeQuery();

        ObservableList<PieChart.Data> ctList = FXCollections.observableArrayList(
                new PieChart.Data("It prog", 20),
                new PieChart.Data("Engineering", 30),
                new PieChart.Data("Agriculture", 500)
        );

//        ctList.add(1,20);
        PieChart pChart = new PieChart(ctList);

        Group rt = new Group(pChart);
        Scene scene = new Scene(rt, 600, 400);
        pStage.setTitle("My Chart");
        pStage.setScene(scene);
        pStage.show();

        while (rsByCt.next()) {
            String month = rsByCt.getString("month");
            Integer count = rsByCt.getInt("NoOfCustomers");

//            viewByLocation.setItems(ctList);
        }

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
