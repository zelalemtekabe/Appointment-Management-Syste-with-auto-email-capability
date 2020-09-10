/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import Appointments.Models.Address;
import Appointments.Models.City;
import Appointments.Models.Country;
import Appointments.Models.Customer;
import Appointments.View_Controllers.NewCustomerController;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import utils.HelperMethods;
import static utils.HelperMethods.isNotValidNumber;
//import static Appointments.View_Controllers.CustomerController.custToModIndex;
//import static Appointments.View_Controllers.CustomerController.cusToMod;
import static Appointments.View_Controllers.LoginPageController.cUser;

/**
 * FXML Controller class
 *
 * @author zelal
 */
public class UpdateCustomerController extends CustomerController implements Initializable {

    @FXML
    private Button btnUpdateCustomer;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddTwo;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private TextField txtPhoneNr;
    @FXML
    private TextField txtEmailUpdate;
    
    @FXML
    private ComboBox<Country> cbxCountry;
    @FXML
    private Button btnCancelCustomer;
    @FXML
    private TextField txtAddOne;
    @FXML
    private ComboBox<City> cbxCity;

    private final int custIndex = custToModIndex();
    private Address ad = HelperMethods.addrById(cusToMod.getAddressId());
    private City cty = HelperMethods.ctyById(ad.getCityId());
    private Country ctry = HelperMethods.cntryById(cty.getCountryId());

    // private City cy = HelperMethods.cityById(ad.getCityId());
    // private Country ctry = 
    private int cusId;
    @FXML
    private TextField txtCountry;

