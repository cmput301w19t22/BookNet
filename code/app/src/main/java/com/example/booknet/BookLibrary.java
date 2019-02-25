package com.example.booknet;

import java.util.ArrayList;

public class BookLibrary {

    private ArrayList<BookListing> books;

    public BookLibrary() {
        this.books = new ArrayList<BookListing>();
    }

    public ArrayList<BookListing> getBooks() {
        return books;
    }

    public void addBook(BookListing bookListing) {
        this.books.add(bookListing);
    }

    public void removeBook(BookListing bookListing) {
        this.books.remove(bookListing);
    }



}
