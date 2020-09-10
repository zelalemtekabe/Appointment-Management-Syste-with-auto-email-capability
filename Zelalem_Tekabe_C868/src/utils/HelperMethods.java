/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Appointments.Models.City;
import Appointments.Models.Address;
import Appointments.Models.Appointments;
import Appointments.Models.Country;
import Appointments.Models.Customer;
import com.mysql.jdbc.Connection;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author zelal
 */
public class HelperMethods {

    private static ZoneId zoneId = ZoneId.systemDefault();

    public static ObservableList<Customer> getCustomersData(String srchItem) throws SQLException {
        Connection myConn = DBConnection.startConnection();
        ObservableList<Customer> listOfCust = FXCollections.observableArrayList();

        //String srchItem ="";
        
        
        String selCus = "select customerId, customerName, address, address2, city, postalCode, country, phone,email,a.addressId\n"
                + "from customer cus\n"
                + "inner join address a \n"
                + "on cus.addressId = a.addressId\n"
                + "inner join city c \n"
                + "on a.cityId = c.cityId\n"
                + "inner join country cr\n"
                + "on c.countryId = cr.countryId\n"
                + "where active = 1\n"
                + "and customerName like '%" + srchItem + "%'\n"
                + "order by customerName;";
        try {
            PreparedStatement prSt = myConn.prepareStatement(selCus);
            ResultSet rSet = prSt.executeQuery();

            while (rSet.next()) {
                listOfCust.add(new Customer(Integer.parseInt(rSet.getString("customerId")),
                        rSet.getString("customerName"),
                        rSet.getString("address"),
                        rSet.getString("address2"),
                        rSet.getString("city"),
                        rSet.getString("postalCode"),
                        rSet.getString("country"),
                        rSet.getString("phone"),
                        rSet.getString("email"),
                        Integer.parseInt(rSet.getString("addressId"))
                ));

            }
        } catch (SQLException e) {
            e.toString();
        }

        return listOfCust;

    }

