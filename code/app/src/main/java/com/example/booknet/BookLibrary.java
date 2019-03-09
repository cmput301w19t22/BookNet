package com.example.booknet;

import java.io.Serializable;
import java.util.ArrayList;

public class BookLibrary implements Serializable {

    private ArrayList<BookListing> books;

    public BookLibrary() {
        this.books = new ArrayList<BookListing>();
    }

    public ArrayList<BookListing> getBooks() {
        return books;
    }

    public void addBookListing(BookListing bookListing) {
        this.books.add(bookListing);
    }

    public void removeBookListing(BookListing bookListing) {
        this.books.remove(bookListing);
    }

    public int size(){
        return books.size();
    }

    public boolean containsListingFor(Book book) {
        return false;
    }

}
