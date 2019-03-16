package com.example.booknet;

import java.io.Serializable;

/**
 * Data structure to represent a review for a user.
 */
public class Review implements Serializable {
    //Attributes
    private UserAccount reviewerAccount;
    private UserAccount reviewedAccount;
    private int score;
    private String message;

    /**
     * Creates a review using with all attributes filled
     *
     * @param reviewerAccount Name of the user who made the review
     * @param reviewedAccount Name of the user who is reviewed
     * @param score           The score of the review
     * @param message         A message attached to the review
     */
    public Review(UserAccount reviewerAccount, UserAccount reviewedAccount, int score, String message) {
        this.reviewerAccount = reviewerAccount;
        this.reviewedAccount = reviewedAccount;
        this.score = score;
        this.message = message;
    }

    //#region Getters and Setters
    public UserAccount getReviewerAccount() {
        return reviewerAccount;
    }

    public void setReviewerAccount(UserAccount reviewerAccount) {
        this.reviewerAccount = reviewerAccount;
    }

    public UserAccount getReviewedAccount() {
        return reviewedAccount;
    }

    public void setReviewedAccount(UserAccount reviewedAccount) {
        this.reviewedAccount = reviewedAccount;
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

    //#endregion
}
