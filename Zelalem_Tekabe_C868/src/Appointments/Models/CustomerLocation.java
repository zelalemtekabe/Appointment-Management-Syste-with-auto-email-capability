/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.Models;

/**
 *
 * @author zelal
 */
public class CustomerLocation {
    
    private int numberOfCustomers;
    private String city;
    private String country;

    public CustomerLocation(int numberOfCustomers, String city, String country) {
        this.numberOfCustomers = numberOfCustomers;
        this.city = city;
        this.country = country;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    
}
