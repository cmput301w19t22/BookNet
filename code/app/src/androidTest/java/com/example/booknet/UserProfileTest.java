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
import com.example.booknet.Activities.ProfileEditActivity;
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
public class UserProfileTest extends ActivityTestRule<LoginPageActivity> {

    private Solo solo;

    Random rand = new Random();
    private String testRandom = "_" + rand.nextInt(10000); // Gives n such that 0 <= n < 20

    public UserProfileTest(){
        super(LoginPageActivity.class);
    }

    @Rule
    public ActivityTestRule<LoginPageActivity> rule =
            new ActivityTestRule<>(LoginPageActivity.class, true,
            true);




    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    /**
     *  Make sure that the profile logged in to is the one displayed.
     */
    public void verifyProfile(){

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        assertTrue(solo.searchText("test1@gmail.com"));
        assertTrue(solo.searchText("test1"));
        assertTrue(solo.searchText("111-222-3333"));

        solo.clickOnView(solo.getView(R.id.logoutButton));

    }

    @Test
    /**
     *  Edit the profile, and make sure the edits take place properly.
     */
    public void editProfile(){

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("editButton"));

        solo.assertCurrentActivity("Wrong Activity", ProfileEditActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.phoneField));
        solo.clearEditText((EditText) solo.getView(R.id.emailField));

        solo.enterText((EditText) solo.getView(R.id.phoneField),"444-555-6666" + testRandom);
        solo.enterText((EditText) solo.getView(R.id.emailField),"test" + testRandom + "@gmail.com");

        solo.clickOnView(solo.getView(R.id.applyButton));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        assertTrue(solo.searchText("test" + testRandom + "@gmail.com"));
        assertTrue(solo.searchText("test1"));
        assertTrue(solo.searchText("444-555-6666" + testRandom));

        solo.clickOnView(solo.getView("editButton"));

        solo.assertCurrentActivity("Wrong Activity", ProfileEditActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.phoneField));
        solo.clearEditText((EditText) solo.getView(R.id.emailField));

        solo.enterText((EditText) solo.getView(R.id.phoneField),"111-222-3333");
        solo.enterText((EditText) solo.getView(R.id.emailField),"test1@gmail.com");

        solo.clickOnView(solo.getView(R.id.applyButton));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        assertTrue(solo.searchText("test1@gmail.com"));
        assertTrue(solo.searchText("test1"));
        assertTrue(solo.searchText("111-222-3333"));

        solo.clickOnView(solo.getView(R.id.logoutButton));

    }

}
