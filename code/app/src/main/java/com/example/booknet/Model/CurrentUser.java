package com.example.booknet.Model;

import android.util.Log;

import com.example.booknet.DatabaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

/**
 * A singleton data structure that contains the currently logged in user data.
 */
public class CurrentUser {
    //Create Singleton Pattern
    private static final CurrentUser instance = new CurrentUser();

    public static CurrentUser getInstance() {
        return instance;
    }

    private DatabaseManager manager = DatabaseManager.getInstance();

    /**
     * Constructs the CurrentUser structure, cannot be called by others.
     */

    private CurrentUser() {
        //Create a default user account
        account = new UserAccount(null);
        account.setProfile(new UserProfile(null, "default_email", null));

        //MockDatabase.getInstance().writeUserAccount(account);
    }

    //Attributes
    private UserAccount account;
    private FirebaseUser user;

    public FirebaseUser getUser() {
        return user;
    }


    public String getUID() {
        return user.getUid();
    }


    /**
     * Method to call when loging in as a new user to update the structure.
     */
    public void onLogin() {

    }


    /**
     * Returns the account for the current user.
     *
     * @return A UserAccount for the current user
     */
    public UserAccount getUserAccount() {
        return account;
    }

    /**
     * Checks whether the current user is the given username
     *
     * @param username The username to check for
     * @return Whether the username matches
     */
    public boolean isMe(String username) {
        return account.getUsername().equals(username);
    }

    /**
     * Creates a request to add a book for this user.
     *
     * @param book The book to add.
     */
    public void requestAddBook(Book book) {
        //Create a listing for the new book
        BookListing newListing = new BookListing(book);
        //add the listing to the database
        manager.writeUserBookListing(newListing);
    }

    /**
     * Gets the current user's library of owned books
     *
     * @return A BookLibrary for the current user's owned books
     */
    public BookLibrary getOwnedLibrary() {
        return account.getOwnedLibrary();
    }

    public String getUsername() {
        Log.d("mattTag", "the user name" + account.getUsername());
        return getUserAccount().getUsername();
    }


    public void setUsername(String username) {
        account.setUsername(username);
        account.getProfile().setName(username);
    }

    public void setProfilePhone(String phonenumber) {
        account.setPhoneNumber(phonenumber);
    }

    /**
     * called after email and password are authenticated.
     * saves account info to CurrentUser singleton for future use
     * <p>
     * makes username and phone blank. As later a database check will be performed to check username and phone
     * Username and phone info will be updated after the database check.
     *
     * @param currentUser
     */
    public void updateUser(FirebaseUser currentUser) {
        this.user = currentUser;
        account.setProfileEmail(user.getEmail());

        // currently unkown
        setUsername(null);
        setProfilePhone(null);
    }

    public String getDefaultEmail() {
        return user.getEmail();

    }


    public String getAccountPhone() {
        return account.getPhoneNumber();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        manager.onLogOut();
        account = new UserAccount(null);
        account.setProfile(new UserProfile(null, "default_email", null));
        user = null;


    }

    public String getProfileEmail() {
        return account.getProfile().getEmail();
    }

    public String getProfilePhone() {
        return account.getProfile().getPhoneNumber();
    }

    public void setProfile(HashMap<String, String> currentUserProfile) {
        account.getProfile().setPhoneNumber(currentUserProfile.get("Phone"));
        account.getProfile().setEmail(currentUserProfile.get("Email"));
    }

    public void setProfileEmail(String email) {
        account.setProfileEmail(email);
    }
}
