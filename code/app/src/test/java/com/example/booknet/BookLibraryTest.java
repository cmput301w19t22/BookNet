package com.example.booknet;

import com.example.booknet.Model.Book;
import com.example.booknet.Model.BookLibrary;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.UserAccount;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookLibraryTest {

    @Test
    public void AddBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("test_username");
        Book book1 = new Book("A Book", "An Author", "Description", "1234567890");
        BookListing listing = new BookListing(book1);

        bookLibrary.addBookListing(listing);

        ArrayList<BookListing> books = bookLibrary.getBooks();
        assertTrue(books.contains(listing));
    }

    @Test
    public void RemoveBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("test_username");
        Book test_book = new Book("A Book", "An Author", "Description", "1234567890");
        BookListing listing = new BookListing(test_book);

        bookLibrary.addBookListing(listing);
        bookLibrary.removeBookListing(listing);

        ArrayList<BookListing> books = bookLibrary.getBooks();
        assertFalse("Removed Book", books.contains(listing));

        //TODO: Try removing book that isn't in list
    }

    @Test
    public void ContainsListingForBook() {
        BookLibrary bookLibrary = new BookLibrary();
        UserAccount owner = new UserAccount("test_username");
        Book book1 = new Book("A Book", "An Author", "Description", "1234567890");
        BookListing listing = new BookListing(book1);

        bookLibrary.addBookListing(listing);
        assertTrue("Contains Added Book", bookLibrary.containsListingFor(book1));
    }

}
