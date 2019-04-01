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
import android.widget.SearchView;

import com.example.booknet.Activities.LoginPageActivity;
import com.example.booknet.Activities.MainActivity;
import com.example.booknet.Activities.OwnListingViewActivity;
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
    private String testRandomNumber = Integer.toString(rand.nextInt(10000)); // Gives n such that 0 <= n < 1000
    private String testRandom = "_" + testRandomNumber;

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
    public void addEditRemoveBook() throws Throwable {

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_mybooks"));

        solo.clickOnView(solo.getView(R.id.addBookButton));

        solo.enterText((EditText) solo.getView(R.id.isbnField),"0000" + testRandomNumber);
        solo.enterText((EditText) solo.getView(R.id.titleField),"Coraline" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.authorField),"Neil Gaiman" + testRandom);

        solo.clickOnButton("Add");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        assertTrue(solo.searchText("Neil Gaiman" + testRandom));
        assertTrue(solo.searchText("Coraline" + testRandom));
        assertTrue(solo.searchText("0000" + testRandomNumber));

        //todo click on the first item in the list

        final RecyclerView personalBookLibrary = (RecyclerView) solo.getView(R.id.bookLibrary);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                personalBookLibrary.getChildAt(0).performClick();
                //searchBar.setQuery("Lost: The Book", true);

            }
        });
        //solo.clickOnText("Neil Gaiman" + testRandom);
        //solo.clickOnText("Coraline" + testRandom);
        //solo.clickOnView(solo.getView(R.id.bookLayout,1));
        //solo.clickInRecyclerView(1);

        solo.assertCurrentActivity("Wrong Activity", OwnListingViewActivity.class);

        solo.clickOnView(solo.getView("editButton"));

        //solo.clearEditText((EditText) solo.getView(R.id.isbnField));
        solo.clearEditText((EditText) solo.getView(R.id.titleField));
        solo.clearEditText((EditText) solo.getView(R.id.authorField));

        //solo.enterText((EditText) solo.getView(R.id.isbnField),"0001" + testRandomNumber);
        solo.enterText((EditText) solo.getView(R.id.titleField),"Turtles All The Way Down" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.authorField),"John Green" + testRandom);

        solo.clickOnView(solo.getView("addButton"));

        solo.clickOnView(solo.getView(R.id.backButton));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        assertTrue(solo.searchText("John Green" + testRandom));
        assertTrue(solo.searchText("Turtles All The Way Down" + testRandom));
        assertTrue(solo.searchText("0000" + testRandomNumber));

        //todo click on the first item in the list
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                personalBookLibrary.getChildAt(0).performClick();
                //searchBar.setQuery("Lost: The Book", true);

            }
        });

        solo.assertCurrentActivity("Wrong Activity", OwnListingViewActivity.class);

        solo.clickOnView(solo.getView(R.id.deleteButton));

        solo.clickOnButton("Delete");

        assertFalse(solo.searchText("John Green" + testRandom));
        assertFalse(solo.searchText("Turtles All The Way Down" + testRandom));
        assertFalse(solo.searchText("0000" + testRandomNumber));

        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.logoutButton));




    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
