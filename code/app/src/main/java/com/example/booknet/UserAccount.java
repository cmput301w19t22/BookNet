package com.example.booknet;

import java.util.ArrayList;

public class UserAccount {
    private int accountID;
    private String accountPassword;
    private ArrayList<Review> reviews;
    private ArrayList<Book> ownedBooks;
    private ArrayList<Book> requestedBooks;

    // Basic Constructor
    public void UserAccount(int accountID, String accountPassword) {
        this.accountID = accountID;
        this.accountPassword = accountPassword;
        this.reviews = new ArrayList<Review>();
        this.ownedBooks = new ArrayList<Book>();
        this.requestedBooks = new ArrayList<Book>();
    }

    // Basic Getters and Setters
    public int getAccountID() {
        return accountID;
    }
    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
    public String getAccountPassword() {
        return accountPassword;
    }
    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }
    public ArrayList<Review> getReviews() {
        return reviews;
    }
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
    public ArrayList<Book> getOwnedBooks() {
        return ownedBooks;
    }
    public void setOwnedBooks(ArrayList<Book> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }
    public ArrayList<Book> getRequestedBooks() {
        return requestedBooks;
    }
    public void setRequestedBooks(ArrayList<Book> requestedBooks) {
        this.requestedBooks = requestedBooks;
    }
}
