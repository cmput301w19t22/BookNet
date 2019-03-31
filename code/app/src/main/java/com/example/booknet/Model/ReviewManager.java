package com.example.booknet.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the collection of reviews from the database
 */
public class ReviewManager {

    //Collection to keep the reviews in each entry in this map is the list
    private Map<String, ReviewList> userReviews = new HashMap<>();

    /**
     * Adds the review to the collection.
     *
     * @param review
     */
    public void addReview(Review review) {
        //Create a new collection if the user has no reviews
        if (!userReviews.containsKey(review.getReviewedUsername())) {
            userReviews.put(review.getReviewedUsername(), new ReviewList());
        }
        userReviews.get(review.getReviewedUsername()).addReview(review);
    }

    /**
     * Gets the reviews for a given user.
     *
     * @param user The user whose reviews to obtain.
     * @return A ReviewList collection, which is empty if no reviews found.
     */
    public ReviewList getReviews(String user) {
        if (userReviews.containsKey(user)) {
            return userReviews.get(user);
        } else {
            //Return an empty collection
            return new ReviewList();
        }
    }

    /**
     * Clears the data structure.
     */
    public void clear() {
        userReviews.clear();
    }

}
