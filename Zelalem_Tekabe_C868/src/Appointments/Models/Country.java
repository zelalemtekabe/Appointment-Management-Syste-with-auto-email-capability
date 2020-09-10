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
public class Country {
    
    private int countryId;
    private String Country;

    public Country(int countryId, String Country) {
        this.countryId = countryId;
        this.Country = Country;
    }

    public Country() {
       
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }
    
    
    
}
