package com.example.booknet;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data structure representing an account on the app.
 */
public class UserAccount extends AppCompatActivity implements Serializable, Cloneable {
    //Attributes
    private String username;
    private String accountPassword;
    private UserProfile profile;
    private ArrayList<Review> reviews;
    private float reviewScore = 5;
    private BookLibrary ownedLibrary;
    private BookLibrary requestedBooks;

    /**
     * Constructor to make an empty account
     */
    public UserAccount() {
        this.username = "";
        this.accountPassword = "";
        this.profile = new UserProfile();
        this.reviews = new ArrayList<Review>();
        this.ownedLibrary = new BookLibrary();
        this.requestedBooks = new BookLibrary();
    }

    /**
     * Constructor to create a new account
     *
     * @param username        The new account's username
     * @param accountPassword The new accounts password
     */
    public UserAccount(String username, String accountPassword) {
        this.username = username;
        this.accountPassword = accountPassword;
        this.profile = new UserProfile();
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

    /**
     * Adds a book to this user's owned library and creates a listing for it.
     *
     * @param book The book to add
     */
    public void addBookToOwned(Book book) {
        ownedLibrary.addBookListing(new BookListing(book, this));
    }

    /**
     * Adds a listing to this user's owned library.
     *
     * @param listing The listing to add
     */
    public void addListingToOwned(BookListing listing) {
        ownedLibrary.addBookListing(listing);
    }

    /**
     * Removes a listing from this user's owned library
     *
     * @param listing The listing to remove
     */
    public void removeBookFromOwned(BookListing listing) {
        ownedLibrary.removeBookListing(listing);
    }

    /**
     * Adds a listing to this user's requested library
     *
     * @param listing The listing to add
     */
    public void addListingToRequested(BookListing listing) {
        //todo: implement
    }

    /**
     * Removes a listing fromm this user's requested library
     *
     * @param listing The listing to remove
     */
    public void removeListingFromRequested(BookListing listing) {
        //todo: implement
    }

    /**
     * Creates a string containing this UserAccount's attributes
     *
     * @return
     */
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

    /**
     * Creates a copy of this UserAccount
     *
     * @return Returns the copy of this UserAccount
     */
    protected UserAccount clone() {
        UserAccount cloned = new UserAccount(username, accountPassword);
        cloned.setProfile(this.profile);
        cloned.setOwnedLibrary(ownedLibrary.clone());
        cloned.setRequestedBooks(requestedBooks.clone());
        cloned.setReviews((ArrayList<Review>) reviews.clone());
        return cloned;
    }
}
