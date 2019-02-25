package com.example.booknet;

import android.location.Location;

import java.util.ArrayList;


public class BookListing {

    public enum Status {
        Available, Requested, Accepted, Borrowed
    }

    private Book book;
    private Status status;
    private String ownerName;
    private ArrayList<String> requests;
    private String borrowerName;
    private Location geoLocation;

    public BookListing(Book book, String ownerName) {
        this.book = book;
        this.ownerName = ownerName;
        this.status = Status.Available;
        this.requests = new ArrayList<String>();
    }

    //#region Getters Setters
    public Book getBook() {
        return book;
    }

    public Status getStatus() {
        return status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public ArrayList<String> getRequesterNames() {
        return requests;
    }

    public String getBorrowerName() {
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
