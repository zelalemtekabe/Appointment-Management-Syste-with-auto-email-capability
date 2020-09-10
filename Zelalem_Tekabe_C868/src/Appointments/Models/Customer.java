/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author zelal
 */
public class Customer {
    
    private int custID;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String postalCode;
    private String country;
    private String phoneNumber;
    private String email;
    private int addressId;
    
  

    public Customer(int custID, String name, String address1, String address2
            , String city, String postalCode, String country, String phoneNumber, String email, int addressId) {
        this.custID = custID;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.addressId = addressId;
    }

    public Customer(int id, String nm, String p) {
        this.custID = id;
        this.name = nm;
        this.phoneNumber = p;
        
       
    }

    public int getCustID() {
        return custID;
    }

    public String getName() {
        return name;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
   
}
