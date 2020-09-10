/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import Appointments.Models.Customer;
import static Appointments.View_Controllers.CustomerController.cusToMod;
import com.mysql.jdbc.Connection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.DBConnection;

/**
 *
 * @author zelal
 */
public class CustomerControllerTest {
    
      private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    
    ObservableList<Customer> listCustomers;
     @FXML
    private TableView<Customer> custTable;
      private static int custModifyIndex;
    
    public CustomerControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of custToModIndex method, of class CustomerController.
     */
    @Test
    public void testCustToModIndex() {
        System.out.println("custToModIndex");
        int expResult =  custModifyIndex;
        int result = CustomerController.custToModIndex();
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getCustToModify method, of class CustomerController.
     */
    @Test
    public void testGetCustToModify() {
        System.out.println("getCustToModify");
        Customer expResult = cusToMod;
        Customer result = CustomerController.getCustToModify();
        assertEquals(expResult, result); 
    }

  
    /**
     * Test of deleteCustomer method, of class CustomerController.
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteCustomer() throws SQLException {
        
        
        int cId = -1;
                      String dCus = "DELETE  customer.* , address.* \n"
                        + "from customer  \n"
                        + "inner join address   on customer.addressId = address.addressId\n"
                        + "where customer.customerId = ?;";

                Connection dCon = DBConnection.startConnection();
                PreparedStatement dSt = dCon.prepareStatement(dCus); 
                dSt.setInt(1, cId); 
                dSt.executeUpdate();

    }

   

    /**
     * Test of getAllCustomers method, of class CustomerController.
     */
    @Test
    public void testGetAllCustomers() {
        System.out.println("getAllCustomers");
        ObservableList<Customer> expResult = allCustomers;
        ObservableList<Customer> result = CustomerController.getAllCustomers();
        assertEquals(expResult, result); 
    }
}
