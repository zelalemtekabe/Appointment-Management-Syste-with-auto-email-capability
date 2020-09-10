/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import Appointments.Models.City;
import Appointments.Models.Country;
import Appointments.Models.User;
import Appointments.View_Controllers.CustomerController;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
import static Appointments.View_Controllers.LoginPageController.cUser;

/**
 * FXML Controller class
 *
 * @author zelal
 */
public class NewCustomerController extends utils.HelperMethods implements Initializable {

    @FXML
    private ComboBox<Country> cbxCountry;
    @FXML
    private ComboBox<City> cbxCity;
    @FXML
    private Button btnSaveCustomer;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddOne;
    @FXML
    private TextField txtAddTwo;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private TextField txtPhoneNr;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnCancelCustomer;

    private TextField txtCity;

    private User userName;
    @FXML
    private TextField txtCountry;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            //  userName = 
            // String usrName = loginUser();
            //userName.getUserName();
//            countryDropDown();
            cityDropDown();
        } catch (SQLException ex) {
            Logger.getLogger(NewCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private void countryDropDown() throws SQLException {
//
//        ObservableList<Country> ctry = FXCollections.observableArrayList();
//
//        String ctrSel = "SELECT   countryId,  country\n"
//                + "FROM  country cr\n"
//                + "ORDER BY country;";
//
//        Connection con;
//        con = DBConnection.startConnection();
//        PreparedStatement p = con.prepareStatement(ctrSel);
////    
//        ResultSet rs = p.executeQuery();
//        try {
//            while (rs.next()) {
//                ctry.add(new Country(rs.getInt("countryId"), rs.getString("country")));
//
//            }
//        } catch (SQLException e) {
//        }
//        cbxCountry.setItems(ctry);
//
//        cbxCountry.setConverter(new StringConverter<Country>() {
//            @Override
//            public String toString(Country object) {
//                return object.getCountry();
//            }
//
//            @Override
//            public Country fromString(String string) {
//                return cbxCountry.getItems().stream().filter(myCtry -> myCtry.getCountry().equals(string)).findFirst().orElse(null);
//            }
//
//        });
//
//    }
    private void cityDropDown() throws SQLException {

        ObservableList<City> cty = FXCollections.observableArrayList();

        String ctrSel = "SELECT cityId,city, countryId FROM  city ORDER BY city;";

        Connection cn;
        cn = DBConnection.startConnection();
        PreparedStatement pr = cn.prepareStatement(ctrSel);
//    
        ResultSet rs = pr.executeQuery();
        try {
            while (rs.next()) {
                cty.add(new City(rs.getInt("cityId"), rs.getString("city"), rs.getInt("countryId")));

            }
        } catch (SQLException e) {
            e.toString();
        }
        cbxCity.setItems(cty);

        cbxCity.setConverter(new StringConverter<City>() {
            @Override
            public String toString(City object) {
                return object.getCity();
            }

            @Override
            public City fromString(String string) {
                return cbxCity.getItems().stream().filter(myCity -> myCity.getCity().equals(string)).findFirst().orElse(null);
            }
        });
    }

    public int newCustId() {
        int newId = 0;
        try {
            String newCustId = "SELECT max(customerId + 1) FROM U06vCi.customer;";
            Connection idCon = DBConnection.startConnection();
            Statement pSt = idCon.createStatement();

            ResultSet idSet = pSt.executeQuery(newCustId);

            if (idSet.next()) {

                newId = idSet.getInt(1);
            }

        } catch (SQLException e) {
            e.toString();
        }
        return newId;
    }

    public int newAdreId() {
        int newAid = 0;
        try {
            String newCustId = "SELECT max(addressId + 1) FROM U06vCi.address;";
            Connection id = DBConnection.startConnection();
            Statement p = id.createStatement();

            ResultSet set = p.executeQuery(newCustId);

            if (set.next()) {

                newAid = set.getInt(1);
            }

        } catch (SQLException e) {
            e.toString();
        }
        return newAid;
    }

    public String countryByCity(int cId) throws SQLException {
        String strCountry = "";
        String ctry = "SELECT cr.country \n"
                + "FROM U06vCi.city c\n"
                + "JOIN country cr\n"
                + "ON c.countryId = cr.countryId\n"
                + "where c.cityId = ?";
//          City c = new City();
        Country c = new Country();
//          cbxCity.getValue()
        Connection my = DBConnection.startConnection();
        PreparedStatement cp = my.prepareStatement(ctry);

        cp.setInt(1, cId);
        ResultSet r = cp.executeQuery();
        if (r.next()) {
            strCountry = r.getString("country");
        }
        return strCountry;
    }

    public void getCountryFromCity(ActionEvent ev) throws SQLException {

        int myCityId = cbxCity.getValue().getCityId();
        String myCountry = countryByCity(myCityId);
        txtCountry.setText(myCountry);

    }

    // public static User cUser = new User();
    @FXML
    public void addCustomers(ActionEvent e) throws IOException, SQLException {
        int custId = newCustId();
        int newAdrsId = -1;

        Alert adAlert = new Alert(Alert.AlertType.CONFIRMATION);
        adAlert.setTitle("Adding a customer.");
        adAlert.setHeaderText("");
        adAlert.setContentText("Are you sure you want to add a new customer?");
        Optional<ButtonType> result = adAlert.showAndWait();
        if (result.get() == ButtonType.OK) {

            if (txtName.getText().isEmpty()
                    || txtAddOne.getText().trim().isEmpty()
                    //                    || txtAddTwo.getText().trim().isEmpty()
                    || cbxCity.getSelectionModel().isEmpty()
                    || txtPostalCode.getText().trim().isEmpty()
                    //                    || cbxCountry.getValue().toString().isEmpty()
                    || txtCountry.getText().trim().isEmpty()
                    //                    ||  txtPhoneNr.getText().trim().isEmpty() 
                    || isNotValidNumber(txtPhoneNr.getText())) {
                Alert adAler = new Alert(Alert.AlertType.ERROR);
                adAler.setTitle("Warning.");
                adAler.setHeaderText("");
                adAler.setContentText("One or more fields are entered incorrectly, please correct.");
                Optional<ButtonType> res = adAler.showAndWait();

            } else {

                try {
                    //Customer Address
                    Connection adrConn = DBConnection.startConnection();
                    PreparedStatement insAdr = adrConn.prepareStatement("INSERT INTO address (address, address2, cityId, postalCode"
                            + ", phone, email, createDate, createdBy, lastUpdate, lastUpdateBy) "
                            + "VALUES (?, ?, ?, ?, ?, ?, Now(), ?, Now(),?)", Statement.RETURN_GENERATED_KEYS);
//            String insAddress = ("INSERT INTO address (address, address2, cityId, postalCode"
//                    + ", phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
//                    + "VALUES (?, ?, ?, ?, ?, Now(), ?, Now(),?)",Statement.RETURN_GENERATED_KEYS);

                    insAdr.setString(1, txtAddOne.getText());
                    insAdr.setString(2, txtAddTwo.getText());
//            insAdr.setString(3, txtCity.getText());
                    insAdr.setInt(3, cbxCity.getValue().getCityId());
                    // userName.getUserName();
                    insAdr.setString(4, txtPostalCode.getText());

                    insAdr.setString(5, txtPhoneNr.getText());
                    insAdr.setString(6, txtEmail.getText());

                    insAdr.setString(7, cUser.getUserName());
                    insAdr.setString(8, cUser.getUserName());
                    // insAdr.executeUpdate();   
//            insAdr.setString(6, "ene");
//            insAdr.setString(7, "ene");
                    insAdr.execute();
                    ResultSet rSet = insAdr.getGeneratedKeys();
                    if (rSet.next()) {
                        newAdrsId = rSet.getInt(1);
                    }

                    //Customer data
                    String insCust = "INSERT INTO customer (customerId"
                            + ", customerName, addressId, active, createDate"
                            + ", createdBy, lastUpdate, lastUpdateBy) "
                            + "VALUES (?, ?, ?, 1, NOW(), ?, NOW(), ?);";

                    Connection inCon = DBConnection.startConnection();
                    PreparedStatement insPr = inCon.prepareStatement(insCust);
                    insPr.setInt(1, custId);
                    insPr.setString(2, txtName.getText());
                    insPr.setInt(3, newAdrsId);
                    // insPr.setInt(4, 1);
                    insPr.setString(4, cUser.getUserName());
                    insPr.setString(5, cUser.getUserName());
                    insPr.executeUpdate();

                    //back to Main Window
                    Parent view = FXMLLoader.load(getClass().getResource("Customer.fxml"));
                    Scene viewScene = new Scene(view);
                    Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    window.setScene(viewScene);
                    window.setTitle("Customers Detail");
                    // customerData();
                    window.show();

                } catch (SQLException ex) {
                    ex.toString();
                }
            }
        }

    }

    @FXML
    public void cancelCustomer(ActionEvent e) {

        try {

            Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
            cancelAlert.setTitle("Cancelling ...");
            cancelAlert.setHeaderText("");
            cancelAlert.setContentText("Are you sure you want to cancel adding a customer?");
            Optional<ButtonType> result = cancelAlert.showAndWait();
            if (result.get() == ButtonType.OK) {

                //back to Main Window
                Parent view = FXMLLoader.load(getClass().getResource("Customer.fxml"));
                Scene viewScene = new Scene(view);
                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                window.setScene(viewScene);
                window.setTitle("Customers Detail");
            }
        } catch (IOException ex) {
            Logger.getLogger(NewCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    @FXML
//    public void backToMain(ActionEvent e) throws IOException {
//        Parent view = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
//        Scene viewScene = new Scene(view);
//        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
//        window.setScene(viewScene);
//        window.setTitle("Main Window");
//        window.show();
//
//    }
}
