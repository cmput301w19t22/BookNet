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


public class LoginTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void loginTest(){

        // Enter email.
        onView(withId(R.id.etEmailAddr))
                .perform(typeText("test1@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.etPassword))
                .perform(typeText("password"), closeSoftKeyboard());

        onView(withId(R.id.btnSignIn)).perform(click());




    }

}
