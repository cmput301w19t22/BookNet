package com.example.booknet;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class BookLibraryTest {

    @Test
    public void AddBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("user1", "password");
        Book book1 = new Book("book1", "a1", "desc");
        BookListing listing = new BookListing(book1, owner);

        bookLibrary.addBookListing(listing);

        ArrayList<BookListing> books = bookLibrary.getBooks();
        assertTrue("Contains Added Book", books.contains(book1));
    }

    @Test
    public void RemoveBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("user1", "password");
        Book book1 = new Book("book1", "a1", "desc");
        BookListing listing = new BookListing(book1, owner);

        bookLibrary.addBookListing(listing);
        bookLibrary.removeBookListing(listing);

        ArrayList<BookListing> books = bookLibrary.getBooks();
        assertTrue("Removed Book", books.contains(book1));

        //TODO: Try removing book that isn't in list
    }

    @Test
    public void ContainsListingForBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("user1", "password");
        Book book1 = new Book("book1", "a1", "desc");
        BookListing listing = new BookListing(book1, owner);

        bookLibrary.addBookListing(listing);
        assertTrue("Contains Added Book", bookLibrary.containsListingFor(book1));
    }

}
