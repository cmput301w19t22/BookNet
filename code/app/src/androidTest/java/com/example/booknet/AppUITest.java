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

import com.example.booknet.Activities.LoginPageActivity;
import com.example.booknet.Activities.MainActivity;
import com.example.booknet.Activities.OwnListingViewActivity;
import com.example.booknet.Activities.ReviewListViewActivity;
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
public class AppUITest extends ActivityTestRule<LoginPageActivity> {


    private Solo solo;

    Random rand = new Random();
    private String testRandom = "_" + rand.nextInt(10000); // Gives n such that 0 <= n < 1000

    public AppUITest(){
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
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }


    @Test
    public void stepThroughUI(){
        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_search"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.searchText("Search"));

        solo.clickOnView(solo.getView( R.id.navigation_mybooks));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.searchText("Your Books"));

        solo.clickOnView(solo.getView( R.id.navigation_myrequests));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.searchText("Requested Books"));

        solo.clickOnView(solo.getView( R.id.navigation_myaccount));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.searchText("Profile"));


        solo.clickOnButton("Reviews");

        solo.assertCurrentActivity("Wrong Activity", ReviewListViewActivity.class);

        solo.clickOnView(solo.getView(R.id.backButton));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.searchText("Profile"));

        solo.clickOnView(solo.getView( R.id.navigation_notifications));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.searchText("Notifications"));

        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.logoutButton));

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);



    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
