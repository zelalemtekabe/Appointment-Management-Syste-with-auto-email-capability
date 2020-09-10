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
public class User {

    private int usrId;
    private String usrName;
    private String psWord;

    public User(int userId, String userName, String passWord) {
        usrId = userId;
        usrName = userName;
        psWord = passWord;
    }

    public User() {

    }

    public int getUserId() {
        return usrId;
    }

    public String getUserName() {
        return usrName;
    }

    public String getPassWord() {
        return psWord;
    }

    public void setUserId(int userId) {
        usrId = userId;
    }

    public void setUserName(String userName) {
        usrName = userName;
    }

    public void setPassWord(String passWord) {
        psWord = passWord;
    }
}
