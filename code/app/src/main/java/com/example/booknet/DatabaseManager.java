package com.example.booknet;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

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

    //singleton pattern
    private static final DatabaseManager manager = new DatabaseManager();

    private BookLibrary userBookLibrary = new BookLibrary();
    private BookLibrary allBookLibrary = new BookLibrary();
    private DatabaseReference allListingsRef;
    private DatabaseReference userLisitngsRef;
    private boolean readwritePermission = false;



    private DatabaseManager(){

    }

    public DatabaseReference getUserLisitngsRef(){
        return userLisitngsRef;
    }
    public DatabaseReference getAllLisitngsRef(){
        return allListingsRef;
    }



    public static DatabaseManager getInstance(){
        return manager;
    }




    /**
     * Writes a BookListing to the database
     *
     * @param listing The listing to write
     */

    public void writeToAllBookListings(BookListing listing) {


        final DatabaseReference allListingsRef = FirebaseDatabase.getInstance().getReference("/BookListings/");
        allListingsRef.child(listing.getBook().getIsbn()).setValue(listing);

    }


    // write a book to the user owned book listings
    // also adds the listing to the app
    public void writeUserBookListing(BookListing listing){

        String uid = CurrentUser.getInstance().getUID();

        userLisitngsRef.child(listing.getBook().getIsbn()).setValue(listing);

        allListingsRef.child(listing.getBook().getIsbn()).setValue(listing);

    }

    /**
     * Writes just the info for a book to the database without a listing
     *
     * @param book The book to write
     */
    public void writeBookInfo(Book book) {
        //todo: implement
    }

    /**
     * Writes a review to the database
     *
     * @param review
     */
    public void writeReview(Review review) {
        //todo: implement
    }


    //replaced by writeUserBookListing

//    /**
//     * Writes the user's library of owned books to the database
//     *
//     * @param bookLibrary
//     */
//    public void writeOwnedLibrary(BookLibrary bookLibrary) {
//    }


    /**
     * Writes the user's library of requested books to the database
     *
     * @param bookLibrary
     */
    public void writeRequestLibrary(BookLibrary bookLibrary) {
        //todo: implement
    }

    //#endregion

    /**
     * Deletes a BookListing from the database
     *
     * @param bookListing The BookListing to delete
     */
    public void removeBookListing(BookListing bookListing) {
        userLisitngsRef.child(bookListing.getBook().getIsbn()).removeValue();
    }


    public void setBookListingStatus(BookListing bookListing, BookListing.Status status) {
        //todo: implement
    }

    /**
     * Adds a request to the BookListing in the database
     *
     * @param bookListing The listing to update
     * @param requester   The user who made the request
     */
    public void addRequestToListing(BookListing bookListing, String requester) {
        //todo: implement
    }

    /**
     * Removes a request from a listing in the database.
     *
     * @param bookListing The listing to update
     * @param requester   The user whose request to remove
     */
    public void removeRequestFromListing(BookListing bookListing, String requester) {
        //todo: implement
    }

    /**
     * Marks the listing as accepted in the database
     *
     * @param bookListing The listing to update
     * @param requester   The user whose request is accepted
     */
    public void acceptRequestForListing(BookListing bookListing, String requester) {
        //todo:implement
    }

    /**
     * Declines the request of a user for a book in the database
     *
     * @param bookListing The listing to update
     * @param requester   The user whose request is declined
     */
    public void declineRequestForListing(BookListing bookListing, String requester) {
        //todo:implement
    }


    /**
     * Reads a user account from the database.
     *
     * @param username The name of the user to obtain
     * @return The UserAccount for the user if it exists
     */
    public UserAccount readUserAccount(String username) {
        //todo: implement
        return null;
    }

    /**
     * Reads a Book from the database
     *
     * @param isbn The ISBN of the book as an id
     * @return The book requested if it exists
     */
    public Book readBookInfo(String isbn) {
        //todo: implement
        return null;
    }

    /**
     * Reads all listings from the database.
     *
     * @return A list of BookListings from the database if any are found
     */
    public BookLibrary readAllBookListings() {

        return allBookLibrary;

    }

    /**
     * Reads the reviews for a specific user from the database
     *
     * @param username The user whose reviews to obtain
     * @return A list of the user's reviews, if any are found
     */
    public ArrayList<Review> readUserReviews(String username) {
        //todo change int to dfferent type
        return null;
    }

    /**
     * Reads the user's library of owned books from the database.
     *
     * @return A BookLibrary for the given user
     */
    public BookLibrary readUserOwnedLibrary() {
        return userBookLibrary;
    }

    /**
     * Reads the user's library of requested books from the database.
     *
     * @param username The user whose books to read.
     * @return A BookLibrary for the given user
     */
    public BookLibrary readUserRequests(String username) {
        //todo: implement
        return null;
    }

    public void connetToDatabase() {
        new InitiationTask().execute();
    }

    public BookListing readUserOwnedBookListingWithISBN(String isbn) {
        for (BookListing listing: userBookLibrary){
            if (listing.getBook().getIsbn().equals(isbn)){
                return listing;
            }
        }
        return null;
    }

    public BookListing readBookListingWithISBN(String isbn) {
        for (BookListing listing: allBookLibrary){
            if (listing.getBook().getIsbn().equals(isbn)){
                return listing;
            }
        }
        return null;
    }


    public class InitiationTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("mattTag", "database connection task started");
            allListingsRef = FirebaseDatabase.getInstance().getReference("BookListings");
            // This listener should take care of database value change automatically
            allListingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    allBookLibrary.removeAllBooks();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookListing bookListing = data.getValue(BookListing.class);

                        if (bookListing != null) {
                            // once the data is changed, we just change the corresponding static variable
                            allBookLibrary.addBookListing(bookListing);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

            String uid = CurrentUser.getInstance().getUID();
            userLisitngsRef = FirebaseDatabase.getInstance().getReference("/UserBooks/"+uid+"/BookListings");

            // This listener should take care of database value change automatically
            userLisitngsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // once the data is changed, we just change our corresponding static variable

                    //first empty it
                    userBookLibrary.removeAllBooks();

                    // then fill it as it is in the database
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookListing bookListing = data.getValue(BookListing.class);
                        if (bookListing != null) {
                            userBookLibrary.addBookListing(bookListing);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.d("mattTag", "DatabaseManager: database connected, allowing read/writes");
            if (success) allowReadWrite();
        }

    }

    // temporarily not in effect: database read/write permission is always allowed even if connection failed
    // to make this work, handler of failing read/write cases should be added.
    public void allowReadWrite(){
        readwritePermission = true;
    }



}
