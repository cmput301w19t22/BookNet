package com.example.booknet.Model;

import android.graphics.Bitmap;

import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.DatabaseManager;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Keeps track of a book that is listed on the app.
 */
public class BookListing implements Serializable, Cloneable {

    public boolean hasBookWithTheSameISBN(BookListing listing) {
        return book.isBookISBNtheSame(listing.book);
    }

    //Attributes
    private Book book;
    private BookListingStatus status;
    private String ownerUsername;
    private ArrayList<String> requests;
    private String borrowerName;
    private UserLocation geoLocation;
    private int dupInd;
    private Photo photo;
    private boolean verifiedByOwner;
    private boolean verifiedByBorrower;

    private DatabaseManager manager = DatabaseManager.getInstance();

    /**
     * Constructor that creates an empty listing
     */
    public BookListing() {
        this.book = new Book();
        this.ownerUsername = "";
        this.borrowerName = "";
        this.status = BookListingStatus.Available;
        this.requests = new ArrayList<String>();
        this.geoLocation = new UserLocation();
        this.dupInd = 0;
    }

    public int getDupInd() {
        return dupInd;
    }

    public void setDupInd(int ind) {
        dupInd = ind;
    }

    /**
     * Creates a BookListing for a book owned by a given user.
     *
     * @param book The book in the new listing
     */
    public BookListing(Book book) {
        this.book = book;
        this.ownerUsername = CurrentUser.getInstance().getUsername();
        this.borrowerName = "";
        this.status = BookListingStatus.Available;
        this.requests = new ArrayList<String>();
        this.geoLocation = new UserLocation();
        dupInd = manager.getListingDupCount(this, CurrentUser.getInstance().getUID());
    }

