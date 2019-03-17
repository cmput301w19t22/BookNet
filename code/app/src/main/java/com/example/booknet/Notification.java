package com.example.booknet;

public class Notification {

    private BookListing requestedBookListing;

    public Notification(BookListing requestedBookListing) {
        this.requestedBookListing = requestedBookListing;
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
