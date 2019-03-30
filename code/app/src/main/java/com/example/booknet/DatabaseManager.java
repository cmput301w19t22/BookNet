package com.example.booknet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.booknet.Activities.LoginPageActivity;
import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.Constants.NotificationType;
import com.example.booknet.Model.Book;
import com.example.booknet.Model.BookLibrary;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Model.Notification;
import com.example.booknet.Model.Notifications;
import com.example.booknet.Model.Review;
import com.example.booknet.Model.UserAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.example.booknet.Constants.BookListingStatus.Accepted;
import static com.example.booknet.Constants.BookListingStatus.Available;
import static com.example.booknet.Constants.BookListingStatus.Borrowed;
import static com.example.booknet.Constants.BookListingStatus.Requested;

/**
 * Class that interfaces with the database
 */
public class DatabaseManager {

    //singleton pattern
    private static final DatabaseManager manager = new DatabaseManager();

    private BookLibrary userBookLibrary = new BookLibrary();
    private BookLibrary userRequestLibrary = new BookLibrary();
    private BookLibrary allBookLibrary = new BookLibrary();
    private Map<String, String> usernames = new HashMap<>();
    private Map<String, HashMap<String, String>> allUserProfile = new HashMap<>();
    private Notifications notifications = new Notifications();

    //used to freeze user interaction when connecting
    private ProgressDialog progressDialog;

    private DatabaseReference allListingsRef;
    private DatabaseReference userListingsRef;
    private DatabaseReference usernameRef;
    private DatabaseReference userPhoneRef;
    private DatabaseReference allUserProfileRef;
    private DatabaseReference notificationRef;
    private DatabaseReference notificationRefOther;
    private DatabaseReference notificationRefSelf;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    private ValueEventListener allListingsListener;
    private ValueEventListener userListingsListener;
    private ValueEventListener usernameListener;
    private ValueEventListener userPhoneListener;
    private ValueEventListener allUserProfileListener;
    private ValueEventListener notificationListener;


    private Boolean phoneLoaded = false;
    private Boolean nameLoaded = false;
    private Boolean onLoginPage = true;

    // synchronizes stuff. It ensures:
    // 1. write will be carried on one by one. new writes will be hold at writeLock.lock() util the last write finishes at writeLock.unlock()
    // 2. can't read while writing, read will be hold at readLock.lock(), which will only proceed after writeLock.unlock()

    //only three of them are getting locks because these can be edited by multiple ends.

    // read lock should be used when relevant local containers are accessed, write lock should be used when relevant local containers are editted
    private ReentrantReadWriteLock l1 = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock allListingReadLock = l1.readLock();
    private ReentrantReadWriteLock.WriteLock allListingWriteLock = l1.writeLock();

    private ReentrantReadWriteLock l2 = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock userListingReadLock = l2.readLock();
    private ReentrantReadWriteLock.WriteLock userListingWriteLock = l2.writeLock();

    private ReentrantReadWriteLock l3 = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock notificationReadLock = l3.readLock();
    private ReentrantReadWriteLock.WriteLock notificationWriteLock = l3.writeLock();


    //not in effect
    private boolean readwritePermission = false;

    private DatabaseManager() {
    }

    public DatabaseReference getUserListingsRef() {
        return userListingsRef;
    }

    public DatabaseReference getAllListingsRef() {
        return allListingsRef;
    }

    public static DatabaseManager getInstance() {
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
        return listing.getBook().getIsbn() + "-" + String.valueOf(dupCount) + "-" + uid;
    }

