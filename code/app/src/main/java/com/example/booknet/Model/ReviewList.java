package com.example.booknet.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ReviewList implements Serializable, Iterable<Review> {

    private ArrayList<Review> reviews;

    /**
     * Constructor
     */
    public ReviewList() {
        this.reviews = new ArrayList<>();
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
     * Get the number of notifications in the list of notifications.
     *
     * @return Size of the InAppNotificationList list
     */
    public int size() {
        return reviews.size();
    }

    @Override
    public Iterator<Review> iterator() {
        return reviews.iterator();
    }

    public Review getReviewAtPosition(int position) {
        return reviews.get(position);
    }
}
