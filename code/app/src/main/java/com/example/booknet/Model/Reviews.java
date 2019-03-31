package com.example.booknet.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Reviews implements Serializable, Iterable<Review> {

    private ArrayList<Review> reviews;

    /**
     * Constructor
     */
    public Reviews() {
        this.reviews = new ArrayList<>();
    }

    /**
     * Gets the list of Reviews.
     *
     * @return ArrayList containing Reviews
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /**
     * Adds a Review to the arraylist.
     *
     * @param review The Review to add
     */
    public void addReview(Review review) {
        this.reviews.add(review);
    }

    /**
     * Removes a InAppNotification from the library.
     *
     * @param review The Review to remove
     */
    public void removeReview(Review review) {
        if (this.reviews.contains(review)) {
            this.reviews.remove(review);
        }
    }

    public void removeAllReviews(){
        reviews.clear();
    }

    /**
     * Get the number of notifications in the list of notifications.
     *
     * @return Size of the InAppNotifications list
     */
    public int size() {
        return reviews.size();
    }

    @Override
    public Iterator<Review> iterator() {
        return reviews.iterator();
    }

    public Reviews getReviews(String username) {
        Reviews currentUserReviews = new Reviews();
        for (Review r: this) {
            if (r.getReviewedUsername().equals(username)) {
                currentUserReviews.addReview(r.clone());
            }
        }
        return currentUserReviews;
    }

    public Review getReviewAtPosition(int position) {
        return reviews.get(position);
    }
}
