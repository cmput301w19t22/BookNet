package com.example.booknet;

public class CurrentUser {
    //Create Singleton Pattern
    private static final CurrentUser instance = new CurrentUser();

    public static CurrentUser getInstance() {
        return instance;
    }

    private CurrentUser() {
        account = new UserAccount("debug","debug");
    }

    //Attributes
    private UserAccount account;

    //todo implement everything

    public void Login(){}

    public void Logout(){}

    public UserAccount getUserAccount() {
        return account;
    }

    public void requestAddBook(Book book) {
        BookListing newListing = new BookListing(book, account);
        account.addBookToOwned(newListing);
    }

    public BookLibrary getOwnedLibrary(){
        return account.getOwnedLibrary();
    }
}
