/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import java.util.Locale;

/**
 *
 * @author zelal
 */
public class Zelalem_Tekabe_C195 extends Application {
            Locale locale;
            ResourceBundle rBundle;
    @Override
    public void start(Stage stage) throws Exception {
        try {
            
            
            // Parent root = FXMLLoader.load(getClass().getResource("Appointments/View_Controllers/LoginPage.fxml"));
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Appointments/View_Controllers/LoginPage.fxml"));
            stage.setResizable(false);

            locale = Locale.getDefault();
            rBundle = ResourceBundle.getBundle("Appointments/login", locale);

            Scene scene = new Scene(root);
            //stage.setTitle("Appointment Scheduling System");
            stage.setTitle(this.rBundle.getString("AppTitle"));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {

            Logger.getLogger(Zelalem_Tekabe_C195.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();

    }

}
