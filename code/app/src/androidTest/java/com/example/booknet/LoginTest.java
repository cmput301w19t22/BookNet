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

import com.example.booknet.Activities.AccountCreateActivity;
import com.example.booknet.Activities.LoginPageActivity;
import com.example.booknet.Activities.MainActivity;
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
public class LoginTest extends ActivityTestRule<LoginPageActivity> {


    private Solo solo;

    Random rand = new Random();
    private String testRandomNum = "_" + rand.nextInt(10000); // Gives n such that 0 <= n < 20
    private String testRandom = "_" + testRandomNum;

    public LoginTest(){
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
     * Test logging into a test user account.
     */
    public void validLoginTest(){

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.logoutButton));

    }

    @Test
    /**
     * Attempt logging in to an invalid account, and confirm that it doesn't log in.
     */
    public void invalidLoginTest(){

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test" + testRandom + "@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

    }


    @Test
    /**
     * Create an account, and confirm that the account is created and logged in.
     */
    public void registerNewAccountAndLogin(){

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.clickOnButton("Create Account");

        solo.assertCurrentActivity("Wrong Activity", AccountCreateActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etEmailAddr),"test" + testRandom + "@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.etPassword),"password");
        solo.enterText((EditText) solo.getView(R.id.usernameField),"test" + testRandom );
        solo.enterText((EditText) solo.getView(R.id.phoneField), "555-555-" + testRandomNum);

        solo.clickOnButton("Create Account");

        solo.assertCurrentActivity("Wrong Activity", LoginPageActivity.class);

        solo.clickOnButton("Sign In");

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView("navigation_myaccount"));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.logoutButton));



    }


}
