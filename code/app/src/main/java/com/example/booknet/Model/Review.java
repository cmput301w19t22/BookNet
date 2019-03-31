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
    private int score;
    private String message;
    private int dupId;

    private DatabaseManager manager = DatabaseManager.getInstance();

    /**
     * Creates a review using with all attributes filled
     *
     * @param reviewerUsername  Name of the user who made the review
     * @param reviewedUsername  Name of the user who is reviewed
     * @param score             The score of the review
     * @param message           A message attached to the review
     */
    public Review(String reviewedUsername, String reviewerUsername, int score, String message) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
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

    public Review clone(){
        return new Review(reviewedUsername, reviewerUsername, score, message);
    }
}
