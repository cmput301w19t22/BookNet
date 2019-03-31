package com.example.booknet;

import com.example.booknet.Model.Review;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ReviewTest {

    @Test
    public void Constructors() {
        String reviewer = "1";
        String reviewed = "2";
        Review review = new Review(reviewer, reviewed, 5, "review1");

        //Basic
        assertEquals(reviewer, review.getReviewerUsername());
        assertEquals(reviewed, review.getReviewedUsername());
        assertEquals(5f, review.getScore());
        assertEquals("review1", review.getMessage());

    }

    @Test
    public void Setters() {
        String reviewer = "1";
        String reviewed = "2";
        Review review = new Review(reviewer, reviewed, 5, "review1");

        review.setMessage("newmessage");
        review.setScore(3);

        assertEquals(3, review.getScore());
        assertEquals("newmessage", review.getMessage());
    }

    @Test
    public void ScoreRange() {
        String reviewer = "1";
        String reviewed = "2";
        Review review = new Review(reviewer, reviewed, 5, "review1");

        //Negative
        review.setScore(-1);
        assertNotEquals(-1, review.getScore());
        //Over Range
        review.setScore(6);
        assertNotEquals(6, review.getScore());
        //Zero
        review.setScore(0);
        assertNotEquals(0, review.getScore());
    }

    @Test
    public void UtilityScoreStarFill() {
        for (float s = 0f; s <= 5.0f; s += 0.1f) {
            int f1 = Review.starFill(s, 0);
            if (s < 0.5f) {
                assertEquals(String.format("Empty?: %f", s), 0, f1);
            } else if (s >= 0.5f && s < 1f) {
                assertEquals(String.format("Half?: %f", s), 1, f1);
            } else {
                assertEquals(String.format("Full?: %f", s), 2, f1);
            }
        }

    }
}
