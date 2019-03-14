package com.example.booknet;

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
        //Add the listing to my library
        account.addListingToOwned(newListing);
        //Send the listing to the database

        MockDatabase.writeBookListing(account, newListing);

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.writeBookListing(newListing);
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
