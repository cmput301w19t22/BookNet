package com.example.booknet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.booknet.BookListingStatus.Available;
import static com.example.booknet.BookListingStatus.Requested;

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
    private Notifications notifications = new Notifications();

    //used to freeze user interaction when connecting
    private ProgressDialog progressDialog;

    private DatabaseReference allListingsRef;
    private DatabaseReference userListingsRef;
    private DatabaseReference usernameRef;
    private DatabaseReference userPhoneRef;
    private DatabaseReference userProfileRef;
    private DatabaseReference notificationRef;

    private ValueEventListener allListingsListener;
    private ValueEventListener userListingsListener;
    private ValueEventListener usernameListener;
    private ValueEventListener userPhoneListener;
    private ValueEventListener userProfileListener;
    private ValueEventListener notificationListener;

    private Boolean phoneLoaded = false;
    private Boolean nameLoaded = false;
    private Boolean onLoginPage = true;

    //not in effect
    private boolean readwritePermission = false;

    private DatabaseManager(){
    }

    public DatabaseReference getUserListingsRef(){
        return userListingsRef;
    }
    public DatabaseReference getAllListingsRef(){
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
        int dupCount = getDupCount(listing, CurrentUser.getInstance().getUID());
        String path = generateAllListingPath(listing, dupCount, CurrentUser.getInstance().getUID());
        allListingsRef.child(path).setValue(listing);
    }

    private String generateAllListingPath(BookListing listing, int dupCount, String uid) {
        return listing.getBook().getIsbn()+"-"+String.valueOf(dupCount)+"-"+uid;
    }

    public int getDupCount(BookListing listing, String UID) {
        int currentInd = 0;

        for (BookListing l: allBookLibrary){
            if (l.hasSameBook(listing) && doesBelong(l, UID)){
                currentInd += 1;
            }
        }
        return currentInd;

    }

    private boolean doesBelong(BookListing l, String uid) {
        return getUIDFromName(l.getOwnerUsername()).equals(uid);
    }


    @SuppressLint("DefaultLocale")
    private String generateUserListingPath(BookListing listing, int dupCount) {

        return String.format("%s-%d", listing.getISBN(), dupCount);
    }


    // write a book to the user owned book listings
    // also adds the listing to the app
    public void writeUserBookListing(BookListing listing){

        int dupCount = getDupCount(listing, CurrentUser.getInstance().getUID());

        userListingsRef.child(generateUserListingPath(listing, dupCount)).setValue(listing);

        allListingsRef.child(generateAllListingPath(listing, dupCount, CurrentUser.getInstance().getUID())).setValue(listing);
	}

    public void writeNotification(Notification notification) {
        Log.d("seanTag", "write notification");
        notificationRef.child(notification.getUserReceivingNotification()).push().setValue(notification);
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
        userListingsRef.child(bookListing.getBook().getIsbn()).removeValue();
        allListingsRef.child(bookListing.getBook().getIsbn()+"-"+CurrentUser.getInstance().getUID()).removeValue();
    }


    public void setBookListingStatus(BookListing bookListing, BookListingStatus status) {
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

    public Notifications getAllNotifications() {
        Log.d("seanTag", "Get Notifications");
        return notifications;
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
        progressDialog.setCancelable(false);
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
        if (userListingsRef != null && userListingsListener != null) {
            userListingsRef.removeEventListener(userListingsListener);
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

    public void onLogOut() {
        resetAllRefs();
        phoneLoaded = false;
        nameLoaded = false;
    }

    public String getUIDFromName(String name){
        return usernames.get(name);
    }

    /**
     * @param listing
     * @return whether the request goes through
     */
    public boolean requestBookListing(BookListing listing, String requester) {
        if (isBookListingAvailableAndNotOwnBook(listing)){

            int dupInd = listing.getDupInd();

            String allPath = generateAllListingPath(listing, dupInd, getUIDFromName(listing.getOwnerUsername()));
            allListingsRef.child(allPath).child("status").setValue(Requested);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserBooks/"+getUIDFromName(listing.getOwnerUsername())+"/"+generateUserListingPath(listing,dupInd));
            ref.child("status").setValue(Requested);

            ArrayList<String> requesters = null;
            for (BookListing l: allBookLibrary){
                if (l.getOwnerUsername().equals(listing.getOwnerUsername()) && l.getISBN().equals(listing.getISBN())){
                    requesters = l.getRequests();
                }
            }
            if (requesters == null) return false;

            requesters.add(CurrentUser.getInstance().getUsername());
            ref.child("requests").setValue(requesters);
            allListingsRef.child(allPath).child("requests").setValue(requesters);

            writeNotification(new Notification(listing, listing.getOwnerUsername(), requester));

            return true;
        }
        return false;
    }

    private boolean isBookListingAvailableAndNotOwnBook(BookListing listing) {
        for (BookListing l: userBookLibrary){
            if (l.getISBN().equals(listing.getISBN())) return false;
        }

        for (BookListing l: allBookLibrary){
            if (l.getOwnerUsername().equals(listing.getOwnerUsername()) && l.getISBN().equals(listing.getISBN())){
                return listing.getStatus() == Available || listing.getStatus()== Requested;
            }
        }
        return false;
    }

    /**
     * read a bookListing from database
     *
     * @param username: the book listing's owner
     * @param isbn: the isbn of the book
     * @param dupID: the dupID of the book (saved as a field in the BookListing class)
     * @return
     */
    public BookListing readBookListingOfUsername(String username, String isbn, int dupID) {
        for (BookListing l: allBookLibrary){
            if (l.getOwnerUsername().equals(username) && l.getISBN().equals(isbn) && l.getDupInd() == dupID){
                return l;
            }
        }
        return null;
    }

    public class InitiationTask extends AsyncTask<Void, Void, Boolean> {
        Activity context;
        String uid = CurrentUser.getInstance().getUID();

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
            //allListingsRef.addValueEventListener(allListingsListener);

            String uid = CurrentUser.getInstance().getUID();

            userListingsListener = new ValueEventListener() {
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
            userListingsRef = FirebaseDatabase.getInstance().getReference("/UserBooks/"+uid);
            // This listener should take care of database value change automatically
            userListingsRef.addValueEventListener(userListingsListener);

            //usernameRef = FirebaseDatabase.getInstance().getReference("Usernames");

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
            usernameRef = FirebaseDatabase.getInstance().getReference("Usernames");
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
            userPhoneRef.addValueEventListener(userPhoneListener);

            notificationListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Notification notification = data.getValue(Notification.class);
                        Log.d("seanTag",  notification.getRequestedBookListing().getBook().getTitle());
                        notifications.addNotification(notification);
                    }
                    Log.d("seanTag", "read notification " +CurrentUser.getInstance().getUsername());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            notificationRef = FirebaseDatabase.getInstance().getReference("/Notifications/"+CurrentUser.getInstance().getUsername());
            notificationRef.addValueEventListener(notificationListener);

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
