package com.example.booknet.Model;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Data structure to store a user's profile info
 */
public class UserProfile implements Serializable {
    //Attributes
    private String name;
    private String email;
    private String phoneNumber;

    /**
     * Creates a new profile with given attributes
     *
     * @param name        The user's name (not their username)
     * @param email       The user's email address
     * @param phoneNumber The user's phone number
     */
    public UserProfile(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Creates a blank profile
     */
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
