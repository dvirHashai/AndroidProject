package com.cambio.finalprojectandroid.model;

/**
 * Created by Chen on 30/08/2017.
 */

public class User {

    private String userId;
    private String userEmail;
    private String userPassword;

    public User() {
    }

    public User(String userId, String userEmail, String userPassword) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
