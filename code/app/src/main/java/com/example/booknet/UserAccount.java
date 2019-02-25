package com.example.booknet;

import java.util.ArrayList;

public class UserAccount {
    private String username;
    private String accountPassword;
    private UserProfile profile;
    private ArrayList<Review> reviews;
    private BookLibrary ownedLibrary;
    private BookLibrary requestedBooks;

    //Constructor
    public UserAccount(String username, String accountPassword) {
        this.username = username;
        this.accountPassword = accountPassword;
        this.reviews = new ArrayList<Review>();
        this.ownedLibrary = new BookLibrary();
        this.requestedBooks = new BookLibrary();
    }

    //#region Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public BookLibrary getRequestedBooks() {
        return requestedBooks;
    }

    public void setRequestedBooks(BookLibrary requestedBooks) {
        this.requestedBooks = requestedBooks;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public BookLibrary getOwnedLibrary() {
        return ownedLibrary;
    }

    public void setOwnedLibrary(BookLibrary library) {
        this.ownedLibrary = library;
    }
    //#endregion

    public void addBookToOwned(BookListing book) {
        //todo: implement
    }

    public void removeBookFromOwned(BookListing listing) {
        //todo: implement
    }

    public void addListingToRequested(BookListing listing) {
        //todo: implement
    }

    public void removeListingFromRequested(BookListing listing) {
        //todo: implement
    }


}
