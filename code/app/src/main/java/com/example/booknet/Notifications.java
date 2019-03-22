package com.example.booknet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Notifications implements Serializable, Iterable<Notification>  {

    private ArrayList<Notification> notifications;

    /**
     * Constructor
     */
    public Notifications() {
        this.notifications = new ArrayList<Notification>();
    }

    /**
     * Gets the list of Notification.
     *
     * @return ArrayList containing Notifications
     */
    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Adds a Notification to the library.
     *
     * @param notification The Notification to add
     */
    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    /**
     * Removes a Notification from the library.
     *
     * @param notification The Notification to remove
     */
    public void removeNotification(Notification notification) {
        if (this.notifications.contains(notification)) {
            this.notifications.remove(notification);
        }
    }

    public void removeAllNotificiations(){
        notifications.clear();
    }

    /**
     * Get the number of notifications in the list of notifications.
     *
     * @return Size of the Notifications list
     */
    public int size() {
        return notifications.size();
    }

    /**
     * Checks whether a listing exists for a certain book in this library.
     *
     * @return True if a notification is found for the notification, false otherwise
     */
    /*public boolean containsNotificationFor(Notification notification) {
        for (Notification notifictationIter : notifications) {
            if (notification.getNotification().equals(notifictationIter)) {
                return true;
            }
        }
        return false;
    }*/

    public ArrayList<Notification> asArray(){
        return notifications;
    }

    @Override
    public Iterator<Notification> iterator() {
        return notifications.iterator();
    }

    public Notifications clone(){
        Notifications cloned = new Notifications();
        for (Notification n: this) cloned.addNotification(n.clone());
        return cloned;
    }


    public Notification getNotificationAtPosition(int position) {
        return notifications.get(position);
    }
}
