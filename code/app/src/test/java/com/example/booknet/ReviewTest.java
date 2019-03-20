package com.example.booknet;
import org.junit.Test;
import static org.junit.Assert.*;
public class ReviewTest {

    @Test
    public void Constructors(){
        UserAccount reviewer = new UserAccount("test_username_1");
        UserAccount reviewed = new UserAccount("test_username_2");
        Review review = new Review(reviewer,reviewed,5,"review1");

        //Basic
        assertEquals(reviewer,review.getReviewerAccount());
        assertEquals(reviewed,review.getReviewedAccount());
        assertEquals(5,review.getScore());
        assertEquals("review1",review.getMessage());

    }
    @Test
    public void Setters(){
        UserAccount reviewer = new UserAccount("test_username_1");
        UserAccount reviewed = new UserAccount("test_username_2");
        Review review = new Review(reviewer,reviewed,5,"review1");

        review.setMessage("newmessage");
        review.setScore(3);

        assertEquals(3,review.getScore());
        assertEquals("newmessage",review.getMessage());
    }

    @Test
    public void ScoreRange(){
        UserAccount reviewer = new UserAccount("test_username_1");
        UserAccount reviewed = new UserAccount("test_username_2");
        Review review = new Review(reviewer,reviewed,5,"review1");

        //Negative
        review.setScore(-1);
        assertNotEquals(-1,review.getScore());
        //Over Range
        review.setScore(6);
        assertNotEquals(6,review.getScore());
        //Zero
        review.setScore(0);
        assertEquals(0,review.getScore());
    }

    @Test
    public void EditReview(){
        UserAccount reviewer = new UserAccount("test_username_1");
        UserAccount reviewed = new UserAccount("test_username_2");
        Review review = new Review(reviewer,reviewed,5,"message");

        //review.editReview(reviewer,reviewed,3,"newmessage");
        //todo: implement editing review?

        assertEquals(3,review.getScore());
        assertEquals("newmessage",review.getMessage());

        //Check values
        assertEquals(reviewer,review.getReviewerAccount());
        assertEquals(reviewed,review.getReviewedAccount());
        assertNotEquals(5,review.getScore());
        assertNotEquals("message",review.getMessage());
    }
}
