package com.example.booknet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.booknet.BookListing.Status.Available;
import static com.example.booknet.BookListing.Status.Requested;

/**
 * Class that interfaces with the database
 */
public class DatabaseManager {

    //singleton pattern
    private static final DatabaseManager manager = new DatabaseManager();

    private BookLibrary userBookLibrary = new BookLibrary();
    private BookLibrary allBookLibrary = new BookLibrary();
    private Map<String, String> usernames = new HashMap<>();
    private Map<String, String> userProfile = new HashMap<>();

    //used to freeze user interaction when connecting
    private ProgressDialog progressDialog;

    private DatabaseReference allListingsRef;
    private DatabaseReference userLisitngsRef;
    private DatabaseReference usernameRef;
    private DatabaseReference userPhoneRef;
    private DatabaseReference userProfileRef;

    private ValueEventListener allListingsListener;
    private ValueEventListener userLstingsListener;
    private ValueEventListener usernameListener;
    private ValueEventListener userPhoneListener;
    private ValueEventListener userProfileListener;

    private Boolean phoneLoaded = false;
    private Boolean nameLoaded = false;
    private Boolean onLoginPage = true;

    //not in effect
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


        allListingsRef.child(listing.getBook().getIsbn()+"-"+CurrentUser.getInstance().getUID()).setValue(listing);

    }



    // write a book to the user owned book listings
    // also adds the listing to the app
    public void writeUserBookListing(BookListing listing){


        userLisitngsRef.child(listing.getBook().getIsbn()).setValue(listing);

        allListingsRef.child(listing.getBook().getIsbn()+"-"+CurrentUser.getInstance().getUID()).setValue(listing);

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
        allListingsRef.child(bookListing.getBook().getIsbn()+"-"+CurrentUser.getInstance().getUID()).removeValue();
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

    public void connectToDatabase(Activity context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("connecting to database");

        progressDialog.show();
        new InitiationTask(context).execute();






    }

    public BookListing readUserOwnedBookListingWithISBN(String isbn) {
        for (BookListing listing: userBookLibrary){
            if (listing.getBook().getIsbn().equals(isbn)){
                return listing;
            }
        }
        return null;
    }

    public BookListing readBookListingWithUIDAndISBN(String UID, String isbn) {
        for (BookListing listing: allBookLibrary){
            if (listing.getBook().getIsbn().equals(isbn)){
                return listing;
            }
        }
        return null;
    }

    public boolean isUsernameTaken(String username) {
        return usernames.containsKey(username);
    }

    /**
     * writes the username under the current user's UID (this method should only be called when the user is signed in)
     *
     * @param username the username to write to the database
     */
    public void writeUsername(String username){
        usernameRef.child(username).setValue(CurrentUser.getInstance().getUID());
    }

    public void writeUserPhone(String phone){
        userPhoneRef.child(CurrentUser.getInstance().getUID()).setValue(phone);
    }

    public void setOnLoginPage(boolean b) {
        onLoginPage = b;
    }

    public void resetAllRefs() {
        if (allListingsRef != null && allListingsListener != null) {
            allListingsRef.removeEventListener(allListingsListener);
        }
        if (userLisitngsRef != null && userLstingsListener != null) {
            userLisitngsRef.removeEventListener(userLstingsListener);
        }
        if (userPhoneRef!= null && userPhoneListener!= null) {
            userPhoneRef.removeEventListener(userPhoneListener);
        }
        if (usernameRef!= null && usernameListener!= null) {
            usernameRef.removeEventListener(usernameListener);
        }
        if (userProfileRef != null && userProfileListener != null){
            userProfileRef.removeEventListener(userProfileListener);
        }

    }

    public void writeUserProfile(String newEmail, String newPhone) {
        userProfileRef.child("Email").setValue(newEmail);
        userProfileRef.child("Phone").setValue(newPhone);
    }

    public HashMap<String, String> readUserProfile(){

        if (userProfile.size() == 2){
            return (HashMap<String, String>) userProfile;
        }
        else{
            Log.d("mattTag", "nahnah");
            Log.d("mattTag", String.valueOf(userProfile.size()));
            HashMap<String, String> profile = new HashMap<String, String>();
            profile.put("Email", CurrentUser.getInstance().getDefaultEmail());
            profile.put("Phone", CurrentUser.getInstance().getAccountPhone());
            return profile;
        }

    }

    public DatabaseReference getUserListingsRef() {
        return userLisitngsRef;
    }

    public void onLogOut() {
        resetAllRefs();
        phoneLoaded = false;
        nameLoaded = false;
    }

    /**
     * @param listing
     * @return whether the request goes through
     */
    public boolean requestBookListing(BookListing listing) {
        if (isBookListingAvailable(listing)){
            allListingsRef.child(listing.getISBN()+"-"+CurrentUser.getInstance().getUID()).child("status").setValue(Requested);
            allListingsRef.child(listing.getISBN()+"-"+CurrentUser.getInstance().getUID()).child("borrowerName").setValue(CurrentUser.getInstance().getUsername());
            return true;
        }
        return false;

    }

    private boolean isBookListingAvailable(BookListing listing) {
        for (BookListing l: allBookLibrary){
            if (l.getOwnerUsername().equals(listing.getOwnerUsername()) && l.getISBN().equals(listing.getISBN())){
                return listing.getStatus() == Available;
            }
        }
        return false;
    }


    public class InitiationTask extends AsyncTask<Void, Void, Boolean> {
        Activity context;


        InitiationTask(Activity context) {
            this.context = context;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("mattTag", "database connection task started");

            allListingsListener = new ValueEventListener() {
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
            };
            allListingsRef = FirebaseDatabase.getInstance().getReference("BookListings");
            // This listener should take care of database value change automatically
            allListingsRef.addValueEventListener(allListingsListener);


            userProfileListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    userProfile.clear();
                    HashMap<String, String> fetchedProfile = (HashMap<String, String>) dataSnapshot.getValue();
//                    Log.d("mattTag", "fetched" + String.valueOf(fetchedProfile.size()));
                    if (fetchedProfile != null){
                        userProfile.putAll(fetchedProfile);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            userProfileRef = FirebaseDatabase.getInstance().getReference("/UserProfiles/"+CurrentUser.getInstance().getUID());
            userProfileRef.addValueEventListener(userProfileListener);

            // This listener should take care of database value change automatically
            allListingsRef.addValueEventListener(allListingsListener);

            String uid = CurrentUser.getInstance().getUID();


            userLisitngsRef = FirebaseDatabase.getInstance().getReference("/UserBooks/"+uid);
            userLstingsListener = new ValueEventListener() {
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
            };

            // This listener should take care of database value change automatically
            userLisitngsRef.addValueEventListener(userLstingsListener);


            usernameRef = FirebaseDatabase.getInstance().getReference("Usernames");

            // This listener should take care of database value change automatically
            usernameListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // once the data is changed, we just change our corresponding static variable

                    usernames.clear();


                    // then fill it as it is in the database
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String name = data.getKey();
                        String uid = data.getValue(String.class);
                        usernames.put(name, uid);
                        if (uid.equals(CurrentUser.getInstance().getUID())){
                            CurrentUser.getInstance().setUsername(name);
                        }

                    }

                    nameLoaded = true;
                    Log.d("mattTag", "nameLoaded");
                    if (phoneLoaded && progressDialog != null){
                        Log.d("mattTag", "checking from name");
                        if (onLoginPage){
                            LoginPageActivity loginPageActivity = (LoginPageActivity) context;
                            progressDialog.dismiss();
                            progressDialog = null;
                            Log.d("mattTag", "dismissed from name");

                            if (CurrentUser.getInstance().getUsername() == null || CurrentUser.getInstance().getAccountPhone() == null){
                                loginPageActivity.promptInitialProfile();
                            }
                            else{
                                loginPageActivity.goToMainPage();
                            }

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };

            usernameRef.addValueEventListener(usernameListener);



            userPhoneListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // once the data is changed, we just change our corresponding static variable



                    // then fill it as it is in the database
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String uid = data.getKey();
                        String phone = data.getValue(String.class);

                        if (uid.equals(CurrentUser.getInstance().getUID())){
                            CurrentUser.getInstance().setAccountPhone(phone);
                        }

                    }
                    phoneLoaded = true;
                    Log.d("mattTag", "phoneLoaded");
                    if (nameLoaded && progressDialog != null){
                        Log.d("mattTag", "checking from phone");
                        Log.d("mattTag", onLoginPage.toString());
                        if (onLoginPage){

                            LoginPageActivity loginPageActivity = (LoginPageActivity) context;
                            progressDialog.dismiss();
                            progressDialog = null;


                            if (CurrentUser.getInstance().getUsername() == null || CurrentUser.getInstance().getAccountPhone() == null){
                                loginPageActivity.promptInitialProfile();
                            }
                            else{
                                loginPageActivity.goToMainPage();
                            }

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };

            userPhoneRef = FirebaseDatabase.getInstance().getReference("UserPhones");

            // This listener should take care of database value change automatically
            userPhoneRef.addValueEventListener(userPhoneListener);


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.d("mattTag", "DatabaseManager: database connected, allowing read/writes");
            if (success) allowReadWrite();
//            dialog.dismiss();
        }

    }



    // temporarily not in effect: database read/write permission is always allowed even if connection failed
    // to make this work, handler of failing read/write cases should be added.
    public void allowReadWrite(){
        readwritePermission = true;
    }



}
