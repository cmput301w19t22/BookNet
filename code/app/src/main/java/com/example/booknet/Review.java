package com.example.booknet;

import java.io.Serializable;

public class Review implements Serializable {
    private UserAccount reviewerAccount;
    private UserAccount reviewedAccount;
    private int score;
    private String message;

    // Constructor
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

    public static void editReview(UserAccount reviewerAccount,
                                    UserAccount reviewedAccount,
                                    int score, String message){
        //TODO: implement
    }
}
