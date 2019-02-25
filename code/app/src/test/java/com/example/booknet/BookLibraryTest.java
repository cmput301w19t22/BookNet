package com.example.booknet;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class BookLibraryTest {

    @Test
    public void AddBook(){
        BookLibrary bookLibrary =new BookLibrary();

        Book book1 = new Book("book1","a1","desc");
        BookListing listing = new BookListing(book1, "owner");

        bookLibrary.addBook(listing);

        ArrayList<BookListing> books= bookLibrary.getBooks();
        assertTrue("Contains Added Book",books.contains(book1));
    }
    @Test
    public void RemoveBook(){
        BookLibrary bookLibrary =new BookLibrary();

        Book book1 = new Book("book1","a1","desc");
        BookListing listing = new BookListing(book1, "owner");

        bookLibrary.addBook(listing);
        bookLibrary.removeBook(listing);

        ArrayList<BookListing> books= bookLibrary.getBooks();
        assertTrue("Removed Book",books.contains(book1));

        //TODO: Try removing book that isn't in list
    }
}
