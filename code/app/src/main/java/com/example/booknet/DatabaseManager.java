package com.example.booknet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Class that interfaces with the database
 */
public class DatabaseManager {

    //todo: attributes, if any

    //Constructor
    public DatabaseManager() {
    }

    ;


    //#region Methods
    //#region Writing To Database
    public void writeUserAccount(UserAccount account) {
        //todo: implement
    }

    /**
     * Writes a BookListing to the database
     *
     * @param listing The listing to write
     */
    public void writeBookListing(BookListing listing) {
        //Get Database Connection
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        //Get Info for this listing
        String currentUserAccount = listing.getOwnerUsername();
        Book currentBook = listing.getBook();

        //Obtain Database
        DatabaseReference listingRef = ref.child("BookListings");
        DatabaseReference userBookRef = ref.child("UserBooks");

        //Write Listing
        listingRef.push().child("BookListing").setValue(listing);

        //Write Book
        userBookRef.child("UserBooks")
                .child(currentUserAccount)
                .child(currentBook.getIsbn()).setValue(currentBook);
    }

    public void writeBookInfo(Book book) {
        //todo: implement
    }

    public void writeReview(Review review) {
        //todo: implement
    }

    public void writeData(UserAccount userAccount) {
        //todo: implement
    }

    public void writeLibraryListings(BookLibrary bookLibrary) {
        //todo: implement
    }

    //#endregion

    public void removeBookListing(BookListing bookListing) {
        //todo: implement
    }

    public void setBookListingStatus(BookListing bookListing, BookListing.Status status) {
        //todo: implement
    }

    public void addRequestToListing(BookListing bookListing, UserAccount requester) {
        //todo: implement
    }

    public void removeRequestFromListing(BookListing bookListing, UserAccount requester) {
        //todo: implement
    }

    public void acceptRequestForListing(BookListing bookListing, UserAccount requester) {
        //todo:implement
    }

    public void declineRequestForListing(BookListing bookListing, UserAccount requester) {
        //todo:implement
    }

    //#region Reading From Database
    public UserAccount readUserAccount(String username) {
        //todo: implement
        return null;
    }

    public Book readBookInfo(String isbn) {
        //todo: implement
        return null;
    }

    public ArrayList<BookListing> readAllBookListings() {
        return null;
    }

    public ArrayList<Review> readUserReviews(String username) {
        //todo change int to dfferent type
        return null;
    }


    public BookLibrary readUserOwnedLibrary(String username) {
        //todo: implement
        return null;
    }

    public BookLibrary readUserRequests(String username) {
        //todo: implement
        return null;
    }
    //#endregion
    //#endregion
}
