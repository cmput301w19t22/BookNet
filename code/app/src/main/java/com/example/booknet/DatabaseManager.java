package com.example.booknet;

import android.os.AsyncTask;
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

    private BookLibrary userLibrary;
    private ArrayList<BookListing> booklistings;

    private boolean readwritePermission = false;

    private DatabaseManager(){

    }

    public static DatabaseManager getInstance(){
        return manager;
    }



    /**
     * Writes a BookListing to the database
     *
     * @param listing The listing to write
     */

    public void writeBookListing(BookListing listing) {
        //Get Database Connection
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/UserBooks/");

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

    /**
     * Writes the user's library of owned books to the database
     *
     * @param bookLibrary
     */
    public void writeOwnedLibrary(BookLibrary bookLibrary) {
        //todo: implement
    }

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
        //todo: implement
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

    //#region Reading From Database

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
    public void readAllBookListings(final BookSearchActivity activity) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BookListings");
        //bookListings = new ArrayList<>();

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    BookListing bookListing = data.child("BookListing").getValue(BookListing.class);
                    if (bookListing != null) {
                        System.out.println(bookListing.getOwnerUsername());
                        activity.addListingToList(bookListing);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

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
     * @param username The user whose books to read.
     * @return A BookLibrary for the given user
     */
    public BookLibrary readUserOwnedLibrary(String username) {
        //todo: implement
        return null;
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

    //#endregion
    //#endregion
    public class InitiationTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {

            final DatabaseReference listingsRef = FirebaseDatabase.getInstance().getReference("BookListings");
            // This listener should take care of database value change automatically
            listingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    booklistings.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookListing bookListing = data.child("BookListing").getValue(BookListing.class);

                        if (bookListing != null) {
                            // once the data is changed, we just change the corresponding static variable
                            booklistings.add(bookListing);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

            final DatabaseReference userLibraryRef = FirebaseDatabase.getInstance().getReference("UserOwned");

            // This listener should take care of database value change automatically
            userLibraryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // once the data is changed, we just change our corresponding static variable

                    //first empty it
                    userLibrary.removeAllBooks();

                    // then fill it as it is in the database
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookListing bookListing = data.child("BookListing").getValue(BookListing.class);
                        if (bookListing != null) {
                            userLibrary.addBookListing(bookListing);
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

    public void allowReadWrite(){
        readwritePermission = true;
    }



}
