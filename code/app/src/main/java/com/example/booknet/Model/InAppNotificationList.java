package com.example.booknet.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class InAppNotificationList implements Serializable, Iterable<InAppNotification>  {

    private ArrayList<InAppNotification> inAppNotifications;

    /**
     * Constructor
     */
    public InAppNotificationList() {
        this.inAppNotifications = new ArrayList<>();
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
     * Removes a InAppNotification to the library.
     *
     * @param inAppNotification The InAppNotification to remove
     */
    public void removeNotification(InAppNotification inAppNotification) {
        this.inAppNotifications.remove(inAppNotification);
    }

    /**
     * Get the number of inAppNotifications in the list of inAppNotifications.
     *
     * @return Size of the InAppNotificationList list
     */
    public int size() {
        return inAppNotifications.size();
    }

    @Override
    public Iterator<InAppNotification> iterator() {
        return inAppNotifications.iterator();
    }

    public InAppNotification getNotificationAtPosition(int position) {
        return inAppNotifications.get(position);
    }
}
