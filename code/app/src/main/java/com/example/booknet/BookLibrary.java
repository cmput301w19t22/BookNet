package com.example.booknet;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        //DatabaseManager databaseManager = new DatabaseManager();
        //databaseManager.writeBookListing(bookListing);
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

    protected BookLibrary clone(){
        BookLibrary cloned = new BookLibrary();
        cloned.books = (ArrayList<BookListing>) this.books.clone();
        return cloned;
    }
}
