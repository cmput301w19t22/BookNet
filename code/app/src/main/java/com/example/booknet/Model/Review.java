package com.example.booknet.Model;

import com.example.booknet.DatabaseManager;

import java.io.Serializable;

/**
 * Data structure to represent a review for a user.
 */
public class Review implements Serializable {
    //Attributes
    private String reviewerUsername;
    private String reviewedUsername;
    private float score;
    private String message;
    private int dupId;

    private DatabaseManager manager = DatabaseManager.getInstance();

    /**
     * Creates a review using with all attributes filled
     *
     * @param reviewerUsername Name of the user who made the review
     * @param reviewedUsername Name of the user who is reviewed
     * @param score            The score of the review
     * @param message          A message attached to the review
     */
    public Review(String reviewedUsername, String reviewerUsername, float score, String message) {
        this.reviewedUsername = reviewedUsername;
        this.reviewerUsername = reviewerUsername;
        this.score = score;
        this.message = message;
        this.dupId = manager.getReviewDupCount(reviewedUsername, reviewerUsername);
    }

    public Review() {
        this.reviewedUsername = "";
        this.reviewerUsername = "";
        this.score = 0;
        this.message = "";
        this.dupId = 0;
    }

    //#region Getters and Setters
    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }

    public String getReviewedUsername() {
        return reviewedUsername;
    }

    public void setReviewedUsername(String reviewedUsername) {
        this.reviewedUsername = reviewedUsername;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDupId() {
        return dupId;
    }

    public void setDupId(int dupId) {
        this.dupId = dupId;
    }

    public Review clone() {
        return new Review(reviewedUsername, reviewerUsername, score, message);
    }

    /**
     * Utility method to determine the fill amount of a given star and rating value.
     *
     * @param score The score to use
     * @param star  Which star's fill amount we want to know (0 to 4)
     * @return The fill amount:
     * 0-empty
     * 1-half
     * 2-filled
     */
    public static int starFill(float score, int star) {
        float fill = (float) (Math.round((score - star) * 4f) / 4f);
        if (fill >= 1) {
            return 2;
        } else if (fill <= 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Utility function to select a drawable for a star given a score.
     *
     * @param score     The score to use.
     * @param star      Which star we are selecting for
     * @param drawables An 3 entry array of the drawable ids to choose from
     * @return The drawable id which was selected.
     */
    public static int starImage(float score, int star, int[] drawables) {
        int result = starFill(score, star);
        if (drawables.length > result) {
            return drawables[result];
        } else {
            //If the array was not large enough just return the last element.
            return drawables[drawables.length - 1];
        }
    }
}
