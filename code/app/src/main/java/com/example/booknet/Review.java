package com.example.booknet;

public class Review {
    private UserAccount reviewerAccount;
    private UserAccount reviewedAccount;
    private int score;
    private String message;

    // Constructors
    public Review(UserAccount reviewerAccount, UserAccount reviewedAccount, int score, String message) {
        this.reviewerAccount = reviewerAccount;
        this.reviewedAccount = reviewedAccount;
        this.score = score;
        this.message = message;
    }
    public Review(UserAccount reviewerAccount, UserAccount reviewedAccount, int score) {
        this.reviewerAccount = reviewerAccount;
        this.reviewedAccount = reviewedAccount;
        this.score = score;
        this.message = "";
    }
    public Review(UserAccount reviewerAccount, UserAccount reviewedAccount) {
        this.reviewerAccount = reviewerAccount;
        this.reviewedAccount = reviewedAccount;
    }
    public Review(UserAccount reviewerAccount) {
        this.reviewerAccount = reviewerAccount;
    }

    // Basic Getters and Setters
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


    public static void editReview(UserAccount reviewerAccount,
                                    UserAccount reviewedAccount,
                                    int score, String message){
        //TODO: implement
    }
}
