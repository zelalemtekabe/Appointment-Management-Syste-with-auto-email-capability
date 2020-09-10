/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Appointments.Models;

import java.time.ZonedDateTime;

/**
 *
 * @author zelal
 */
public class Calendars {

    private int custId;
    private String userName;
    private int weekNo;
    private String weekOf;
    private String month;
    private String custName;
    private String title;
    private String phone;
    private String location;
    private String start;
    private String end;

    public Calendars(int custId,String userName, int weekNo, String weekOf, String month, String custName, String title, String phone, String location, String start, String end) {
        this.custId = custId;
        this.userName = userName;
        this.weekNo = weekNo;
        this.weekOf = weekOf;
        this.month = month;
        this.custName = custName;
        this.title = title;
        this.phone = phone;
        this.location = location;
        this.start = start;
        this.end = end;
    }



    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public int getCustId() {
        return custId;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public String getWeekOf() {
        return weekOf;
    }

    public String getMonth() {
        return month;
    }

    public String getCustName() {
        return custName;
    }

    public String getTitle() {
        return title;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public void setWeekOf(String weekOf) {
        this.weekOf = weekOf;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }


















}
