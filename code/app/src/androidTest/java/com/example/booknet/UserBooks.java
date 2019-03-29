package com.example.booknet;

import static org.junit.Assert.*;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.InstrumentationTestCase;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.*;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class UserBooks extends ActivityTestRule<LoginPageActivity> {

    private Solo solo;

    Random rand = new Random();
    private String testRandom = "_" + rand.nextInt(10000); // Gives n such that 0 <= n < 20

    public UserBooks(){
        super(LoginPageActivity.class);
    }

    @Rule
    public ActivityTestRule<LoginPageActivity> rule =
            new ActivityTestRule<>(LoginPageActivity.class, true,
           true);

    /**
     *  Brings the UI tests to the LoginPageActivity before each test is run.
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    /**
     * Attempt to add a book, and make sure it appears correctly. Attempt to edit the book
     * information, confirming that it is properly edited. Then remove said book, confirming
     * that it is properly removed.
     */
    @Test
    public void addEditRemoveBook(){

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_mybooks"));

        solo.clickOnButton("Add Book");

        solo.assertCurrentActivity("Wrong Activity", OwnListingViewActivity.class);

        solo.enterText((EditText) solo.getView(R.id.isbnField),"0000000000000000000000000000000" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.titleField),"Coraline" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.authorField),"Neil Gaiman" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.descriptionField),"A story to strike fear into the hearts of all" + testRandom);

        solo.clickOnButton("Add");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        assertTrue(solo.searchText("Neil Gaiman" + testRandom));
        assertTrue(solo.searchText("Coraline" + testRandom));
        assertTrue(solo.searchText("0000000000000000000000000000000" + testRandom));

        //todo click on the first item in the list

        solo.assertCurrentActivity("Wrong Activity", OwnListingViewActivity.class);

        solo.clickOnView(solo.getView("editButton"));

        solo.clearEditText((EditText) solo.getView(R.id.isbnField));
        solo.clearEditText((EditText) solo.getView(R.id.titleField));
        solo.clearEditText((EditText) solo.getView(R.id.authorField));
        solo.clearEditText((EditText) solo.getView(R.id.descriptionField));

        solo.enterText((EditText) solo.getView(R.id.isbnField),"0000000000000000000000000000001" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.titleField),"Turtles All The Way Down" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.authorField),"John Green" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.descriptionField),"A story about a girl finding a lost father" + testRandom);

        solo.clickOnButton("Apply");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        assertTrue(solo.searchText("John Green" + testRandom));
        assertTrue(solo.searchText("Turtles All The Way Down" + testRandom));
        assertTrue(solo.searchText("0000000000000000000000000000001" + testRandom));

        //todo click on the first item in the list

        solo.assertCurrentActivity("Wrong Activity", OwnListingViewActivity.class);

        solo.clickOnButton("deleteButton");

        assertFalse(solo.searchText("John Green" + testRandom));
        assertFalse(solo.searchText("Turtles All The Way Down" + testRandom));
        assertFalse(solo.searchText("0000000000000000000000000000001" + testRandom));


    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
