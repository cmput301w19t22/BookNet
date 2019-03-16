package com.example.booknet;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookLibraryTest {

    @Test
    public void AddBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("user1", "password");
        Book book1 = new Book("book1", "a1", "desc", "1234567890");
        BookListing listing = new BookListing(book1);

        bookLibrary.addBookListing(listing);

        ArrayList<BookListing> books = bookLibrary.getBooks();
        assertTrue(books.contains(listing));
    }

    @Test
    public void RemoveBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("user1", "password");
        Book book1 = new Book("book1", "a1", "desc", "1234567890");
        BookListing listing = new BookListing(book1);

        bookLibrary.addBookListing(listing);
        bookLibrary.removeBookListing(listing);

        ArrayList<BookListing> books = bookLibrary.getBooks();
        assertFalse("Removed Book", books.contains(listing));

        //TODO: Try removing book that isn't in list
    }

    @Test
    public void ContainsListingForBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("user1", "password");
        Book book1 = new Book("book1", "a1", "desc", "1234567890");
        BookListing listing = new BookListing(book1);

        bookLibrary.addBookListing(listing);
        assertTrue("Contains Added Book", bookLibrary.containsListingFor(book1));
    }

}
