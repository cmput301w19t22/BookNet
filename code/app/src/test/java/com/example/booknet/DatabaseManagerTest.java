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

        UserAccount user = new UserAccount("testuser");

        databaseManager.writeUserAccount(user);

        assertTrue(false);
        //todo: interact with actual database
    }

    @Test
    public void WriteBookListing() {
        DatabaseManager databaseManager = new DatabaseManager();

        UserAccount user = new UserAccount("testuser");
        Book book = new Book("title", "author", "description", "1234567890");
        BookListing listing = new BookListing(book);

        databaseManager.writeToAllBookListings(listing);

        assertTrue(false);
        //todo: interact with actual database
    }

    @Test
    public void WriteBookInfo() {
        DatabaseManager databaseManager = new DatabaseManager();

        UserAccount user = new UserAccount("testuser");
        Book book = new Book("title", "author", "description", "1234567890");

        databaseManager.writeBookInfo(book);

        assertTrue(false);
        //todo: interact with actual database
    }

    @Test
    public void WriteReview() {
        DatabaseManager databaseManager = new DatabaseManager();

        UserAccount reviewer = new UserAccount("testuser");
        UserAccount reviewed= new UserAccount("testuser2");
        Book book = new Book("title", "author", "description", "1234567890");
        BookListing listing = new BookListing(book);

        Review review = new Review(reviewer, reviewed, 4, "test");

        databaseManager.writeReview(review);

        ArrayList<Review> reviewedUserReviews = reviewed.getReviews();

        assertTrue(false);
        //todo: interact with actual database
    }

    @Test
    public void ReadUserAccount() {
        DatabaseManager databaseManager = new DatabaseManager();

        //Add the user account
        UserAccount user = new UserAccount("testuser");
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
        Book book = new Book("title", "author", "description", "1234567890");
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
        UserAccount reviewer = new UserAccount("testuser");
        UserAccount reviewed = new UserAccount("testuser2");
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
        UserAccount user1 = new UserAccount("testuser");
        Book book = new Book("title", "author", "description","1234567890");
        BookListing listing = new BookListing(book);
        databaseManager.writeToAllBookListings(listing);

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
        UserAccount user1 = new UserAccount("testuser");
        Book book = new Book("title", "author", "description","1234567890");
        BookListing listing = new BookListing(book);
        listing.addRequest("testuser");
        databaseManager.writeToAllBookListings(listing);

        //Read the listing back
        BookLibrary library = databaseManager.readUserRequestLibrary("testuser");
        ArrayList<BookListing> books = library.getBooks();

        assertNotNull(library);
        assertTrue(books.contains(listing));
    }

}
