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
    private UserProfile profile;
    private ArrayList<Review> reviews;
    private float reviewScore = 5;
    private BookLibrary ownedLibrary;
    private BookLibrary requestedBooks;
    private String phoneNumber;

    /**
     * Constructor to make an empty account
     */
    public UserAccount() {
        this.username = "";
        this.profile = new UserProfile();
        this.reviews = new ArrayList<Review>();
        this.ownedLibrary = new BookLibrary();
        this.requestedBooks = new BookLibrary();
    }

    /**
     * Constructor to create a new account
     *
     * @param username The new account's username
     */
    public UserAccount(String username) {
        this.username = username;
        this.profile = new UserProfile();
        this.reviews = new ArrayList<Review>();
        this.ownedLibrary = new BookLibrary();
        this.requestedBooks = new BookLibrary();
    }

    //#region Getters and Setters

    /**
     * Gets the username of the UserAccount
     *
     * @return String of the username of this UserAccount
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the UserAccount to the passed in value.
     *
     * @param username String - what the username of UserAccount is going to be set to.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Deprecated
    public String getAccountPassword() {
        return null;
    }

    @Deprecated
    public void setAccountPassword(String accountPassword) {

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

    public void setProfileEmail(String email) {
        profile.setEmail(email);
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
     * Calculated as the average of the user's review ratings.
     * If no reviews exist the function returns 0.
     *
     * @return A float of the user rating
     */
    public float getRatingScore() {
        if (reviews.size() > 0) {
            float val = 0;
            for (Review review : reviews) {
                val += review.getScore();
            }
            return val / reviews.size();
        } else {
            return 0;
        }
    }

    /**
     * Adds a book to this user's owned library and creates a listing for it.
     *
     * @param book The book to add
     */
    public void addBookToOwned(Book book) {
        ownedLibrary.addBookListing(new BookListing(book));
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
        listing.addRequest(getUsername());
        requestedBooks.addBookListing(listing);
    }

    /**
     * Removes a listing from this user's requested library
     *
     * @param listing The listing to remove
     */
    public void removeListingFromRequested(BookListing listing) {
        listing.cancelRequest(this.username);
        requestedBooks.removeBookListing(listing);
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
        UserAccount cloned = new UserAccount(username);
        cloned.setProfile(this.profile);
        cloned.setOwnedLibrary(ownedLibrary.clone());
        cloned.setRequestedBooks(requestedBooks.clone());
        cloned.setReviews((ArrayList<Review>) reviews.clone());
        return cloned;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
