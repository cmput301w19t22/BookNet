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

public class UserBooks {

    @Before
    /**
     *  Brings the UI tests to the MyBooks Activity before each test is run.
     */
    public void getToUserBooks(){

    }


    @Test
    /**
     * Attempt to add a book, and make sure it appears correctly. Attempt to edit the book
     * information, confirming that it is properly edited. Then remove said book, confirming
     * that it is properly removed.
     */
    public void addEditRemoveBook(){

    }


}