    /**
     * Initializes the controller class.
     *
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try { 
             
            
//          txtName.setText(c.getName());
            txtName.setText(cusToMod.getName());
            txtAddOne.setText(cusToMod.getAddress1());
            txtAddTwo.setText(cusToMod.getAddress2());
            txtPostalCode.setText(cusToMod.getPostalCode());
            txtPhoneNr.setText(cusToMod.getPhoneNumber());
            
            txtEmailUpdate.setText(cusToMod.getEmail());
           
//            cbxCity.getValue().getCityId(); 
            txtCountry.setText(cusToMod.getCountry()); 
//            cbxCity.getValue().getCity(); 
//            countryToString();
            cityToString();
//            countryDropDown();
            cityDropDown(); 
            //Logger.getLogger(UpdateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            //String toString = ex.toString();
        } catch (SQLException ex) {
            Logger.getLogger(UpdateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
  

//    private void countryDropDown() throws SQLException {
//
//        ObservableList<Country> cntry = FXCollections.observableArrayList();
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
//                cntry.add(new Country(rs.getInt("countryId"), rs.getString("country")));
//
//            }
//        } catch (SQLException e) {
//        }
//        cbxCountry.setItems(cntry);
//
//        cbxCountry.setConverter(new StringConverter<Country>() {
//            @Override
//            public String toString(Country object) {
//                return object.getCountry();
//            }
//
//            @Override
//            public Country fromString(String str) {
//                return cbxCountry.getItems().stream().filter(myCtry -> myCtry.getCountry().equals(str)).findFirst().orElse(null);
//
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
                // return cbxCity.getValue();
            }
        });
    }

    public void cityToString() {
        cbxCity.setConverter(new StringConverter<City>() {
            @Override
            public String toString(City object) {
                return object.getCity();
            }

            @Override
            public City fromString(String string) {
                //return cbxCity.getItems().stream().filter(myCity -> myCity.getCity().equals(string)).findFirst().orElse(null);
                return cbxCity.getValue();
            }
        });

    }

//    public void countryToString() {
//        cbxCountry.setConverter(new StringConverter<Country>() {
//            @Override
//            public String toString(Country obj) {
//                return obj.getCountry();
//            }
//
//            @Override
//            public Country fromString(String string) {
//                //return cbxCountry.getItems().stream().filter(myCtry -> myCtry.getCountry().equals(string)).findFirst().orElse(null);
//                return cbxCountry.getValue();
//            }
//        });
//
//    }
    public void updateCustomerData(Customer updCustomer) throws SQLException {
//        String custUpdate = "UPDATE customer SET customerName=?,"
//                + "lastUpdate = NOW(), lastUpdateBy = ? WHERE customerId = ?";
        String custUpdate = "UPDATE customer c, address a \n"
                + "SET c.customerName = ?\n"
                + ",c.lastUpdate = Now()\n"
                + ",c.lastUpdateBy = ?\n"
                + ",a.address=?\n"
                + ",a.address2=?\n"
                + ",a.cityId=?\n"
                + ",a.postalCode=?\n"
                + ",a.phone=?\n"
                + ",a.email=?\n"
                + ",a.lastUpdate=Now()\n"
                + ",a.lastUpdateBy=?\n"
                + "WHERE c.customerId = ? \n"
                + "and c.addressId = a.addressId";

        try {
            Connection uCon = DBConnection.startConnection();
            PreparedStatement uSt = uCon.prepareStatement(custUpdate);
            //uSt.setString(1, updCustomer.getName());
//            uSt.setString(1,"ene");
//            uSt.setString(2, "ene erase");
//            uSt.setInt(3, updCustomer.getCustID());

            uSt.setString(1, txtName.getText());
            uSt.setString(2, cUser.getUserName());
            uSt.setString(3, txtAddOne.getText());
            uSt.setString(4, txtAddTwo.getText());
            uSt.setInt(5, cbxCity.getValue().getCityId());
            //cbxCity.setValue(cty)
            uSt.setString(6, txtPostalCode.getText());
            uSt.setString(7, txtPhoneNr.getText());            
            uSt.setString(8, txtEmailUpdate.getText());
            uSt.setString(9, cUser.getUserName());
            uSt.setInt(10, updCustomer.getCustID());

            uSt.executeUpdate();

            // String dd = cUser.getUserName();
        } catch (SQLException ex) {
            ex.toString();
        }

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

    @FXML
    public void saveUpdates(ActionEvent e) throws IOException {

        Alert Al = new Alert(Alert.AlertType.CONFIRMATION);
        Al.setTitle("Updating a customer.");
        Al.setHeaderText("");
        Al.setContentText("Are you sure you want to update the customer record?");
        Optional<ButtonType> resu = Al.showAndWait();
        if (resu.get() == ButtonType.OK) {

            if (txtName.getText().trim().isEmpty()
                    || txtAddOne.getText().isEmpty()
                    //                    || cbxCity.getValue().toString().isEmpty()
                    || cbxCity.getSelectionModel().isEmpty()
                    || txtPostalCode.getText().trim().isEmpty()
                    || txtCountry.getText().trim().isEmpty()
//                    || txtPhoneNr.getText().trim().isEmpty()) {
                    || isNotValidNumber(txtPhoneNr.getText())
                    
                    ){

                Alert warn = new Alert(Alert.AlertType.ERROR);
                warn.setTitle("Incorrect Entry.");
                warn.setHeaderText("");
                warn.setContentText("One or more fields are entered incorrectly, please correct.");
                Optional<ButtonType> res = warn.showAndWait();

            } else {

                try {
                    updateCustomerData(cusToMod);
                } catch (SQLException ex) {
                    ex.toString();
                }

                //back to Main Window
                Parent view = FXMLLoader.load(getClass().getResource("Customer.fxml"));
                Scene viewScene = new Scene(view);
                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                window.setScene(viewScene);
                window.setTitle("Customers Detail");
            }
        }

    }

    @FXML
    public void cancelUpdate(ActionEvent e) {

        try {

            Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
            cancelAlert.setTitle("Cancelling ...");
            cancelAlert.setHeaderText("");
            cancelAlert.setContentText("Are you sure you want to cancel updating the customer record?");
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

}
