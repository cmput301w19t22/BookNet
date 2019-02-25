package com.example.booknet;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {

    @Test
    public void WriteUserAccount() {
        DatabaseManager databaseManager = new DatabaseManager();

        UserAccount user = new UserAccount("testuser", "debug");

        databaseManager.writeUserAccount(user);

        //todo: what to assert?
    }

    @Test
    public void WriteBookListing() {
        DatabaseManager databaseManager = new DatabaseManager();

        UserAccount user = new UserAccount("testuser", "debug");
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "testuser");

        databaseManager.writeBookListing(listing);

        //todo: what to assert?
    }

    @Test
    public void WriteBookInfo() {
        DatabaseManager databaseManager = new DatabaseManager();

        UserAccount user = new UserAccount("testuser", "debug");
        Book book = new Book("title", "author", "description");

        databaseManager.writeBookInfo(book);

        //todo: what to assert?
    }

    @Test
    public void WriteReview() {
        DatabaseManager databaseManager = new DatabaseManager();

        UserAccount user1 = new UserAccount("testuser", "debug");
        UserAccount user2 = new UserAccount("testuser2", "debug");
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "testuser");

        Review review = new Review(user1, user2, 4, "test");

        databaseManager.writeReview(review);

        //todo: what to assert?
    }

    @Test
    public void ReadUserAccount() {
        DatabaseManager databaseManager = new DatabaseManager();

        //Add the user account
        UserAccount user = new UserAccount("testuser", "debug");
        databaseManager.writeUserAccount(user);

        //Read the user account
        UserAccount readUser = databaseManager.readUserAccount("testuser");

        assertNotNull(readUser);
        assertEquals(user, readUser);
    }

    @Test
    public void ReadBookInfo() {
        DatabaseManager databaseManager = new DatabaseManager();

        //Add a book
        Book book = new Book("title", "author", "description");
        databaseManager.writeBookInfo(book);

        //Read the book
        Book readBook = databaseManager.readBookInfo("???");//todo: proper isbn

        assertNotNull(readBook);
        assertEquals(book, readBook);
    }

    @Test
    public void ReadUserReviews() {
        DatabaseManager databaseManager = new DatabaseManager();

        //Create a review
        UserAccount reviewer = new UserAccount("testuser", "debug");
        UserAccount reviewed = new UserAccount("testuser2", "debug");
        Review review = new Review(reviewer, reviewed, 5, "review1");
        databaseManager.writeReview(review);

        //Read the review
        ArrayList<Review> reviews = databaseManager.readUserReviews("testuser");

        assertNotNull(reviews);
        assertTrue(reviews.contains(review));
    }

    @Test
    public void ReadUserOwnedLibrary() {
        DatabaseManager databaseManager = new DatabaseManager();

        //Add a listing
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "testuser");
        databaseManager.writeBookListing(listing);

        //Read the listing back
        BookLibrary library = databaseManager.readUserOwnedLibrary("testuser");
        ArrayList<BookListing> books = library.getBooks();

        assertNotNull(library);
        assertTrue(books.contains(listing));
    }

    @Test
    public void ReadUserRequests() {
        DatabaseManager databaseManager = new DatabaseManager();

        //Add a listing
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "testuser2");
        listing.addRequest("testuser");
        databaseManager.writeBookListing(listing);

        //Read the listing back
        BookLibrary library = databaseManager.readUserRequests("testuser");
        ArrayList<BookListing> books = library.getBooks();

        assertNotNull(library);
        assertTrue(books.contains(listing));
    }

}
