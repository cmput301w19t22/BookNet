package com.example.booknet;

import android.os.Debug;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class BookLibrary implements Serializable {

    private ArrayList<BookListing> books;
    private FirebaseDatabase database;

    public BookLibrary() {
        this.books = new ArrayList<BookListing>();
    }

    public ArrayList<BookListing> getBooks() {
        return books;
    }

    public void addBookListing(BookListing bookListing) {

        this.books.add(bookListing);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        UserAccount currentUserAccount = bookListing.getOwnerUsername();
        Book currentBook = bookListing.getBook();

        DatabaseReference listingRef = ref.child("BookListings");

        DatabaseReference userBookRef = ref.child("BookListings");

        BookListing testListing = new BookListing();

        listingRef.push().setValue(testListing);

        userBookRef.child("UserBooks")
                .child(currentUserAccount.getUsername())
                .child(currentBook.getIsbn()).setValue(currentBook);
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
