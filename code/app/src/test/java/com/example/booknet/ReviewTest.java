package com.example.booknet;
import com.example.booknet.Model.Review;
import com.example.booknet.Model.UserAccount;

import org.junit.Test;
import static org.junit.Assert.*;
public class ReviewTest {

    @Test
    public void Constructors(){
        String reviewer = new UserAccount("1");
        String reviewed = new UserAccount("2");
        Review review = new Review(reviewer,reviewed,5,"review1");

        //Basic
        assertEquals(reviewer,review.getReviewerUsername());
        assertEquals(reviewed,review.getReviewedUsername());
        assertEquals(5,review.getScore());
        assertEquals("review1",review.getMessage());

    }
    @Test
    public void Setters(){
        String reviewer = new UserAccount("1");
        String reviewed = new UserAccount("2");
        Review review = new Review(reviewer,reviewed,5,"review1");

        review.setMessage("newmessage");
        review.setScore(3);

        assertEquals(3,review.getScore());
        assertEquals("newmessage",review.getMessage());
    }

    @Test
    public void ScoreRange(){
        String reviewer = new UserAccount("1");
        String reviewed = new UserAccount("2");
        Review review = new Review(reviewer,reviewed,5,"review1");

        //Negative
        review.setScore(-1);
        assertNotEquals(-1,review.getScore());
        //Over Range
        review.setScore(6);
        assertNotEquals(6,review.getScore());
        //Zero
        review.setScore(0);
        assertNotEquals(0,review.getScore());
    }
}
