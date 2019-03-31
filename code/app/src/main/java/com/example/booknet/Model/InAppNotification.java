package com.example.booknet.Model;

import com.example.booknet.Constants.NotificationType;

import java.io.Serializable;

public class InAppNotification implements Serializable {

    private BookListing requestedBookListing;
    private String userReceivingNotification;
    private String userMakingNotification;
    private NotificationType notificationType;
    private Boolean pushNotificationSent;

    public void setPushNotificationSent(Boolean pushNotificationSent) {
        this.pushNotificationSent = pushNotificationSent;
    }

    public Boolean getPushNotificationSent() {
        return pushNotificationSent;
    }

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

    public InAppNotification() {
        this.requestedBookListing = new BookListing();
        this.userReceivingNotification = "";
        this.userMakingNotification = "";
        this.notificationType = null;
        this.pushNotificationSent = false;
    }

    public InAppNotification(BookListing requestedBookListing, String userReceivingNotification, String userMakingNotification, NotificationType notificationType) {
        this.requestedBookListing = requestedBookListing;
        this.userReceivingNotification = userReceivingNotification;
        this.userMakingNotification = userMakingNotification;
        this.notificationType = notificationType;
        this.pushNotificationSent = false;
    }

    public InAppNotification clone(){
        return new InAppNotification(requestedBookListing.clone(), userReceivingNotification, userMakingNotification, notificationType);
    }
}