    public static ObservableList<Appointments> getAppointmentsData() throws SQLException {
        Connection apCon = DBConnection.startConnection();
        ObservableList<Appointments> listOfApt = FXCollections.observableArrayList();

        String selAppt = "SELECT a.appointmentId, c.customerId, a.userId, \n"
                + "                    c.customerName,ad.phone, a.title, a.description, a.location,\n"
                + "                    a.contact, a.type, a.url , a.start, a.end \n"
                + "                    FROM U06vCi.appointment a\n"
                + "                    inner join customer c\n"
                + "                     on a.customerId = c.customerId \n"
                + "                     inner join address ad\n"
                + "                     on ad.addressId = c.addressId\n"
                + "                     where a.start >= now() order by a.start asc ";

        try {
            PreparedStatement prSt = apCon.prepareStatement(selAppt);
            ResultSet rSt = prSt.executeQuery();

            while (rSt.next()) {

                ZoneId znId = ZoneId.systemDefault();

                Timestamp start = rSt.getTimestamp("start"); //rSt.getDate("start");
                Timestamp end = rSt.getTimestamp("end");
                //Date end = rSt.getDate("end");            

                LocalDateTime localStart = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
                LocalDateTime localEnd = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());

                ZonedDateTime zonedStart = localStart.atZone(znId);
                ZonedDateTime zonedEnd = localEnd.atZone(znId);

                listOfApt.add(new Appointments(Integer.parseInt(rSt.getString("appointmentId")),
                        Integer.parseInt(rSt.getString("customerId")),
                        Integer.parseInt(rSt.getString("userId")),
                        rSt.getString("customerName"),
                        rSt.getString("phone"),
                        rSt.getString("title"),
                        rSt.getString("description"),
                        rSt.getString("location"),
                        rSt.getString("contact"),
                        rSt.getString("type"),
                        rSt.getString("url"),
                        zonedStart,
                        zonedEnd
                ));

            }

        } catch (SQLException ex) {
            ex.toString();
        }
        return listOfApt;

    }
    
     public static ObservableList<Appointments> getAppointmentsData(String custName) throws SQLException {
        Connection apCon = DBConnection.startConnection();
        ObservableList<Appointments> listOfApt = FXCollections.observableArrayList();

        String selAppt = "SELECT a.appointmentId, c.customerId, a.userId, \n"
                + "                    c.customerName,ad.phone, a.title, a.description, a.location,\n"
                + "                    a.contact, a.type, a.url , a.start, a.end \n"
                + "                    FROM U06vCi.appointment a\n"
                + "                    inner join customer c\n"
                + "                     on a.customerId = c.customerId \n"
                + "                     inner join address ad\n"
                + "                     on ad.addressId = c.addressId\n"
                + "                     where a.start >= now() and customerName like '%"+ custName + "%' order by a.start asc ;";

        try {
            PreparedStatement prSt = apCon.prepareStatement(selAppt);
            ResultSet rSt = prSt.executeQuery();

            while (rSt.next()) {

                ZoneId znId = ZoneId.systemDefault();

                Timestamp start = rSt.getTimestamp("start"); //rSt.getDate("start");
                Timestamp end = rSt.getTimestamp("end");
                //Date end = rSt.getDate("end");            

                LocalDateTime localStart = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
                LocalDateTime localEnd = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());

                ZonedDateTime zonedStart = localStart.atZone(znId);
                ZonedDateTime zonedEnd = localEnd.atZone(znId);

                listOfApt.add(new Appointments(Integer.parseInt(rSt.getString("appointmentId")),
                        Integer.parseInt(rSt.getString("customerId")),
                        Integer.parseInt(rSt.getString("userId")),
                        rSt.getString("customerName"),
                        rSt.getString("phone"),
                        rSt.getString("title"),
                        rSt.getString("description"),
                        rSt.getString("location"),
                        rSt.getString("contact"),
                        rSt.getString("type"),
                        rSt.getString("url"),
                        zonedStart,
                        zonedEnd
                ));

            }

        } catch (SQLException ex) {
            ex.toString();
        }
        return listOfApt;

    }

    public static int cityId(String ctyId) throws SQLException {

        String c = "select cityId from city where city=?";

        int cId = -1;

        try {

            Connection con = DBConnection.startConnection();
            PreparedStatement s = con.prepareStatement(c);
            s.setString(1, ctyId);

            ResultSet r = s.executeQuery();

            if (r.next()) {
                cId = r.getInt("cityId");
            }
        } catch (SQLException e) {
            e.toString();
                   
        }
        return cId;

    }

    public static int addressId(String addId) throws SQLException {
        String a = "SELECT addressId \n"
                + "FROM U06vCi.address where address =?";
        int aId = -1;
        try {
            Connection co = DBConnection.startConnection();
            PreparedStatement st = co.prepareStatement(a);
            //st.setString(2, addId);

            st.setString(1, addId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {

                aId = rs.getInt("addressId");
            }
        } catch (SQLException e) {
            e.toString();
                   
        }
        return aId;

    }

    public static City ctyById(int ctId) {
        //String cty = "select cityId, city, countryId from city where city = ?";
        String cty = "select * from city where cityId = ?";
        //City ctyById = new City();
        City cById = new City();
//        String cById = "";

        try {
            Connection myCon = DBConnection.startConnection();
            PreparedStatement pSt = myCon.prepareStatement(cty);//check here
            pSt.setInt(1, ctId);
            ResultSet rSet = pSt.executeQuery();

            if (rSet.next()) {
                cById.setCityId(rSet.getInt("cityId"));
                cById.setCity(rSet.getString("city"));
//                cById = rSet.getString("city");
                cById.setCountryId(rSet.getInt("countryId"));

            }
        } catch (SQLException e) {
            e.toString();
        }
        return cById;
    }

    public static Address addrById(int addrId) {
        String add = "select address from address where addressId=?";
        //City ctyById = new City();
        Address aId = new Address();
        try {

            Connection myCon = DBConnection.startConnection();
            PreparedStatement pSt = myCon.prepareStatement(add);
            pSt.setInt(1, addrId);

            ResultSet rt = pSt.executeQuery();

            if (rt.next()) {
//                aId.setAddressId(rt.getInt(rt.getInt("addressId")));
                aId.setAddress(rt.getString("address"));
//                aId.setAddress2(rt.getString("address2"));
//                aId.setCityId(rt.getInt("cityId"));
//                aId.setPostalCode(rt.getString("postalCode"));
//                aId.setPhoneNumber(rt.getString("phone"));

            }
        } catch (SQLException e) {
            e.toString();
        }
        return aId;
    }

    public static int countryId(String ctryId) {

        String ctr = "select countryId, country from country where country=?";

        int cy = -1;

        try {

            Connection cConn = DBConnection.startConnection();
            PreparedStatement sr = cConn.prepareStatement(ctr);
            sr.setString(1, ctryId);

            ResultSet re = sr.executeQuery();

            if (re.next()) {
                cy = re.getInt("cityId");
            }
        } catch (SQLException e) {
            e.toString();
        }
        return cy;

    }

    public static Country cntryById(int crId) {
        String ctry = "SELECT * FROM country WHERE countryId=?";
        //City ctyById = new City();
        Country ry = new Country();

        try {
            Connection myCo = DBConnection.startConnection();
            PreparedStatement cPs = myCo.prepareStatement(ctry);
            cPs.setInt(1, crId);

            //cPs.setInt(0, crId);
            ResultSet rSet = cPs.executeQuery();

            if (rSet.next()) {
                ry.setCountryId(rSet.getInt("countryId"));
                ry.setCountry(rSet.getString("country"));

            }
        } catch (SQLException e) {
            e.toString();
        }
        return ry;
    }

    public static int cntryByCityId(int ctyId) {
        int ctryId = -1;
        String ctry = "SELECT cr.countryId \n" //    , cr.countryId, c.city,cr.country\n"
                + "FROM U06vCi.city c\n"
                + "JOIN country cr\n"
                + "ON c.countryId = cr.countryId\n"
                + "where c.cityId = ?";
//        //City ctyById = new City();
        Country ry = new Country();

        try {
            Connection my = DBConnection.startConnection();
            PreparedStatement cp = my.prepareStatement(ctry);
            cp.setInt(1, ctyId);

            //cPs.setInt(0, crId);
            ResultSet rSet = cp.executeQuery();

            if (rSet.next()) {
                ctryId = ry.getCountryId();

//                ry.setCountryId(rSet.getInt("countryId"));
//                ry.setCountry(rSet.getString("country"));
            }
        } catch (SQLException e) {
            e.toString();
        }
        return ctryId;
    }

    public static boolean appointmentExists(int cstId) {
        // Customer delCu = custTable.getSelectionModel().getSelectedItem();
        //int crId = cst.getCustID();
        boolean exists = false;
        try {
            String apt = "SELECT * FROM U06vCi.appointment\n"
                    + "where customerId = ? and end >= now();";
            Connection apCon = DBConnection.startConnection();
            PreparedStatement pn = apCon.prepareStatement(apt);
            //pn.setInt(1, crId);
            pn.setInt(1, cstId);
            ResultSet r = pn.executeQuery();
            //pn.executeQuery();
            if (r.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.toString();
        }

        return exists;
    }

    public static int userId(String userName) throws SQLException {
        String a = "select userId from user where userName = ?";
        int uId = -1;
        try {
            Connection uCo = DBConnection.startConnection();
            PreparedStatement st = uCo.prepareStatement(a);
            //st.setString(2, addId);

            st.setString(1, userName);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {

                uId = rs.getInt("userId");
            }
        } catch (SQLException e) {
            e.toString();
        }
        return uId;

    }

    public static boolean isNotValidNumber(String number) {
        try {
            number = number.replaceAll(" ", "");
            number = number.replaceAll("-", "");
            BigInteger n = new BigInteger(number);

            return false;

        } catch (NumberFormatException ex) {

            return true;
        }

    }
    
    public static boolean validEmail(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    } 
  
    
    

}
