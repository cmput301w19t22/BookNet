package com.example.booknet.Model;

import com.example.booknet.Constants.NotificationType;

import java.io.Serializable;

public class Notification implements Serializable {

    private BookListing requestedBookListing;
    private String userReceivingNotification;
    private String userMakingNotification;
    private NotificationType notificationType;

    public BookListing getRequestedBookListing() {
        return requestedBookListing;
    }

    public String getUserReceivingNotification() {
        return userReceivingNotification; }

    public String getUserMakingNotification() {
        return userMakingNotification; }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public Notification() {
        this.requestedBookListing = new BookListing();
        this.userReceivingNotification = "";
        this.userMakingNotification = "";
        this.notificationType = null;
    }

    public Notification(BookListing requestedBookListing, String userReceivingNotification, String userMakingNotification, NotificationType notificationType) {
        this.requestedBookListing = requestedBookListing;
        this.userReceivingNotification = userReceivingNotification;
        this.userMakingNotification = userMakingNotification;
        this.notificationType = notificationType;
    }
    public Notification clone(){
        return new Notification(requestedBookListing.clone(), userReceivingNotification, userMakingNotification, notificationType);


    }


}
