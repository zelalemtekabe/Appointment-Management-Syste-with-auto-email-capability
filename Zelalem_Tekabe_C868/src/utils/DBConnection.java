/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author zelal
 */
public class DBConnection {

    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U06vCi";

    //url
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    private static final String mysqlJDBCDriver = "com.mysql.jdbc.Driver";

    private static Connection conn = null;

    private static final String userName = "U06vCi";
    private static final String passWord = "53688882075";

    public static Connection startConnection() {
        try {
            Class.forName(mysqlJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, userName, passWord);
            System.out.println("Connection successful.");

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }

        return conn;

    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
