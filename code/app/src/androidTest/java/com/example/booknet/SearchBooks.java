package com.example.booknet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;



import org.junit.Test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class SearchBooks {

    @Before
    /**
     *  Brings the UI tests to the BookSearch Activity before each test is run.
     */
    public void getToSearchBooks(){
        //todo step through the various UI, including logging in to get to the SearchBooks activity.

    }

    @Test
    /**
     *  Checks for seaching the books, and making sure all the books contained in the search listing
     *  are valid for the search.
     */
    public void searchBooks(){
        //todo search various known book titles, to see if the search works properly.

    }

    @Test
    /**
     * Checks for filtering the books, and making sure all the books contained in the search listing
     * are valid for the search.
     */
    public void filterBooks(){
        //todo filter by available, accepted, borrowed, and requested
    }



}
