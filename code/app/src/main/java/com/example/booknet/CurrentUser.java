package com.example.booknet;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseUser;

/**
 * A singleton data structure that contains the currently logged in user data.
 */
public class CurrentUser{
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

        account = new UserAccount("Jhon_Doe");
        account.setProfile(new UserProfile("Jhon_Doe", "default_email", "phone"));

        //MockDatabase.getInstance().writeUserAccount(account);
    }

    //Attributes
    private UserAccount account;

    private FirebaseUser user;

    public void setUser(FirebaseUser user){
        this.user = user;
        account.setProfileEmail(user.getEmail());
    }

    public String getUID(){
        return user.getUid();
    }


    /**
     * Method to call when loging in as a new user to update the structure.
     */
    public void onLogin() {

    }

    /**
     * Method to call when loging out as a new user to update the structure.
     */
    public void onLogout() {

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
     * Creates a request to add a book for this user.
     *
     * @param book The book to add.
     */
    public void requestAddBook(Book book) {




        //Create a listing for the new book
        BookListing newListing = new BookListing(book, account);

        // no more adding to memory, adding to database is enough
//        account.addListingToOwned(newListing);

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
}
