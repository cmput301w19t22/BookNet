package com.example.booknet.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the collection of reviews from the database
 */
public class InAppNotificationManager {

    //Collection to keep the reviews in each entry in this map is the list
    private Map<String, InAppNotificationList> inAppNotifications = new HashMap<>();

    /**
     * Adds the review to the collection.
     *
     * @param inAppNotification
     */
    public void addInAppNotification(InAppNotification inAppNotification) {
        //Create a new collection if the user has no reviews
        if (!inAppNotifications.containsKey(inAppNotification.getUserReceivingNotification())) {
            inAppNotifications.put(inAppNotification.getUserReceivingNotification(), new InAppNotificationList());
        }
        inAppNotifications.get(inAppNotification.getUserReceivingNotification()).addNotification(inAppNotification);
    }

    /**
     * Gets the reviews for a given user.
     *
     * @param user The user whose reviews to obtain.
     * @return A ReviewList collection, which is empty if no reviews found.
     */
    public InAppNotificationList getInAppNotifications(String user) {
        if (inAppNotifications.containsKey(user)) {
            return inAppNotifications.get(user);
        } else {
            //Return an empty collection
            return new InAppNotificationList();
        }
    }

    /**
     * Clears the data structure.
     */
    public void clear() {
        inAppNotifications.clear();
    }

}