    //#region Getters Setters
    public Book getBook() {
        return book;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public UserLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(UserLocation userLocation) {
        this.geoLocation = userLocation;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void deletePhoto() {
        this.photo = null;
    }

    public boolean isVerifiedByOwner() {
        return verifiedByOwner;
    }

    public void setVerifiedByOwner(boolean verifiedByOwner) {
        this.verifiedByOwner = verifiedByOwner;
    }

    public boolean isVerifiedByBorrower() {
        return verifiedByBorrower;
    }

    public void setVerifiedByBorrower(boolean verifiedByBorrower) {
        this.verifiedByBorrower = verifiedByBorrower;
    }

    public void editTitle(String title) {
        book.setTitle(title);
    }

    public void editAuthor(String author) {
        book.setAuthor(author);
    }

    public void editIsbn(String isbn) {
        book.setIsbn(isbn);
    }

    public void editDescription(String description) {
        book.setDescription(description);
    }

    //#endregion

    //#region Methods

    /**
     * Adds a request to this book
     *
     * @param requesterName The user who made the request.
     */
    public void addRequest(String requesterName) {
        //Only requestable when Available or Requested
        if (status == BookListingStatus.Available || status == BookListingStatus.Requested) {
            //Add to the list
            if (!requests.contains(requesterName)) {
                requests.add(requesterName);
            }
            //change status to reflect change
            this.status = BookListingStatus.Requested;
            //todo send to database
        } else {
            //todo notify cannot add request
        }
    }

    /**
     * Accepts a user's request for this book..
     *
     * @param requesterName The user whose request to accept
     */
    public void acceptRequest(String requesterName) {
        if (status == BookListingStatus.Requested) {
            if (requests.contains(requesterName)) {
                requests.remove(requesterName);

                //Deny other requests
                for (Integer i = 0; i < requests.size(); ) {
                    String otherRequesterName = requests.get(i);
                    denyRequest(otherRequesterName);
                }

                requests.clear();

                //Accept this request
                status = BookListingStatus.Accepted;
                this.borrowerName = requesterName;
                //todo allow geolocation
                //todo notify database
            }
        }
    }

    /**
     * Cancels a user's request for this book.
     *
     * @param requesterName The user whose request to cancel
     */
    public void cancelRequest(String requesterName) {
        for (String r : requests) {
            if (r.equals(requesterName)) {
                requests.remove(r);
            }
        }
        //this.requests.remove(requesterName);
        if (this.borrowerName.equals(requesterName)) {
            borrowerName = "";
        }
        if (status == BookListingStatus.Accepted) {
            setStatus(BookListingStatus.Requested);
        }
        if (status == BookListingStatus.Requested) {
            if (requests.isEmpty()) {
                setStatus(BookListingStatus.Available);
            }
        }
    }


    /**
     * Denies a user's request for this book.
     *
     * @param requesterName - String -
     */
    public void denyRequest(String requesterName) {
        if (requests.contains(requesterName)) {
            requests.remove(requesterName);
            if (requests.size() < 1) this.status = BookListingStatus.Available;
            //todo notify database
        }
    }

    /**
     * Checks whether the listing is verified by both the owner and borrower
     * for either borrowing or returning a book.
     *
     * @return True if both users verified false otherwise
     */
    //public boolean isVerifiedByBothUsers() {
    //    return verifiedByOwner && verifiedByBorrower;
    //}

    /**
     * Call this to update this BookListing when the book is borrowed.
     */
    public void bookBorrowed() {
        setVerifiedByBorrower(false);//Reset the verification
        setVerifiedByOwner(false);
        status = BookListingStatus.Borrowed;
    }

    /**
     * Call this to update this BookListing when the book is returned
     */
    public void bookReturned() {
        setVerifiedByBorrower(false);//Reset the verification
        setVerifiedByOwner(false);
        status = BookListingStatus.Available;
        borrowerName = "";
    }

    public boolean containKeyword(String keyword) {
        return book.getTitle().toLowerCase().contains(keyword.toLowerCase())
                || book.getAuthor().toLowerCase().contains(keyword.toLowerCase())
                || getOwnerUsername().toLowerCase().contains(keyword.toLowerCase());
    }

    @Exclude
    public String getISBN() {
        return book.getIsbn();
    }


    public BookListingStatus getStatus() {
        return status;
    }

    public BookListing clone() {

        BookListing cloned = new BookListing();
        cloned.setBook(book);
        cloned.setBorrowerName(borrowerName);
        cloned.setStatus(status);

        ArrayList<String> nR = new ArrayList<>();
        for (String s : requests) nR.add(s);

        cloned.setRequests(nR);
        cloned.setGeoLocation(geoLocation);
        cloned.setOwnerUsername(ownerUsername);
        cloned.setPhoto(photo);

        cloned.setDupInd(dupInd);


        return cloned;
    }


    private void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    private void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }

    public void setStatus(BookListingStatus status) {
        this.status = status;
    }

    private void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    private void setBook(Book book) {
        this.book = book;
    }


    @Override
    public String toString() {
        String s = "";
        s += book.toString() + " with  status:" + status.toString() + " with ISBN: " + book.getIsbn() + " with dupID: " + dupInd;
        return s;
    }

    public String getOwnerPhone() {
        return manager.getPhoneFromUsername(ownerUsername);

    }

    public String getOwnerEmail() {
        return manager.getEmailFromUsername(ownerUsername);

    }

    public boolean isSameListing(BookListing listing) {
        return getISBN().equals(listing.getISBN())
                && dupInd == listing.getDupInd()
                && ownerUsername.equals(listing.getOwnerUsername());

    }

    public Bitmap getPhotoBitmap() {
        if (photo != null) {
            return photo.getBitmap();
        } else {
            return null;
        }

    }

    public boolean isOwnedBy(String username) {
        return ownerUsername.equals(username);
    }

    public boolean isRequestedBy(String username) {
        if(borrowerName.equals(username)) {
            return true;
        }
        for (String user : requests) {
            if (user.equals(username)) {
                return true;
            }
        }
        return false;
    }

    //#endregion

}
