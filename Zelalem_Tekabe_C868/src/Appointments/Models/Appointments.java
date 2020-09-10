/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.Models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.sql.Date;

/**
 *
 * @author zelal
 */
public class Appointments {

    private int apptId;
    private int custId;
    private int userId;
    private String custName;
    private String phoneNumber;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private ZonedDateTime start;
    private ZonedDateTime end;
//    private String start;
//    private String end;
//

    public Appointments(int apptId, int custId, int userId, String custName, String phoneNo, String title, String description,
             String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end) {
        this.apptId = apptId;
        this.custId = custId;
        this.userId = userId;
        this.custName = custName;
        this.phoneNumber = phoneNo;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
    }

    public Appointments() {
    }


    public int getApptId() {
        return apptId;
    }

    public int getCustId() {
        return custId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    

}
