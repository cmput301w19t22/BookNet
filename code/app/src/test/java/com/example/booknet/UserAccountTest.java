package com.example.booknet;


import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserAccountTest {

    @Test
    public void Constructor() {
        UserAccount user = new UserAccount("user1", "password");

        assertEquals("user1", user.getUsername());
        assertEquals("password", user.getAccountPassword());
    }

    @Test
    public void Setters() {
        UserAccount user = new UserAccount("user1", "password");

        //Check Id and Password
        user.setUsername("newname");
        user.setAccountPassword("newpass");

        assertEquals("newname", user.getUsername());
        assertEquals("newpass", user.getAccountPassword());


        //Check BookLibrary
        BookLibrary library = new BookLibrary();
        user.setOwnedLibrary(library);
        assertEquals(library, user.getOwnedLibrary());

        //Check Requested Books
        BookLibrary requests = new BookLibrary();
        user.setRequestedBooks(requests);
        assertEquals(requests, user.getRequestedBooks());

        //Reviews
        ArrayList<Review> reviews = new ArrayList<Review>();
        UserAccount user2 = new UserAccount("user2", "password");
        reviews.add(new Review(user, user2, 4, "review1"));
        user.setReviews(reviews);
        assertEquals(reviews, user.getReviews());
    }

    @Test
    public void AddBookToOwned() {
        UserAccount user = new UserAccount("user1", "password");
        Book book = new Book("title", "author", "description",  "1234567890");
        BookListing listing = new BookListing(book);

        user.addListingToOwned(listing);

        BookLibrary owned = user.getOwnedLibrary();
        assertTrue(owned.containsListingFor(book));
    }

    @Test
    public void RemoveBookFromOwned() {
        UserAccount user = new UserAccount("user1", "password");
        Book book = new Book("title", "author", "description", "1234567890");
        BookListing listing = new BookListing(book);

        user.addListingToOwned(listing);
        BookLibrary owned = user.getOwnedLibrary();
        assertTrue(owned.containsListingFor(book));

        user.removeBookFromOwned(listing);
        owned = user.getOwnedLibrary();
        assertFalse(owned.containsListingFor(book));
    }

    @Test
    public void AddListingToRequested() {
        UserAccount user = new UserAccount("user1", "password");
        UserAccount owner = new UserAccount("notMe", "password");
        Book book = new Book("title", "author", "description", "1234567890");
        BookListing listing = new BookListing(book);

        user.addListingToRequested(listing);

        BookLibrary requests = user.getRequestedBooks();
        assertTrue(requests.containsListingFor(book));
    }

    @Test
    public void RemoveListingFromRequested() {
        UserAccount user = new UserAccount("user1", "password");
        UserAccount owner = new UserAccount("notMe", "password");
        Book book = new Book("title", "author", "description", "1234567890");
        BookListing listing = new BookListing(book);

        user.addListingToRequested(listing);
        BookLibrary requests = user.getRequestedBooks();
        assertTrue(requests.containsListingFor(book));

        user.removeListingFromRequested(listing);
        requests = user.getOwnedLibrary();
        assertFalse(requests.containsListingFor(book));
    }
}