    public int getDupCount(BookListing listing, String UID) {
        int currentInd = 0;

        allListingReadLock.lock();
        for (BookListing l : allBookLibrary) {
            if (l.hasSameBook(listing) && doesBelong(l, UID)) {
                currentInd += 1;
            }
        }
        allListingReadLock.unlock();
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
    public void writeUserBookListing(BookListing listing) {

        int dupCount = getDupCount(listing, CurrentUser.getInstance().getUID());

        userListingsRef.child(generateUserListingPath(listing, dupCount)).setValue(listing);

        allListingsRef.child(generateAllListingPath(listing, dupCount, CurrentUser.getInstance().getUID())).setValue(listing);
    }


    /**
     * Overwrites the listing in the database for both UserBooks and AllListings
     *
     * @param listing
     */
    public void overwriteUserBookListing(BookListing listing) {

        int dupCount = listing.getDupInd();//getDupCount(listing, CurrentUser.getInstance().getUID());

        userListingsRef.child(generateUserListingPath(listing, dupCount)).setValue(listing);

        allListingsRef.child(generateAllListingPath(listing, dupCount, CurrentUser.getInstance().getUID())).setValue(listing);
    }


    public void writeNotification(Notification notification) {
        Log.d("seanTag", "write notification");
        notificationRef.child(notification.getUserReceivingNotification()
                + "-" + notification.getUserMakingNotification()
                + "-" + notification.getRequestedBookListing().getISBN()
                + "-" + notification.getRequestedBookListing().getDupInd()).setValue(notification);
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
        userListingsRef.child(generateUserListingPath(bookListing, bookListing.getDupInd())).removeValue();
        allListingsRef.child(generateAllListingPath(bookListing, bookListing.getDupInd(), getUIDFromName(bookListing.getOwnerUsername()))).removeValue();
    }

    public void removeNotification(Notification notification) {
        notificationRef.child(notification.getUserReceivingNotification()
                + "-" + notification.getUserMakingNotification()
                + "-" + notification.getRequestedBookListing().getISBN()
                + "-" + notification.getRequestedBookListing().getDupInd()).removeValue();
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
        DatabaseReference allRef = allListingsRef.child(generateAllListingPath(bookListing, bookListing.getDupInd(), getUIDFromName(bookListing.getOwnerUsername())));
        allRef.child("status").setValue(Accepted);
        allRef.child("requests").removeValue();
        allRef.child("borrowerName").setValue(requester);

        DatabaseReference userRef = userListingsRef.child(generateUserListingPath(bookListing, bookListing.getDupInd()));
        userRef.child("status").setValue(Accepted);
        userRef.child("requests").removeValue();
        userRef.child("borrowerName").setValue(requester);

        writeNotification(new Notification(bookListing, requester, bookListing.getOwnerUsername(), NotificationType.hasAccepted));
    }

    /**
     * Verifies the request from the owner of the booklisting
     *
     * @return True if the verification is complete (by both users)
     */
    public boolean verifyRequest(BookListing listing, boolean byOwner) {
        String key = byOwner ? "verifiedByOwner" : "verifiedByBorrower";
        String otherKey = !byOwner ? "verifiedByOwner" : "verifiedByBorrower";
        DatabaseReference allRef = allListingsRef.child(generateAllListingPath(listing, listing.getDupInd(), getUIDFromName(listing.getOwnerUsername())));
        DatabaseReference userRef = userListingsRef.child(generateUserListingPath(listing, listing.getDupInd()));
        allRef.child(key).setValue(true);
        userRef.child(key).setValue(true);

        //Check for other verification
        String otherVerified = allRef.child(otherKey).getKey();
        boolean isVerified = Boolean.parseBoolean(otherVerified);
        if (isVerified) {
            //Both verified so proceed with transaction
            if (listing.getStatus() == Accepted) {
                onBorrowVerified(listing);
            } else if (listing.getStatus() == Borrowed) {
                onReturnVerified(listing);
            } else {
                //Shouldn't be able to get here, so clear this action
                Log.d("jamie", "Tried to verify booklisting in " + listing.getStatus() + " state. Clearing verification.");
                clearVerification(listing);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Called when the borrow request is verified by both users
     */
    private void onBorrowVerified(BookListing listing) {
        changeListingStatusTo(listing, Borrowed);
        clearVerification(listing);
    }

    /**
     * Called when the return request is verified by both users
     */
    private void onReturnVerified(BookListing listing) {
        changeListingStatusTo(listing, Available);
        clearVerification(listing);
    }

    /**
     * Clears the verification on a listing. To be called when fulfilling a borrow or return,
     * and when canceling a transaction.
     *
     * @param listing The listing to clear
     */
    public void clearVerification(BookListing listing) {
        String keyOwn = "verifiedByOwner";
        String keyOther = "verifiedByBorrower";
        DatabaseReference allRef = allListingsRef.child(generateAllListingPath(listing, listing.getDupInd(), getUIDFromName(listing.getOwnerUsername())));
        DatabaseReference userRef = userListingsRef.child(generateUserListingPath(listing, listing.getDupInd()));
        allRef.child(keyOwn).setValue(false);
        allRef.child(keyOther).setValue(false);
        userRef.child(keyOwn).setValue(false);
        userRef.child(keyOther).setValue(false);
    }


    private void changeStatusToAcceptedAndSetBorrowerName(BookListing bookListing) {


    }

    public void writeThumbnailForListing(BookListing listing, Bitmap bitmap, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        // todo: we shouldn't allow "/" in the username
        StorageReference ref = storageRef.child(listing.getOwnerUsername()).child(listing.getISBN() + "-" + listing.getDupInd());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(onFailureListener).addOnSuccessListener(onSuccessListener);
    }


    /**
     * Declines the request of a user for a book in the database
     *
     * @param bookListing The listing to update
     * @param requester   The user whose request is declined
     */
    public void declineRequestForListing(BookListing bookListing, String requester) {
        int dupInd = bookListing.getDupInd();

        String allPath = generateAllListingPath(bookListing, dupInd, getUIDFromName(bookListing.getOwnerUsername()));
        String ownerPath = generateUserListingPath(bookListing, dupInd);//todo jamie edit
        allListingsRef.child(allPath).child("status").setValue(Requested);

        ArrayList<String> requesters = null;

        allListingReadLock.lock();
        for (BookListing l : allBookLibrary) {
            if (l.getOwnerUsername().equals(bookListing.getOwnerUsername()) && l.getISBN().equals(bookListing.getISBN())) {
                requesters = l.getRequests();
            }
        }
        allListingReadLock.unlock();

        if (requesters == null) return;

        requesters.remove(requester);
        userListingsRef.child(ownerPath).child("requests").setValue(requesters);
        allListingsRef.child(allPath).child("requests").setValue(requesters);
        if (requesters.size() == 0) {
            allListingsRef.child(allPath).child("status").setValue(Available);
            userListingsRef.child(ownerPath).child("status").setValue(Available);
        }


        writeNotification(new Notification(bookListing, requester, bookListing.getOwnerUsername(), NotificationType.hasDeclined));
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
        BookLibrary libClone = new BookLibrary();

        allListingReadLock.lock();
        libClone = allBookLibrary.clone();
        allListingReadLock.unlock();
        return libClone;
    }

    public Notifications getAllNotifications() {
        //Log.d("seanTag", "Get Notifications");

        Notifications cloned;
        notificationReadLock.lock();
        cloned = notifications.clone();
        notificationReadLock.unlock();

        return cloned;
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
     * Reads the current user's library of owned books from the database.
     *
     * @return A BookLibrary for the given user
     */
    public BookLibrary readUserOwnedLibrary() {
        BookLibrary libClone;
        userListingReadLock.lock();
        libClone = userBookLibrary.clone();
        userListingReadLock.unlock();
        return libClone;
    }

    /**
     * Reads the current user's library of requested books from the database.
     *
     * @return A BookLibrary for the given user
     */
    public BookLibrary readUserRequestLibrary() {
        BookLibrary result = new BookLibrary();
        allListingReadLock.lock();
        for (BookListing bl : allBookLibrary) {
            if (bl.getRequests().contains(CurrentUser.getInstance().getUsername()) ||
                    bl.getBorrowerName().equals(CurrentUser.getInstance().getUsername())) {
                result.addBookListing(bl.clone());
            }
        }
        allListingReadLock.unlock();


        return result;
    }

    public void connectToDatabase(Activity context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Connecting to Database...");

        progressDialog.show();
        new InitiationTask(context).execute();

    }

    public BookListing readUserOwnedBookListing(String isbn, int dupID) {
        BookListing res = null;

        userListingReadLock.lock();
        for (BookListing listing : userBookLibrary) {
            if (listing.getBook().getIsbn().equals(isbn)) {
                res = listing.clone();
            }
        }
        userListingReadLock.unlock();
        return res;
    }


    public boolean isUsernameTaken(String username) {
        return usernames.containsKey(username);
    }

    /**
     * writes the username under the current user's UID (this method should only be called when the user is signed in)
     *
     * @param username the username to write to the database
     */
    public void writeUsername(String username) {
        usernameRef.child(username).setValue(CurrentUser.getInstance().getUID());
    }

    public void writeUserPhone(String phone) {
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
        if (userPhoneRef != null && userPhoneListener != null) {
            userPhoneRef.removeEventListener(userPhoneListener);
        }
        if (usernameRef != null && usernameListener != null) {
            usernameRef.removeEventListener(usernameListener);
        }
        if (allUserProfileRef != null && allUserProfileListener != null) {
            allUserProfileRef.removeEventListener(allUserProfileListener);
        }
    }

    public void writeUserProfile(String newEmail, String newPhone) {
        allUserProfileRef.child(CurrentUser.getInstance().getUID()).child("Email").setValue(newEmail);
        allUserProfileRef.child(CurrentUser.getInstance().getUID()).child("Phone").setValue(newPhone);
    }

    public HashMap<String, String> readCurrentUserProfile() {
        HashMap<String, String> currentUserProfile = new HashMap<String, String>();
        if (allUserProfile.containsKey(CurrentUser.getInstance().getUID())) {
            HashMap<String, String> p = allUserProfile.get(CurrentUser.getInstance().getUID());
            currentUserProfile.putAll(p);
        } else {

            HashMap<String, String> profile = new HashMap<String, String>();
            profile.put("Email", CurrentUser.getInstance().getDefaultEmail());
            profile.put("Phone", CurrentUser.getInstance().getAccountPhone());
            return profile;
        }
        return currentUserProfile;

    }

    public void onLogOut() {
        resetAllRefs();
        phoneLoaded = false;
        nameLoaded = false;
    }

    public String getUIDFromName(String name) {
        return usernames.get(name);
    }

    /**
     * @param listing
     */
    public void requestBookListing(BookListing listing) throws DatabaseException {
        if (!isBookListingRequestableAndNotOwnBook(listing)) {
            throw new DatabaseException("You can't request this book");
        }


        ArrayList<String> newRequests = new ArrayList<String>();
        allListingReadLock.lock();
        for (BookListing l : allBookLibrary) {
            if (l.isSameListing(listing)) {
                for (String request : l.getRequests()) {
                    newRequests.add(request);
                }
                newRequests.add(CurrentUser.getInstance().getUsername());

                changeListingRequestsTo(listing, newRequests);
                changeListingStatusTo(listing, Requested);
                break;
            }
        }
        allListingReadLock.unlock();

        if (newRequests.size() == 0) {
            throw new DatabaseException("The requested booklisting is not found");
        }

        Log.d("mattTag", "wrting notif");
        writeNotification(new Notification(listing, listing.getOwnerUsername(), CurrentUser.getInstance().getUsername(), NotificationType.hasRequested));
        Log.d("mattTag", "finished wring noi");
    }


    public void requestBookListingRemoval(BookListing listing) throws DatabaseException {

        if (doesBelong(listing, CurrentUser.getInstance().getUID()))
            throw new DatabaseException("One cannot cancel request on his own book");

        try {
            allListingReadLock.lock();
            for (BookListing l : allBookLibrary) {
                if (l.isSameListing(listing)) {


                    ArrayList<String> newRequests = new ArrayList<String>();
                    boolean isRequester = false;
                    String requesterName = CurrentUser.getInstance().getUsername();
                    boolean isBorrower = l.getBorrowerName().equals(requesterName);
                    for (String request : l.getRequests()) {
                        if (!request.equals(requesterName)) newRequests.add(request);
                        else isRequester = true;
                    }

                    //for cancelling a request after it is accepted
                    if (isBorrower) {
                        changeListingStatusTo(listing, Available);
                        break;
                    }

                    if (!isRequester) {
                        throw new DatabaseException("The database doesn't recognize current user as one of the booklisting's requesters");
                    } else {
                        if (newRequests.size() == 0) {
                            changeListingStatusTo(listing, Available);
                        }
                        changeListingRequestsTo(listing, newRequests);
                    }
                    break;

                }
            }

        } finally { // finally is here to ensure that when the above exception occurs, read lock
            // will still be released and dead-lock situation won't happen
            // (a dead lock prevents all future allListing writes)
            allListingReadLock.unlock();
        }


    }

    /**
     * convenient method to change a listing's status both in AllListings and in corresponding UserListings
     * so that no more annoying path needs to deal with
     * <p>
     * makes changes in the database
     * <p>
     * should only be used internally as declared private
     *
     * @param l
     * @param s
     */
    private void changeListingStatusTo(BookListing l, BookListingStatus s) {
        String allPath = generateAllListingPath(l, l.getDupInd(), getUIDFromName(l.getOwnerUsername()));
        DatabaseReference allRef = allListingsRef.child(allPath).child("status");
        allRef.setValue(s);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserBooks/" + getUIDFromName(l.getOwnerUsername()) + "/" + generateUserListingPath(l, l.getDupInd())).child("status");
        userRef.setValue(s);
    }


    /**
     * convenient method to change a listing's requests both in AllListings and in corresponding UserListings
     * so that no more annoying path needs to deal with
     * <p>
     * makes changes in the database
     * <p>
     * should only be used internally as declared private
     *
     * @param l
     * @param newRequests
     */
    private void changeListingRequestsTo(BookListing l, ArrayList<String> newRequests) {
        String allPath = generateAllListingPath(l, l.getDupInd(), getUIDFromName(l.getOwnerUsername()));
        DatabaseReference allRef = allListingsRef.child(allPath).child("requests");
        allRef.setValue(newRequests);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserBooks/" + getUIDFromName(l.getOwnerUsername()) + "/" + generateUserListingPath(l, l.getDupInd())).child("requests");
        userRef.setValue(newRequests);
    }

    public boolean ifListingRequestedByCurrentUser(BookListing listing) {
        Boolean alreadyRequested = true;

        ArrayList<String> requesters = null;
        String borrower = "";

        allListingReadLock.lock();
        for (BookListing l : allBookLibrary) {
            if (l.getOwnerUsername().equals(listing.getOwnerUsername())
                    && l.getISBN().equals(listing.getISBN())
                    && l.getDupInd() == listing.getDupInd()) {
                requesters = l.getRequests();
                borrower = l.getBorrowerName();
            }
        }
        allListingReadLock.unlock();

        String currentUser = CurrentUser.getInstance().getUsername();

        if (requesters.contains(currentUser) || borrower.equals(currentUser))
            alreadyRequested = true;
        else
            alreadyRequested = false;

        return alreadyRequested;
    }

    private boolean isBookListingRequestableAndNotOwnBook(BookListing listing) {
        boolean res = true;
        userListingReadLock.lock();
        for (BookListing l : userBookLibrary) {
            if (l.isSameListing(listing)) {
                res = false;
                break;
            }
        }
        userListingReadLock.unlock();


        allListingReadLock.lock();
        for (BookListing l : allBookLibrary) {
            if (l.isSameListing(listing)) {
                res = listing.getStatus() == Available || listing.getStatus() == Requested;
                break;
            }
        }
        allListingReadLock.unlock();


        return res;
    }

    /**
     * read a bookListing from database
     *
     * @param username: the book listing's owner
     * @param isbn:     the isbn of the book
     * @param dupID:    the dupID of the book (saved as a field in the BookListing class)
     * @return
     */
    public BookListing readBookListingOfUsername(String username, String isbn, int dupID) {
        BookListing res = null;

        allListingReadLock.lock();
        for (BookListing l : allBookLibrary) {
            if (l.getOwnerUsername().equals(username) && l.getISBN().equals(isbn) && l.getDupInd() == dupID) {
                res = l.clone();
            }
        }
        allListingReadLock.unlock();

        return res;
    }

    public String getPhoneFromUsername(String ownerUsername) {
        String res = null;
        if (allUserProfile.containsKey(getUIDFromName(ownerUsername))) {
            res = allUserProfile.get(getUIDFromName(ownerUsername)).get("Phone");
        }

        return res;
    }

    ;

    public HashMap<String, String> readOtherUserProfile(String username) {
        HashMap<String, String> res = new HashMap<>();
        if (allUserProfile.containsKey(getUIDFromName(username))) {
            res.putAll(allUserProfile.get(getUIDFromName(username)));
        }
        return res;

    }

    public String getEmailFromUsername(String ownerUsername) {
        String uid = getUIDFromName(ownerUsername);
        if (allUserProfile.containsKey(uid)) {
            return allUserProfile.get(uid).get("Email");
        }
        return null;

    }

    public DatabaseReference getAllProfileRef() {
        return allUserProfileRef;
    }

    public void fetchListingThumbnail(BookListing listing, OnSuccessListener<byte[]> onSuccessListener, OnFailureListener onFailureListener) {
        StorageReference ref = storageRef.child(listing.getOwnerUsername()).child(listing.getISBN() + "-" + listing.getDupInd());

        // maximum size of the image
        final long TEN_MEGABYTE = 1024 * 1024 * 10;      //todo: limit the maximum size of the image the user can upload to 10 mb
        ref.getBytes(TEN_MEGABYTE).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
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

                    allListingWriteLock.lock();

                    allBookLibrary.removeAllBooks();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookListing bookListing = data.getValue(BookListing.class);

                        if (bookListing != null) {
                            // once the data is changed, we just change the corresponding static variable
                            allBookLibrary.addBookListing(bookListing);
                        }
                    }

                    allListingWriteLock.unlock();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            allListingsRef = FirebaseDatabase.getInstance().getReference("BookListings");
            // This listener should take care of database value change automatically
            allListingsRef.addValueEventListener(allListingsListener);

            allUserProfileListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    allUserProfile.clear();
                    HashMap<String, HashMap<String, String>> fetchedProfile = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
//                    Log.d("mattTag", "fetched" + String.valueOf(fetchedProfile.size()));

                    if (fetchedProfile != null) {
                        allUserProfile.putAll(fetchedProfile);
                    }
                    HashMap<String, String> currentUserProfile = fetchedProfile.get(CurrentUser.getInstance().getUID());
                    if (currentUserProfile != null) {

                        CurrentUser.getInstance().setProfile(currentUserProfile);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            allUserProfileRef = FirebaseDatabase.getInstance().getReference("UserProfiles");
            allUserProfileRef.addValueEventListener(allUserProfileListener);

            // This listener should take care of database value change automatically
            //allListingsRef.addValueEventListener(allListingsListener);

            String uid = CurrentUser.getInstance().getUID();

            userListingsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // once the data is changed, we just change our corresponding static variable

                    //first empty it
                    userListingWriteLock.lock();
                    userBookLibrary.removeAllBooks();

                    // then fill it as it is in the database
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookListing bookListing = data.getValue(BookListing.class);
                        if (bookListing != null) {
                            Log.d("mattTag", "at its root: " + bookListing.toString());
                            userBookLibrary.addBookListing(bookListing);
                        }
                    }
                    userListingWriteLock.unlock();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            userListingsRef = FirebaseDatabase.getInstance().getReference("/UserBooks/" + uid);
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
                        if (uid.equals(CurrentUser.getInstance().getUID())) {
                            CurrentUser.getInstance().setUsername(name);

                        }
                    }
                    nameLoaded = true;


                    Log.d("mattTag", "nameLoaded");
                    if (phoneLoaded && progressDialog != null) {

                        if (onLoginPage) {
                            LoginPageActivity loginPageActivity = (LoginPageActivity) context;
                            progressDialog.dismiss();
                            progressDialog = null;


                            if (CurrentUser.getInstance().getUsername() == null || CurrentUser.getInstance().getAccountPhone() == null) {
                                loginPageActivity.promptInitialProfile();
                            } else {
                                // old users may not have UserProfile entries, in that case, use account email and account phone as their profile
                                if (!allUserProfile.containsKey(CurrentUser.getInstance().getUID())) {
                                    writeUserProfile(CurrentUser.getInstance().getDefaultEmail(), CurrentUser.getInstance().getAccountPhone());
                                }

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

                        if (uid.equals(CurrentUser.getInstance().getUID())) {
                            CurrentUser.getInstance().setAccountPhone(phone);

                        }
                    }
                    phoneLoaded = true;

                    Log.d("mattTag", "phoneLoaded");
                    if (nameLoaded && progressDialog != null) {
                        Log.d("mattTag", "checking from phone");
                        Log.d("mattTag", onLoginPage.toString());
                        if (onLoginPage) {

                            LoginPageActivity loginPageActivity = (LoginPageActivity) context;
                            progressDialog.dismiss();
                            progressDialog = null;

                            if (CurrentUser.getInstance().getUsername() == null || CurrentUser.getInstance().getAccountPhone() == null) {
                                loginPageActivity.promptInitialProfile();
                            } else {
                                // old users may not have UserProfile entries, in that case, use account email and account phone as their profile
                                if (!allUserProfile.containsKey(CurrentUser.getInstance().getUID())) {
                                    writeUserProfile(CurrentUser.getInstance().getDefaultEmail(), CurrentUser.getInstance().getAccountPhone());
                                }
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

                    notificationWriteLock.lock();
                    notifications.removeAllNotificiations();

                    Log.d("seanTag", "start noti read for " + CurrentUser.getInstance().getUsername());

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Notification notification = data.getValue(Notification.class);
                        if (notification.getUserReceivingNotification().equals(CurrentUser.getInstance().getUsername())) {
                            Log.d("seanTag", "new noti " + notification.getRequestedBookListing().getISBN());
                            notifications.addNotification(notification);
                        }
                    }
                    notificationWriteLock.lock();
                    //Log.d("seanTag", "read notification " + CurrentUser.getInstance().getUsername());
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            notificationRef = FirebaseDatabase.getInstance().getReference("Notifications");
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
    public void allowReadWrite() {
        readwritePermission = true;
    }


    public class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }
}
