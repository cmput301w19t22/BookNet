package com.example.booknet.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class InAppNotifications implements Serializable, Iterable<InAppNotification>  {

    private ArrayList<InAppNotification> inAppNotifications;

    /**
     * Constructor
     */
    public InAppNotifications() {
        this.inAppNotifications = new ArrayList<>();
    }

    /**
     * Gets the list of InAppNotification.
     *
     * @return ArrayList containing InAppNotifications
     */
    public ArrayList<InAppNotification> getInAppNotifications() {
        return inAppNotifications;
    }

    /**
     * Adds a InAppNotification to the library.
     *
     * @param inAppNotification The InAppNotification to add
     */
    public void addNotification(InAppNotification inAppNotification) {
        this.inAppNotifications.add(inAppNotification);
    }

    /**
     * Removes a InAppNotification from the library.
     *
     * @param inAppNotification The InAppNotification to remove
     */
    public void removeNotification(InAppNotification inAppNotification) {
        if (this.inAppNotifications.contains(inAppNotification)) {
            this.inAppNotifications.remove(inAppNotification);
        }
    }

    public void removeAllNotificiations(){
        inAppNotifications.clear();
    }

    /**
     * Get the number of inAppNotifications in the list of inAppNotifications.
     *
     * @return Size of the InAppNotifications list
     */
    public int size() {
        return inAppNotifications.size();
    }

    /**
     * Checks whether a listing exists for a certain book in this library.
     *
     * @return True if a notification is found for the notification, false otherwise
     */
    /*public boolean containsNotificationFor(InAppNotification notification) {
        for (InAppNotification notifictationIter : inAppNotifications) {
            if (notification.getNotification().equals(notifictationIter)) {
                return true;
            }
        }
        return false;
    }*/

    @Override
    public Iterator<InAppNotification> iterator() {
        return inAppNotifications.iterator();
    }

    public InAppNotifications clone(){
        InAppNotifications cloned = new InAppNotifications();
        for (InAppNotification n: this) cloned.addNotification(n.clone());
        return cloned;
    }


    public InAppNotification getNotificationAtPosition(int position) {
        return inAppNotifications.get(position);
    }
}
