package com.example.booknet;

import java.util.ArrayList;

/**
 * A fake memory based database for testing the activities. Uses the same function
 * signatures as the real database but overrides their functions.
 *
 * @author Jamie
 * @see DatabaseManager
 */
public class MockDatabase extends DatabaseManager {
    //Singleton Pattern
    private static final MockDatabase ourInstance = new MockDatabase();

    public static MockDatabase getInstance() {
        return ourInstance;
    }

    //Data
    ArrayList<BookListing> bookListings;
    ArrayList<UserAccount> userAccounts;
    ArrayList<Book> books;

    /**
     * Private constructor to init the mock database and populate it.
     */
    private MockDatabase() {
        bookListings = new ArrayList<>();
        userAccounts = new ArrayList<>();
        books = new ArrayList<>();
        populateDatabase();
    }

    /**
     * Inserts testing data into the mock database
     */
    private void populateDatabase() {
        UserAccount user1 = new UserAccount("User1");
        UserAccount user2 = new UserAccount("User2");
        UserAccount user3 = new UserAccount("User3");

        Book book1 = new Book("Title1", "Author1", "Description", "1000");
        Book book2 = new Book("Title2", "Author2", "Description", "1001");
        Book book3 = new Book("Title3", "Author3", "Description", "1002");
        Book book4 = new Book("Title4", "Author4", "Description", "1003");
        Book book5 = new Book("Title5", "Author5", "Description", "1004");
        Book book6 = new Book("Title6", "Author6", "Description", "1005");

        user1.addBookToOwned(book1);
        user2.addBookToOwned(book2);
        user2.addBookToOwned(book3);
        user3.addBookToOwned(book4);
        user3.addBookToOwned(book5);
        user3.addBookToOwned(book6);

        writeUserAccount(user1);
        writeUserAccount(user2);
        writeUserAccount(user3);
        writeUserAccount(CurrentUser.getInstance().getUserAccount());
    }

    //#region Overridden Methods
    @Override
    public void writeUserAccount(UserAccount account) {
        //Remove old version
        for (UserAccount user : userAccounts) {
            if (user.getUsername().equals(account.getUsername())) {
                userAccounts.remove(user);
            }
        }
        userAccounts.add(account);
        //Write user's library as well
        BookLibrary owned = account.getOwnedLibrary();
        for (BookListing listing : owned.getBooks()) {
            writeBookListing(listing);
        }
    }

    @Override
    public void writeBookListing(BookListing listing) {
        bookListings.add(listing);
    }

    @Override
    public void writeBookInfo(Book book) {
        books.add(book);
    }

    @Override
    public void writeReview(Review review) {

    }

    @Override
    public void removeBookListing(BookListing bookListing) {
        CurrentUser.getInstance().getOwnedLibrary().removeBookListing(bookListing);
        writeUserAccount(CurrentUser.getInstance().getUserAccount());
    }

    @Override
    public UserAccount readUserAccount(String username) {
        //Find the user in the array
        for (UserAccount user : userAccounts) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Book readBookInfo(String isbn) {
        return super.readBookInfo(isbn);
    }

    @Override
    public ArrayList<Review> readUserReviews(String username) {
        return super.readUserReviews(username);
    }


    //Constants for the below function
    public static final int OWNEDLIBRARY = 0;
    public static final int REQUESTLIBRARY = 1;

    /**
     * Reads a specific BookListing
     *
     * @param username Owner
     * @param bookisbn Book Id
     * @param library  Which library to search? Owned = 0, Requests = 1
     * @return
     */
    public BookListing readBookListing(String username, String bookisbn, int library) {
        BookLibrary bookLibrary = null;
        if (library == 0) {
            bookLibrary = readUserOwnedLibrary(username);
        } else if (library == 1) {
            bookLibrary = readUserRequests(username);
        }
        if (bookLibrary != null) {
            //Search library for the book
            ArrayList<BookListing> listings = bookLibrary.getBooks();
            for (BookListing bookListing : listings) {
                if (bookListing.getBook().getIsbn().equals(bookisbn)) {
                    return bookListing;
                }
            }
        }
        return null;
    }

    @Override
    public void readAllBookListings(BookSearchActivity activity) {
    }

    @Override
    public BookLibrary readUserOwnedLibrary(String username) {
        //Find the user in the array
        for (UserAccount user : userAccounts) {
            if (user.getUsername().equals(username)) {
                return user.getOwnedLibrary();
            }
        }
        return null;
    }

    @Override
    public BookLibrary readUserRequests(String username) {
        //Find the user in the array
        for (UserAccount user : userAccounts) {
            if (user.getUsername().equals(username)) {
                return user.getRequestedBooks();
            }
        }
        return null;
    }
    //#endregion
}
