package com.example.booknet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.booknet.Activities.AccountCreateActivity;
import com.example.booknet.Activities.LoginPageActivity;
import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.Constants.NotificationType;
import com.example.booknet.Model.BookLibrary;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Model.InAppNotification;
import com.example.booknet.Model.InAppNotificationList;
import com.example.booknet.Model.InAppNotificationManager;
import com.example.booknet.Model.Photo;
import com.example.booknet.Model.Review;
import com.example.booknet.Model.ReviewList;
import com.example.booknet.Model.ReviewManager;
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

    //#region Variable Fields
    private BookLibrary userBookLibrary = new BookLibrary();
    private BookLibrary allBookLibrary = new BookLibrary();
    private Map<String, String> usernames = new HashMap<>();
    private Map<String, HashMap<String, String>> allUserProfile = new HashMap<>();
    private InAppNotificationManager inAppNotificationManager = new InAppNotificationManager();
    private ReviewManager reviewManager = new ReviewManager();
    //private ReviewList reviewList = new ReviewList();

    //used to freeze user interaction when connecting
    private ProgressDialog progressDialog;

    private DatabaseReference allListingsRef;
    private DatabaseReference userListingsRef;
    private DatabaseReference usernameRef;
    private DatabaseReference userPhoneRef;
    private DatabaseReference allUserProfileRef;
    private DatabaseReference notificationRef;
    private DatabaseReference reviewRef;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private ValueEventListener allListingsListener;
    private ValueEventListener userListingsListener;
    private ValueEventListener usernameListener;
    private ValueEventListener userPhoneListener;
    private ValueEventListener allUserProfileListener;
    private ValueEventListener notificationListener;
    private ValueEventListener reviewListener;

    private Boolean phoneLoaded = false;
    private Boolean nameLoaded = false;
    private Boolean onLoginPage = true;

    private HashMap<String, Bitmap> thumbNailCache = new HashMap<>();

    // synchronizes stuff. It ensures:
    // 1. write will be carried on one by one. new writes will be hold at writeLock.lock() util the last write finishes at writeLock.unlock()
    // 2. can't read while writing, read will be hold at readLock.lock(), which will only proceed after writeLock.unlock()

    // read lock should be used when relevant local containers are accessed, write lock should be used when relevant local containers are edited
    private ReentrantReadWriteLock l1 = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock allListingReadLock = l1.readLock();
    private ReentrantReadWriteLock.WriteLock allListingWriteLock = l1.writeLock();

    private ReentrantReadWriteLock l2 = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock userListingReadLock = l2.readLock();
    private ReentrantReadWriteLock.WriteLock userListingWriteLock = l2.writeLock();

    private ReentrantReadWriteLock l3 = new ReentrantReadWriteLock();
    //private ReentrantReadWriteLock.ReadLock notificationReadLock = l3.readLock();
    //private ReentrantReadWriteLock.WriteLock notificationWriteLock = l3.writeLock();

    private ReentrantReadWriteLock l4 = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock thumbnailCacheReadLock = l4.readLock();
    private ReentrantReadWriteLock.WriteLock thumbnailCacheWriteLock = l4.writeLock();

    private ReentrantReadWriteLock l5 = new ReentrantReadWriteLock();
    //private ReentrantReadWriteLock.ReadLock usernamesReadLock = l5.readLock();
    private ReentrantReadWriteLock.WriteLock usernamesWriteLock = l5.writeLock();

    private ReentrantReadWriteLock l6 = new ReentrantReadWriteLock();
    //private ReentrantReadWriteLock.ReadLock userProfileReadLock = l6.readLock();
    private ReentrantReadWriteLock.WriteLock userProfileWriteLock = l6.writeLock();

    //not in effect
    private boolean readwritePermission = false;
    //#endregion

    //#region Constructor, Getters, Setters

    /**
     * Empty Constructor
     */
    private DatabaseManager() {


    }


    public DatabaseReference getUserListingsRef() {
        return userListingsRef;
    }

    public DatabaseReference getAllListingsRef() {
        return allListingsRef;
    }

    public DatabaseReference getNotificationsRef() {
        return notificationRef;
    }

    public static DatabaseManager getInstance() {
        return manager;
    }

    //#endregion

    //#region BookListings Read/Write

    /**
     * Reads all listings from the database.
     *
     * @return A list of BookListings from the database if any are found
     */
    public BookLibrary readAllBookListings() {
        BookLibrary libClone;

        allListingReadLock.lock();
        libClone = allBookLibrary.clone();
        allListingReadLock.unlock();
        return libClone;
    }

    /**
     * Reads a BookListing from the current user's own library.
     *
     * @param isbn  ISBN of the book to read
     * @param dupID Duplicate id of the book.
     * @return The BookListing requested if it exists, null otherwise
     */
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

    // write a book to the user owned book listings
    // also adds the listing to the app

    /**
     * Writes BookListing to DB
     *
     * @param listing: Listing to write
     */
    public void writeUserBookListing(BookListing listing) {

        int dupCount = getListingDupCount(listing, CurrentUser.getInstance().getUID());

        userListingsRef.child(generateUserListingPath(listing, dupCount)).setValue(listing);

        allListingsRef.child(generateAllListingPath(listing, dupCount, CurrentUser.getInstance().getUID())).setValue(listing);
    }

    /**
     * Overwrites the listing in the database for both UserBooks and AllListings
     *
     * @param listing
     */
    public void overwriteUserBookListing(BookListing listing) {

        int dupCount = listing.getDupInd();//getListingDupCount(listing, CurrentUser.getInstance().getUID());

        userListingsRef.child(generateUserListingPath(listing, dupCount)).setValue(listing);

        allListingsRef.child(generateAllListingPath(listing, dupCount, CurrentUser.getInstance().getUID())).setValue(listing);
    }

    /**
     * Deletes a BookListing from the database
     *
     * @param bookListing The BookListing to delete
     */
    public void removeBookListing(BookListing bookListing) {
        userListingsRef.child(generateUserListingPath(bookListing, bookListing.getDupInd())).removeValue();
        allListingsRef.child(generateAllListingPath(bookListing, bookListing.getDupInd(), getUIDFromName(bookListing.getOwnerUsername()))).removeValue();
    }

    //#region Thumbnails

    /**
     * Writes the photo thumbnail for the listing into the database.
     *
     * @param listing Listing to write for
     * @param bitmap  Bitmap of the photo to write
     */
    public void writeThumbnailForListing(BookListing listing, Bitmap bitmap, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        // todo: we shouldn't allow "/" in the username
        StorageReference ref = storageRef.child(listing.getOwnerUsername()).child(listing.getISBN() + "-" + listing.getDupInd());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(onFailureListener).addOnSuccessListener(onSuccessListener);

        //Add to the cache
        thumbnailCacheWriteLock.lock();
        thumbNailCache.put(listing.getOwnerUsername() + "-" + listing.getISBN() + "-" + listing.getDupInd(), bitmap);
        thumbnailCacheWriteLock.unlock();
        //listing.setPhoto(new Photo(bitmap));
    }

    /**
     * Deletes the photo thumbnail from the database
     *
     * @param listing           Listing to write for
     * @param onSuccessListener
     * @param onFailureListener
     */
    public void deleteThumbnailForListing(BookListing listing, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        StorageReference ref = storageRef.child(listing.getOwnerUsername()).child(listing.getISBN() + "-" + listing.getDupInd());

        ref.delete().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }

    /**
     * @param listing : the booklisting that requires a thumbnail
     * @param adapter : the adpater to notify once the thumbnail is fetched from db and cached in manager
     */
    public synchronized void fetchListingThumbnail(final BookListing listing, final RecyclerView.Adapter adapter) {
        Log.d("mattFin", listing.toString());
        if (!(listing.getOwnerUsername().isEmpty() || listing.getISBN().isEmpty())) {
            StorageReference ref = storageRef.child(listing.getOwnerUsername()).child(listing.getISBN() + "-" + listing.getDupInd());


            Log.d("mattFin2", "trying to fetch " + listing.getOwnerUsername() + listing.getISBN() + "-" + listing.getDupInd());
            // maximum size of the image
            final long TEN_MEGABYTE = 1024 * 1024 * 10;      //todo: limit the maximum size of the image the user can upload to 10 mb

            ref.getBytes(TEN_MEGABYTE).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap fetchedThumnail = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    thumbnailCacheWriteLock.lock();
                    thumbNailCache.put(listing.getOwnerUsername() + "-" + listing.getISBN() + "-" + listing.getDupInd(), fetchedThumnail);
                    thumbnailCacheWriteLock.unlock();
                    listing.setPhoto(new Photo(fetchedThumnail));
                    adapter.notifyDataSetChanged();


                }
            });


        }
    }

    /**
     * Gets cached thumbnail from DB
     *
     * @param bl: BookListing for the thumbnail
     * @return: The thumbnail
     */
    public Bitmap getCachedThumbnail(BookListing bl) {
        thumbnailCacheReadLock.lock();
        Bitmap cachedBitmap = thumbNailCache.get(bl.getOwnerUsername() + "-" + bl.getISBN() + "-" + bl.getDupInd());
        thumbnailCacheReadLock.unlock();

        return cachedBitmap;
    }

    public void removeCachedThumbnail(BookListing bl) {
        thumbnailCacheWriteLock.lock();
        Bitmap toRemove = getCachedThumbnail(bl);
        toRemove.recycle();
        thumbNailCache.remove(bl.getOwnerUsername() + "-" + bl.getISBN() + "-" + bl.getDupInd());
        thumbnailCacheWriteLock.unlock();
    }
    //#endregion

    /**
     * Generates a path to store all listings
     *
     * @param listing:  Listing to save
     * @param dupCount: Duplication ID
     * @param uid:      Unique ID of user
     * @return: Returns the string path
     */
    private String generateAllListingPath(BookListing listing, int dupCount, String uid) {
        return listing.getBook().getIsbn() + "-" + String.valueOf(dupCount) + "-" + uid;
    }

    /**
     * Generates a path for user listings
     *
     * @param listing:  Listing to save
     * @param dupCount: Duplication ID
     * @return: Returns path string
     */
    @SuppressLint("DefaultLocale")
    private String generateUserListingPath(BookListing listing, int dupCount) {
        return String.format("%s-%d", listing.getISBN(), dupCount);
    }


    public int getListingDupCount(BookListing listing, String UID) {
        int currentInd = 0;

        allListingReadLock.lock();
        for (BookListing l : allBookLibrary) {
            if (l.hasBookWithTheSameISBN(listing) && belongsToUser(l, UID)) {
                currentInd += 1;
            }
        }
        allListingReadLock.unlock();
        return currentInd;
    }

    /**
     * Checks if the listing belongs to the user with the given UID.
     *
     * @param l
     * @param uid
     * @return
     */
    private boolean belongsToUser(BookListing l, String uid) {
        return getUIDFromName(l.getOwnerUsername()).equals(uid);
    }
    //#endregion

    //#region BookListings Requests

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
        writeNotification(new InAppNotification(listing, listing.getOwnerUsername(), CurrentUser.getInstance().getUsername(), NotificationType.hasRequested));
        Log.d("mattTag", "finished wring noi");
    }

    /**
     * Attempt to cancel a request for the BookListing
     *
     * @param listing The listing to remove a request from.
     * @throws DatabaseException
     */
    public void cancelRequestForListing(BookListing listing) throws DatabaseException {

        if (belongsToUser(listing, CurrentUser.getInstance().getUID()))
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
                        changeListingBorrowerNameTo(listing, "");
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

        writeNotification(new InAppNotification(bookListing, requester, bookListing.getOwnerUsername(), NotificationType.hasAccepted));
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
        writeNotification(new InAppNotification(bookListing, requester, bookListing.getOwnerUsername(), NotificationType.hasDeclined));
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


    private void changeListingBorrowerNameTo(BookListing l, String borrower) {
        String allPath = generateAllListingPath(l, l.getDupInd(), getUIDFromName(l.getOwnerUsername()));
        DatabaseReference allRef = allListingsRef.child(allPath).child("borrowerName");
        allRef.setValue(borrower);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserBooks/"
                + getUIDFromName(l.getOwnerUsername()) + "/" + generateUserListingPath(l, l.getDupInd())).child("borrowerName");
        userRef.setValue(borrower);
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
     * Verifies the request from the owner of the booklisting
     *
     * @return True if the verification is complete (by both users)
     */
    public boolean verifyRequest(BookListing listing, boolean byOwner,) {
        String key = byOwner ? "verifiedByOwner" : "verifiedByBorrower";
        String otherKey = !byOwner ? "verifiedByOwner" : "verifiedByBorrower";
        DatabaseReference allRef = allListingsRef.child(generateAllListingPath(listing, listing.getDupInd(), getUIDFromName(listing.getOwnerUsername())));
        DatabaseReference userRef = userListingsRef.child(generateUserListingPath(listing, listing.getDupInd()));
        allRef.child(key).setValue(true);
        userRef.child(key).setValue(true);

        BookListing dbListing = readBookListingOfUsername(listing.getOwnerUsername(),
                listing.getISBN(), listing.getDupInd());

        //Check for other verification
        //String otherVerified = allRef.child(otherKey).getKey();
        //boolean otherVerified = otherKey.equals("verifiedByBorrower") ? dbListing.isVerifiedByBorrower() : dbListing.isVerifiedByOwner();
        //boolean isVerified = Boolean.parseBoolean(otherVerified);
        boolean isVerified = otherKey.equals("verifiedByBorrower") ? dbListing.isVerifiedByBorrower() : dbListing.isVerifiedByOwner()
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

    //#endregion

    //#region BookLibraries

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
    //#endregion

    //#region User Accounts

    //#region Account

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
     * Gets the users UID
     *
     * @param name
     * @return
     */
    public String getUIDFromName(String name) {
        return usernames.get(name);
    }

    //#endregion

    //#region Profile

    /**
     * writes the username under the current user's UID
     *
     * @param username the username to write to the database
     */
    public void writeUsername(String username, DatabaseReference.CompletionListener completionListener) {
        usernameRef.child(username).setValue(CurrentUser.getInstance().getUID(), completionListener);
    }

    public void writeUserPhone(String phone, DatabaseReference.CompletionListener completionListener) {
        userPhoneRef.child(CurrentUser.getInstance().getUID()).setValue(phone);
    }

    public void writeUserProfile(String newEmail, String newPhone, DatabaseReference.CompletionListener completionListener1, DatabaseReference.CompletionListener completionListener2) {
        allUserProfileRef.child(CurrentUser.getInstance().getUID()).child("Email").setValue(newEmail, completionListener1);
        allUserProfileRef.child(CurrentUser.getInstance().getUID()).child("Phone").setValue(newPhone, completionListener2);
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

    public HashMap<String, String> readOtherUserProfile(String username) {
        HashMap<String, String> res = new HashMap<>();
        if (allUserProfile.containsKey(getUIDFromName(username))) {
            res.putAll(allUserProfile.get(getUIDFromName(username)));
        }
        return res;

    }

    public String getPhoneFromUsername(String ownerUsername) {
        String res = null;
        if (allUserProfile.containsKey(getUIDFromName(ownerUsername))) {
            res = allUserProfile.get(getUIDFromName(ownerUsername)).get("Phone");
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
    //#endregion
    //#endregion

    //#region Reviews

    /**
     * Writes a review to the database
     *
     * @param review
     */
    public void writeReview(Review review) {
        String receiverId = getUIDFromName(review.getReviewedUsername());
        reviewRef.child(receiverId)
                .child(review.getReviewerUsername()
                        + "-" + review.getDupId()).setValue(review);
    }

    /**
     * Reads the reviewList for the current user from the database
     *
     * @return A list of the Current user's reviewList, if any are found
     */
    public ReviewList readReviews(String username) {
        return reviewManager.getReviews(username);
    }

    /**
     * Reads the average rating score for the given user
     *
     * @param username The user whose reviews to check.
     * @return The average review score, or -1 if there are no reviews.
     */
    public float readUserReviewAverage(String username) {
        ReviewList reviewList = readReviews(username);
        float totalRating = 0;
        if (reviewList.size() > 0) {
            for (Review r : reviewList) {
                totalRating += r.getScore();
            }
            return totalRating / reviewList.size();
        } else {
            return -1;
        }
    }

    /**
     * Reads the number of reviews for a given user.
     *
     * @param username The user whoes reviews to check
     * @return The number of reviews the user has
     */
    public int readUserReviewCount(String username) {
        return readReviews(username).size();
    }

    public int getReviewDupCount(String reviewed, String reviewer) {
        int currentInd = 0;

        ReviewList reviewList = reviewManager.getReviews(reviewed);
        for (Review r : reviewList) {
            if (r.equals(reviewed) && r.equals(reviewer)) {
                currentInd += 1;
            }
        }
        return currentInd;
    }
    //#endregion

    //#region Notifications

    /**
     * Reads the notificationList for the current user from the database
     *
     * @return A list of the Current user's notificationList, if any are found
     */
    public InAppNotificationList readNotifications(String username) {
        return inAppNotificationManager.getInAppNotifications(username);
    }

    public void writeNotification(InAppNotification inAppNotification) {
        String receiverId = getUIDFromName(inAppNotification.getUserReceivingNotification());
        Log.d("seanTag", "write noti " + receiverId);
        notificationRef.child(receiverId)
                .child(inAppNotification.getUserMakingNotification()
                        + "-" + inAppNotification.getRequestedBookListing().getDupInd()).setValue(inAppNotification);
    }


    public void removeNotification(InAppNotification inAppNotification) {
        String receiverId = getUIDFromName(inAppNotification.getUserReceivingNotification());
        Log.d("seanTag", "remove noti " + receiverId);
        notificationRef.child(receiverId)
                .child(inAppNotification.getUserMakingNotification()
                        + "-" + inAppNotification.getRequestedBookListing().getDupInd()).removeValue();
    }

    //#endregion

    //#region Connection, Misc

    public void connectToDatabase(Activity context) {
        new InitiationTask(context).execute();
    }

    public void setOnLoginPage(boolean b) {
        onLoginPage = b;
    }

    public void onLogOut() {
        resetAllRefs();
        phoneLoaded = false;
        nameLoaded = false;
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

    public class InitiationTask extends AsyncTask<Void, Void, Boolean> {
        Activity context;

        InitiationTask(Activity context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String uid = CurrentUser.getInstance().getUID();

            allUserProfileListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userProfileWriteLock.lock();
                    allUserProfile.clear();
                    HashMap<String, HashMap<String, String>> fetchedProfile = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
//                    Log.d("mattTag", "fetched" + String.valueOf(fetchedProfile.size()));

                    if (fetchedProfile != null) {
                        allUserProfile.putAll(fetchedProfile);
                    }
                    HashMap<String, String> currentUserProfile = fetchedProfile.get(CurrentUser.getInstance().getUID());
                    if (currentUserProfile != null) {

                        CurrentUser.getInstance().setProfile(currentUserProfile);
                        if (LoginPageActivity.onLoginPage)
                            LoginPageActivity.notifyTaskFinished(context, "user profile (contact email, phone) fetched from database");
                    }
                    userProfileWriteLock.unlock();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            allUserProfileRef = FirebaseDatabase.getInstance().getReference("UserProfiles");
            allUserProfileRef.addValueEventListener(allUserProfileListener);

            usernameListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    usernamesWriteLock.lock();
                    usernames.clear();

                    // then fill it as it is in the database
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String name = data.getKey();
                        String uid = data.getValue(String.class);
                        usernames.put(name, uid);
                        if (uid.equals(CurrentUser.getInstance().getUID())) {
                            CurrentUser.getInstance().setUsername(name);
                            if (LoginPageActivity.onLoginPage)
                                LoginPageActivity.notifyTaskFinished(context, "username fetched from database");

                        }
                    }
                    usernamesWriteLock.unlock();
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
                            CurrentUser.getInstance().setProfilePhone(phone);

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
            allListingsRef.addValueEventListener(allListingsListener);

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
            userListingsRef.addValueEventListener(userListingsListener);

            notificationListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    inAppNotificationManager.clear();

                    Log.d("seanTag", "start noti read for " + CurrentUser.getInstance().getUsername());

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        for (DataSnapshot data : user.getChildren()) {
                            InAppNotification inAppNotification = data.getValue(InAppNotification.class);
                            if (inAppNotification.getUserReceivingNotification().equals(CurrentUser.getInstance().getUsername())) {
                                Log.d("seanTag", "new noti " + inAppNotification.getUserReceivingNotification());
                                inAppNotificationManager.addInAppNotification(inAppNotification);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            notificationRef = FirebaseDatabase.getInstance().getReference("InAppNotificationList");
            notificationRef.addValueEventListener(notificationListener);

            reviewListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    reviewManager.clear();

                    Log.d("seanTag", "start review read for " + CurrentUser.getInstance().getUsername());

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        String userUid = user.getKey();
                        for (DataSnapshot data : user.getChildren()) {
                            Review review = data.getValue(Review.class);
                            reviewManager.addReview(review);
                            Log.d("seanTag", "new review ");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            reviewRef = FirebaseDatabase.getInstance().getReference("ReviewList");
            reviewRef.addValueEventListener(reviewListener);


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.d("mattTag", "DatabaseManager: database connected, allowing read/writes");
            if (success) allowReadWrite();
            if (context instanceof AccountCreateActivity) {
                ((AccountCreateActivity) context).notifyTaskFinished("Connected to database");

                final String username = ((AccountCreateActivity) context).username;
                final String email = ((AccountCreateActivity) context).profileEmail;
                final String phone = ((AccountCreateActivity) context).phone;

                writeUsername(username, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        CurrentUser.getInstance().setUsername(username);
                        ((AccountCreateActivity) context).notifyTaskFinished("Username written to database");
                    }
                });

                manager.writeUserProfile(email, phone, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        CurrentUser.getInstance().setProfileEmail(email);
                        ((AccountCreateActivity) context).notifyTaskFinished("user email written to database");
                    }
                }, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        CurrentUser.getInstance().setProfilePhone(phone);
                        ((AccountCreateActivity) context).notifyTaskFinished("user phone written to database");
                    }
                });


            }
            if (context instanceof LoginPageActivity) {
                LoginPageActivity.notifyTaskFinished(context, "Connected to database");
            }
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
    //#endregion

}
