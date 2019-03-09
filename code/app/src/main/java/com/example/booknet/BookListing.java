package com.example.booknet;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;


public class BookListing implements Serializable {

    public enum Status {
        Available, Requested, Accepted, Borrowed;

        /**
         * Returns the status as a text string.
         * @return
         */
        @Override
        public String toString() {
            switch (this){
                case Available: return "Available";
                case Requested: return "Requested";
                case Accepted: return "Accepted";
                case Borrowed: return "Borrowed";
            }
            return super.toString();
        }
    }

    private Book book;
    private Status status;
    private UserAccount ownerName;
    private ArrayList<UserAccount> requests;
    private UserAccount borrowerName;
    private Location geoLocation;

    public BookListing(Book book, UserAccount ownerName) {
        this.book = book;
        this.ownerName = ownerName;
        this.status = Status.Available;
        this.requests = new ArrayList<UserAccount>();
    }

    //#region Getters Setters
    public Book getBook() {
        return book;
    }

    public Status getStatus() {
        return status;
    }

    public UserAccount getOwnerName() {
        return ownerName;
    }

    public ArrayList<UserAccount> getRequesters() {
        return requests;
    }

    public UserAccount getBorrowerName() {
        return borrowerName;
    }

    public Location getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Location location) {
        this.geoLocation = location;
    }

    //#endregion

    //#region Methods
    public void addRequest(String requesterName) {
        //todo: implement
    }

    public void acceptRequest(String borrowerName) {
        //todo: implement
    }

    public void denyRequest(String userName) {
        //todo: implement
    }


    public void bookBorrowed() {
        //todo: implement
    }

    public void bookReturned() {
        //todo: implement}
    }
    //#endregion
}
