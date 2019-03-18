package com.example.booknet;

import java.io.Serializable;

public class Notification implements Serializable {

    private BookListing requestedBookListing;
    private String userReceivingNotification;
    private String userMakingNotification;

    public BookListing getRequestedBookListing() {
        return requestedBookListing;
    }

    public String getUserReceivingNotification() { return userReceivingNotification; }

    public String getUserMakingNotification() { return userMakingNotification; }

    public Notification() {
        this.requestedBookListing = new BookListing();
        this.userReceivingNotification = "";
        this.userMakingNotification = "";
    }

    public Notification(BookListing requestedBookListing, String userReceivingNotification, String userMakingNotification) {
        this.requestedBookListing = requestedBookListing;
        this.userReceivingNotification = userReceivingNotification;
        this.userMakingNotification = userMakingNotification;
    }

    public void notifyOwnerOfRequest() {

    }

    public void notifyRequesterOfAcceptance() {

    }

    public void notifyRequesterOfDecline() {

    }

    public void notifyAllRequestersOfDecline() {

    }
}
