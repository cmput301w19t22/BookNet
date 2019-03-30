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
public class SearchBooks extends ActivityTestRule<LoginPageActivity> {

    private Solo solo;

    Random rand = new Random();
    private String testRandom = "_" + rand.nextInt(10000); // Gives n such that 0 <= n < 1000

    public SearchBooks(){
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

    @Test
    /**
     *  Checks for seaching the books, and making sure all the books contained in the search listing
     *  are valid for the search.
     */
    public void searchBooks(){
        //todo search various known book titles, to see if the search works properly.

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_search"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //todo implement clicking on searchView and entering "Harry Potter"

        assertTrue(solo.searchText("J.K. Rowling"));

        //todo implement clicking on searchView and entering "John Locke"

        assertTrue(solo.searchText("Lost: The Book"));

        //todo implement clicking on the searchView and entering "4815162342"

        assertTrue(solo.searchText("John Locke"));

        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnButton("Logout");




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
