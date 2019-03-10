package com.example.booknet;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private String name;
    private String email;
    private String phoneNumber;

    // Constructor
    public UserProfile(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UserProfile() {
        this.name = "";
        this.email = "";
        this.phoneNumber = "";
    }

    //#region Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //endregion
}
