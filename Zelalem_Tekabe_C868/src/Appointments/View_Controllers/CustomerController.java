/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.DBConnection;
//import static utils.HelperMethods.appointmentExists;
//import static utils.HelperMethods.getCustomersData;

/**
 *
 * @author zelal
 */
public class CustomerController extends utils.HelperMethods implements Initializable {

    @FXML
    private TableView<Customer> custTable;

//    @FXML
//    private TableColumn<Customer, String> col_id;
    @FXML
    private TableColumn<Customer, String> col_name;
    @FXML
    private TableColumn<Customer, String> col_addOne;
    @FXML
    private TableColumn<Customer, String> col_addTwo;
    @FXML
    private TableColumn<Customer, String> col_city;
    @FXML
    private TableColumn<Customer, String> col_postalCode;
    @FXML
    private TableColumn<Customer, String> col_country;
    @FXML
    private TableColumn<Customer, Integer> col_phoneNr;
    @FXML
    private TableColumn<Customer, Integer> col_email;
    @FXML
    private Button addCustomer;
    public static Customer cusToMod;

    @FXML
    private TextField txtSearchItem;

    ObservableList<Customer> listCustomers;
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    private static int custModifyIndex;

    // private final int custIndex = custToModIndex();
    private int cusId;

    public static int custToModIndex() {
        return custModifyIndex;
    }

    public static Customer getCustToModify() {
        return cusToMod;
    }

//    int index = -1;
//    Connection conn = null;
//    ResultSet rSet = null;
//    PreparedStatement pSt = null;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Lambda expression to populate customer table for code and performance efficiency purposes.
        col_name.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        col_addOne.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAddress1()));
        col_addTwo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAddress2()));
        col_city.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCity()));
        col_postalCode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPostalCode()));
        col_country.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCountry()));

        customerDt();
        col_phoneNr.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        

        /*
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_addOne.setCellValueFactory(new PropertyValueFactory<>("address1"));
        col_addTwo.setCellValueFactory(new PropertyValueFactory<>("address2"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        col_country.setCellValueFactory(new PropertyValueFactory<>("country"));
        col_phoneNr.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
         */
    }

//    public void customerData() {
    public void customerDt() {
        try {
            listCustomers = getCustomersData("");
            custTable.setItems(listCustomers);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
//    public void addCustomer(ActionEvent e) throws IOException {
    public void customerDt(ActionEvent e) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("New Customer.fxml"));
        Scene viewScene = new Scene(view);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.setTitle("Adding a New Customer");
        window.show();

    }

    public void searchData(ActionEvent eve) throws SQLException {
       
        String s = txtSearchItem.getText().trim();
        
//        searchCustomer(s);

        try {
            listCustomers = getCustomersData(s);
            custTable.setItems(listCustomers);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void clearSearch(MouseEvent clear){
    
         txtSearchItem.clear();
         customerDt();
    }

    @FXML
    public void updateCustomer(ActionEvent even) throws IOException {

        if (custTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.initModality(Modality.NONE);
            alert.setTitle("Selection missing.");
            alert.setHeaderText("");
            alert.setContentText("Please select a customer to update.");
            Optional<ButtonType> result = alert.showAndWait();

        } else {

//            try {
            cusToMod = custTable.getSelectionModel().getSelectedItem();  //issues here 
            custModifyIndex = getAllCustomers().indexOf(cusToMod);

            Parent view = FXMLLoader.load(getClass().getResource("Update Customer.fxml"));
            Scene viewScene = new Scene(view);
            Stage window = (Stage) ((Node) even.getSource()).getScene().getWindow();
            window.setScene(viewScene);
            window.setTitle("Updating Customer Information");
            window.show();
            //Customer c = getAllCustomers().get(custIndex);
            // cusId = getAllCustomers().get(custIndex).getCustID();
            // txtName.setText(c.getName());
            //txtAddOne.setText(c.getAddress1());
//            } catch (IOException ex) {
//                ex.toString();
//
//            }
        }
    }

    public void deleteCustomer(Customer delCust) {
        Customer delCu = custTable.getSelectionModel().getSelectedItem();
        int cId = delCu.getCustID();

        //String cst = delCust.getName();
        if (appointmentExists(cId)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.initModality(Modality.NONE);

            alert.setTitle("An appointment Exists.");
            alert.setHeaderText("");//          
            alert.setContentText("The customer has an upcoming appointment, please delete the appointment first.");
            alert.showAndWait();//delCust.getName()

            //Optional<ButtonType> result = alert.showAndWait();
        } else {

            try {
                
                  Connection dCon = DBConnection.startConnection();
                  Statement stmt = dCon.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_UPDATABLE);
                   dCon.setAutoCommit(false);
                

                   
                   
                String delApt = "DELETE appointment.* FROM appointment where customerId = " +cId;  
                
//                   String delAdrs = "DELETE a.* FROM address a join customer c "
//                        + "on a.addressId = c.addressId where c.customerId = " +cId; 
                String delCst =  "  DELETE customer.* FROM customer "
                        + "JOIN address "
                        + "ON customer.addressId = address.addressId "
                        + "WHERE customerId = " +cId;               
                stmt.addBatch(delApt);  
//                stmt.addBatch(delAdrs);  
                stmt.addBatch(delCst);
                
                stmt.executeBatch();
                dCon.commit();
                
//                PreparedStatement dApt = dCon.prepareStatement(delApt);
//////                dApt.setInt(1, cId);
//                  dApt.executeUpdate();
//                
//                  
             
//                 PreparedStatement dAdr = dCon.prepareStatement(delAdrs);
////                   dAdr.setInt(2, cId);
//                 dAdr.executeUpdate();
                        
//                String delCst  = "DELETE FROM appointment where customerId = " + cId ; 
                        
                       
//                 PreparedStatement dCst = dCon.prepareStatement(delCst);  
////                dCst.setInt(3, cId);
                //dSt.setInt(1, delCust.getCustID());
              
                 
//                  dCst.executeUpdate();
                customerDt();

            } catch (SQLException ex) {
            }
        }

    }

    @FXML
    public void delAction(ActionEvent e) {

        if (custTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.initModality(Modality.NONE);
            alert.setTitle("Selection missing.");
            alert.setHeaderText("");
//        alert.setContentText(apptToMod.getCustName()+ "Are you sure you want to delete {0} the appointment?");
            alert.setContentText("Please select a customer to Delete.");
            Optional<ButtonType> result = alert.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.initModality(Modality.NONE);
            alert.setTitle("Deleting a customer.");
            alert.setHeaderText("");
            alert.setContentText("This will also delete previous appointment records of the customer, are you sure you want to delete the customer?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deleteCustomer(cusToMod);
            }

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

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

//    public String searchCustomer(String nm) {
//        String res = "";
//
//        String searchSQL = "SELECT customerName FROM U06vCi.customer\n"
//                + "WHERE customerName like '%" + nm + "%'";
//
//        try {
//            Connection don = DBConnection.startConnection();
//            PreparedStatement dSt = don.prepareStatement(searchSQL);
//
//            ResultSet rs = dSt.executeQuery(); // dSt.executeUpdate();
//
//            if (rs.next()) {
////            res = rs.toString();
//                res = rs.getString("customerName");
//            }
//
//        } catch (SQLException exx) {
//            exx.toString();
//        }
//
//        return res;
//
//    }

}
