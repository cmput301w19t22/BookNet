package com.example.booknet;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAccount implements Serializable,Cloneable {
    private String username;
    private String accountPassword;
    private UserProfile profile;
    private ArrayList<Review> reviews;
    private float reviewScore = 5;
    private BookLibrary ownedLibrary;
    private BookLibrary requestedBooks;

    public UserAccount() {
        this.username = "";
        this.accountPassword = "";
        this.profile = new UserProfile();
        this.reviews = new ArrayList<Review>();
        this.ownedLibrary = new BookLibrary();
        this.requestedBooks = new BookLibrary();
    }

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

    /**
     * Gets the user's rating in 1 to 5 stars.
     *
     * @return A float of the user rating
     */
    public float getRatingScore() {
        //todo implement
        return 5;
    }

    public void addBookToOwned(Book book) {
        ownedLibrary.addBookListing(new BookListing(book,this));
    }

    public void addListingToOwned(BookListing book) {
        ownedLibrary.addBookListing(book);
    }

    public void removeBookFromOwned(BookListing listing) {
        ownedLibrary.removeBookListing(listing);
    }

    public void addListingToRequested(BookListing listing) {
        //todo: implement
    }

    public void removeListingFromRequested(BookListing listing) {
        //todo: implement
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "username='" + username + '\'' +
                ", accountPassword='" + accountPassword + '\'' +
                ", profile=" + profile +
                ", reviews=" + reviews.toString() +
                ", reviewScore=" + reviewScore +
                ", ownedLibrary=" + ownedLibrary.toString() +
                ", requestedBooks=" + requestedBooks.toString() +
                '}';
    }

    protected UserAccount clone() {
        UserAccount cloned = new UserAccount(username, accountPassword);
        cloned.setProfile(this.profile);
        cloned.setOwnedLibrary(ownedLibrary.clone());
        cloned.setRequestedBooks(requestedBooks.clone());
        cloned.setReviews((ArrayList<Review>) reviews.clone());
        return cloned;
    }
}
