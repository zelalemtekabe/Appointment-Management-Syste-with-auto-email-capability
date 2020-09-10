/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.View_Controllers;

import Appointments.Models.Customer;
import static Appointments.View_Controllers.CustomerController.cusToMod;
import static Appointments.View_Controllers.LoginPageController.cUser;
import com.mysql.jdbc.Connection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import junit.framework.Assert;
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
public class LoginPageControllerTest {
    
      Connection myConn;
    
    public LoginPageControllerTest() {
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

    
@Test
    public void testLoginApp() throws Exception {
                
        String uName = "test";
        String pWord = "test";
        int uId = 0;
        
        cUser.setUserName(uName);
        cUser.setPassWord(pWord);
        cUser.setUserId(uId);        
        
        String pwString = "SELECT password "
                + "FROM user WHERE userName=? AND password=?";
        myConn = DBConnection.startConnection();
        PreparedStatement prSt = myConn.prepareStatement(pwString);
        prSt.setString(1, uName);
        prSt.setString(2, pWord);
        ResultSet rSet = prSt.executeQuery();
        rSet.next();
        
        if(rSet.getString("password").equalsIgnoreCase(pWord)){      
            
        Assert.assertEquals(pWord, uId, uId);
        }
    }     
   
    
    
}
