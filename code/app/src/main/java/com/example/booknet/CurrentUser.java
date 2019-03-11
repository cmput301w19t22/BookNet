package com.example.booknet;

public class CurrentUser {
    //Create Singleton Pattern
    private static final CurrentUser instance = new CurrentUser();

    public static CurrentUser getInstance() {
        return instance;
    }

    private CurrentUser() {
        account = new UserAccount("tester", "debug");
        account.setProfile(new UserProfile("name", "email", "phone"));
        //requestAddBook(new Book("Real1", "Author1", "Description", "isbn1"));
        //requestAddBook(new Book("Title2", "Author2", "Description", "isbn2"));
        //MockDatabase.getInstance().writeUserAccount(account);
    }

    //Attributes
    private UserAccount account;

    //todo implement everything

    public void Login() {
    }

    public void Logout() {
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public UserAccount getUserAccount() {
        return account;
    }

    public void requestAddBook(Book book) {
        BookListing newListing = new BookListing(book, account);
        account.addListingToOwned(newListing);
        MockDatabase.getInstance().writeBookListing(newListing);
        //MockDatabase.getInstance().writeUserAccount(account);

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.writeBookListing(newListing);
    }

    public BookLibrary getOwnedLibrary() {
        return account.getOwnedLibrary();
    }
}
