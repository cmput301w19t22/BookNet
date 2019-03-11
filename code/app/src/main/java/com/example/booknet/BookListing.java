package com.example.booknet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Keeps track of a book that is listed on the app.
 */
public class BookListing implements Serializable {

    /**
     * Enum for the status of a BookListing, so the values are more easily tracked
     */
    public enum Status {
        Available, Requested, Accepted, Borrowed;

        /**
         * Returns the status as a text string.
         *
         * @return
         */
        @Override
        public String toString() {
            switch (this) {
                case Available:
                    return "Available";
                case Requested:
                    return "Requested";
                case Accepted:
                    return "Accepted";
                case Borrowed:
                    return "Borrowed";
            }
            return super.toString();
        }
    }

    //Attributes
    private Book book;
    private Status status;
    private String ownerUsername;
    private ArrayList<String> requests;
    private String borrowerName;
    private UserLocation geoLocation;

    /**
     * Constructor that creates an empty listing
     */
    public BookListing() {
        this.book = new Book();
        this.ownerUsername = "";
        this.borrowerName = "";
        this.status = Status.Available;
        this.requests = new ArrayList<String>();
        this.geoLocation = new UserLocation();
    }

    /**
     * Creates a BookListing for a book owned by a given user.
     *
     * @param book  The book in the new listing
     * @param owner The owner of this listing
     */
    public BookListing(Book book, UserAccount owner) {
        this.book = book;
        this.ownerUsername = owner.getUsername();
        this.borrowerName = "";
        this.status = Status.Available;
        this.requests = new ArrayList<String>();
        this.geoLocation = new UserLocation();
    }

    //#region Getters Setters
    public Book getBook() {
        return book;
    }

    public Status getStatus() {
        return status;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public ArrayList<String> getRequesters() {
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

    //#endregion

    //#region Methods

    /**
     * Adds a request to this book
     *
     * @param requesterName The user who made the request.
     */
    public void addRequest(String requesterName) {
        //Only requestable when Available or Requested
        if (status == Status.Available || status == Status.Requested) {
            //Add to the list
            if (!requests.contains(requesterName)) {
                requests.add(requesterName);
            }
            //change status to reflect change
            this.status = Status.Requested;
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
        if (status == Status.Requested) {
            if (requests.contains(requesterName)) {
                requests.remove(requesterName);

                //Deny other requests
                for (Integer i = 0; i < requests.size();) {
                    String otherRequesterName = requests.get(i);
                    denyRequest(otherRequesterName);
                }

                requests.clear();

                //Accept this request
                status = Status.Accepted;
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
    public void cancelRequest(String requesterName){
        this.requests.remove(requesterName);
        //todo send notification for successful removal?
    }


    /**
     * Denies a user's request for this book.
     *
     *
     * @param requesterName - String -
     */
    public void denyRequest(String requesterName) {
        if (requests.contains(requesterName)) {
            requests.remove(requesterName);
            if(requests.size() < 1) this.status = Status.Available;
            //todo notify database
        }
    }

    /**
     * Call this to update this BookListing when the book is borrowed.
     */
    public void bookBorrowed() {
        //todo: complete?
        status = Status.Borrowed;
    }

    /**
     * Call this to update this BookListing when the book is returned
     */
    public void bookReturned() {
        //todo: complete?
        status = Status.Available;
        borrowerName = "";
    }
    //#endregion

}
