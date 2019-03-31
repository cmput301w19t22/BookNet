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
     * Gets the list of ReviewList.
     *
     * @return ArrayList containing ReviewList
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
     * Removes a Notification from the library.
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
     * @return Size of the Notifications list
     */
    public int size() {
        return reviews.size();
    }

    @Override
    public Iterator<Review> iterator() {
        return reviews.iterator();
    }

    public ReviewList getReviews(String username) {
        ReviewList currentUserReviewList = new ReviewList();
        for (Review r: this) {
            if (r.getReviewedUsername().equals(username)) {
                currentUserReviewList.addReview(r.clone());
            }
        }
        return currentUserReviewList;
    }

    public Review getReviewAtPosition(int position) {
        return reviews.get(position);
    }
}
