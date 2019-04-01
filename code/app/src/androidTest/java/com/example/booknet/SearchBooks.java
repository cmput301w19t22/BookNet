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
import android.widget.Spinner;

import com.example.booknet.Activities.LoginPageActivity;
import com.example.booknet.Activities.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.regex.Pattern;

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
    public void searchBooks() throws Throwable {

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_search"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.searchBar));

        final SearchView searchBar = (SearchView) solo.getView(R.id.searchBar);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                searchBar.setQuery("Lost: The Book", true);

            }
        });

        assertTrue("Can't see ISBN", solo.searchText(Pattern.quote("4815162342"),true));
        //assertFalse("Can see ISBN", solo.searchText(Pattern.quote("19800731"), true));
        //todo solve issue with invisible text being registered.

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                searchBar.setQuery("John Locke", true);

            }
        });

        assertTrue("Can't see ISBN", solo.searchText(Pattern.quote("Lost: The Book"), true));
        //assertFalse("Can see ISBN", solo.searchText(Pattern.quote("Harry Potter"), true));
        //todo solve issue with invisible text being registered.

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                searchBar.setQuery("4815162342", true);

            }
        });

        assertTrue("Can't see author", solo.searchText(Pattern.quote("John Locke"), true));
        //assertFalse("Can see author", solo.searchText(Pattern.quote("J.K. Rowling"), true));
        //todo solve issue with invisible text being registered.

        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.logoutButton));



    }

    @Test
    /**
     * Checks for filtering the books, and making sure all the books contained in the search listing
     * are valid for the search.
     */
    public void filterBooks() throws Throwable {

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_search"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        final Spinner statusSpinner = (Spinner) solo.getView(R.id.searchFilter);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                statusSpinner.setSelection(1);

            }
        });


        assertFalse("Can see requested books", solo.searchText(Pattern.quote("Requested"), true));
        solo.scrollToTop();
        assertFalse("Can see accepted books", solo.searchText(Pattern.quote("Accepted"), true));
        solo.scrollToTop();
        assertFalse("Can see accepted books", solo.searchText(Pattern.quote("Borrowed"), true));
        solo.scrollToTop();
        //todo solve issue with invisible text being registered.


        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                statusSpinner.setSelection(2);
            }
        });


        assertFalse("Can see available books", solo.searchText(Pattern.quote("Available"),true));
        solo.scrollToTop();
        assertFalse("Can see accepted books", solo.searchText(Pattern.quote("Accepted"), true));
        solo.scrollToTop();
        assertFalse("Can see borrowed books", solo.searchText(Pattern.quote("Borrowed"), true));
        solo.scrollToTop();
        //todo solve issue with invisible text being registered.


        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                statusSpinner.setSelection(3);

            }
        });


        assertFalse("Can see available books", solo.searchText(Pattern.quote("Available"),true));
        solo.scrollToTop();
        assertFalse("Can see requested books", solo.searchText(Pattern.quote("Requested"), true));
        solo.scrollToTop();
        assertFalse("Can see accepted books", solo.searchText(Pattern.quote("Borrowed"), true));
        solo.scrollToTop();
        //todo solve issue with invisible text being registered.


        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                statusSpinner.setSelection(4);

            }
        });


        assertFalse("Can see available books", solo.searchText(Pattern.quote("Available"),true));
        solo.scrollToTop();
        assertFalse("Can see requested books", solo.searchText(Pattern.quote("Requested"), true));
        solo.scrollToTop();
        assertFalse("Can see accepted books", solo.searchText(Pattern.quote("Accepted"), true));
        solo.scrollToTop();
        //todo solve issue with invisible text being registered.



        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.logoutButton));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
